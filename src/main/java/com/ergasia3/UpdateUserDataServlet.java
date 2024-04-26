package com.ergasia3;

import com.ergasia3.utils.DbUtil;
import com.ergasia3.utils.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

import static com.ergasia3.LoginServlet.session;

@WebServlet("/updateUserData")
public class UpdateUserDataServlet extends HttpServlet {
    String update;
    String currentName;
    String currentUsername;
    String currentPassword;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException //appears the form for changing username/name or password, which depends on the choice of user
    {
        update = req.getParameter("typeUpdate");
        currentUsername = (String) session.getAttribute("uname"); //selects the username from the session
        currentPassword = "";
        //selects the name and password for the user who is connected with the username which is known from his session
        try {
            //Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "qwerty");
            final Connection connection = DbUtil.getConnection();

            //selects the other information for the user with the username same as the attribute of the session
            final String sql = "SELECT name, password, role FROM user WHERE username=?";

            final PreparedStatement pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, currentUsername);
            final ResultSet resultSet = pstatement.executeQuery();
            if (resultSet.next())
            {
                currentName = resultSet.getString("name");
                currentPassword = resultSet.getString("password");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        //appears the form of changing name or username
        if(update.equals("nameUsername"))
        {
            req.setAttribute("updating", "Change username");
            req.setAttribute("updateInput", "<div><label>name:</label>"+
                    "<input required type=\"text\" name=\"name\" value=\""+ currentName +"\"></div>"+
                    "<div><label>username:</label>" +
                    "<input required type=\"text\" name=\"username\" value=\""+ currentUsername +"\"></div><input type=\"submit\">");
            req.getRequestDispatcher("updateUserData.jsp").forward(req, resp);
        }
        //appears the form of changing password
        else if(update.equals("password"))
        {
            req.setAttribute("updating", "Change password");
            req.setAttribute("updateInput", "<div><label>current password:</label>" +
                    "<input required type=\"password\" name=\"currentPassword\"></div>" +
                    "<div><label>new password:</label>" +
                    "<input required type=\"password\" name=\"newPassword\"></div>" +
                    "<div><label>Confirm password:</label>" +
                    "<input type=\"password\" name=\"confirmPassword\"></div><input type=\"submit\">");
            req.getRequestDispatcher("updateUserData.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  //makes the changes in the user attributes: name, username or password
    {
        try {
            final Connection connection = DbUtil.getConnection();

            //updates the username and name
            if(update.equals("nameUsername"))
            {
                changeNameUsername(req, resp, connection);
            }
            //updates the password
            else if (update.equals("password"))
            {
                changePassword(req, resp, connection);
            }
            // update the attributes after the changes are done
            final String updatedUsername = req.getParameter("username");
            final HttpSession session = req.getSession();
            session.setAttribute("uname", updatedUsername);
            currentName = req.getParameter("name");
            currentUsername = updatedUsername;
            currentPassword = req.getParameter("confirmPassword");

            req.getRequestDispatcher("updateUserData.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeNameUsername(HttpServletRequest req, HttpServletResponse resp, Connection connection)
            throws IOException, ServletException, SQLException {
        String newName = req.getParameter("name");
        String newUsername = req.getParameter("username");
        if(newName.equals(currentName) && newUsername.equals(currentUsername))
        {
            req.setAttribute("messageResult", "Nothing has changed!");
            req.getRequestDispatcher("updateUserData.jsp").forward(req, resp);
            return;
        }

        final HttpSession session = req.getSession();
        // directly updating the users (or customers, etc.) table when the username has changed means a foreign
        // key violation, therefore this boolean serves to update the database in a more safe manner.
        // if the username has indeed changed, then the safe method is used, otherwise the direct UPDATE one.
        final boolean shouldUpdateUserTable = newUsername.equals(currentUsername);
        if (session.getAttribute("role").equals("Customer"))
            updateNameUsernameInDB(req, connection, newName, newUsername, "customers", shouldUpdateUserTable);
        else if (session.getAttribute("role").equals("Content Admin"))
            updateNameUsernameInDB(req, connection, newName, newUsername, "content_admins", shouldUpdateUserTable);
        else if (session.getAttribute("role").equals("Admin"))
            updateNameUsernameInDB(req, connection, newName, newUsername, "admins", shouldUpdateUserTable);

        // if the user didn't change his/her username, there's no need to worry about foreign keys and we can
        // just update the users table in one go after updating admins/content admins/customers
        if (shouldUpdateUserTable) {
            final String updateUsersStatement = "UPDATE user SET name='" + newName + "', username='" + newUsername
                    + "' WHERE username='" + currentUsername + "' AND name='" + currentName + "'";
            final Statement statement = connection.createStatement();
            statement.executeUpdate(updateUsersStatement);
        }
        req.setAttribute("messageResult", "The changes were applied!");
    }

    private void updateNameUsernameInDB(final HttpServletRequest req,
                                        final Connection connection,
                                        final String newName,
                                        final String newUsername,
                                        final String tableName, // admins, content_admins or customers
                                        final boolean shouldUpdateUserTable)
            throws SQLException {
        // if the username has changed, neither customers (or content admins etc.) nor the user tables
        // can be updated in one go (the foreign key constraints would fail in either case), so we:
        // 1) insert the new user
        // 2) we modify the table admins/content admins/customers so it points to the user created in (1)
        // 3) THEN we delete the old user
        if (!shouldUpdateUserTable) {
            final PreparedStatement insertstatement = connection.prepareStatement(
                    "INSERT INTO user VALUES (?, ?, ?, ?, ?)"
            );
            insertstatement.setString(1, newName);
            insertstatement.setString(2, newUsername);
            final String copiedSalt = PasswordUtils.getUserSaltFromDB(connection, currentUsername);
            insertstatement.setString(3, currentPassword);
            insertstatement.setString(4, copiedSalt);
            insertstatement.setString(5, (String)session.getAttribute("role"));

            // this can fail if there's already another user with name = newName, username = newUsername
            try {
                insertstatement.executeUpdate();
            }
            catch (SQLException e) {
                req.setAttribute("messageResult",
                        "ERROR: There's already a user with name: " + newName + ", username: " + newUsername
                                + "!"
                );
                return;
            }
        }

        // step (2)
        final String updateUserSubTable =
                "UPDATE " + tableName + " SET user_name='" + newName + "', user_username='" + newUsername + "'" +
                        " WHERE user_name='" + currentName + "' AND user_username='" + currentUsername + "'";
        final Statement statement = connection.createStatement();
        statement.executeUpdate(updateUserSubTable);

        // now we delete the old user (step (3))
        if (!shouldUpdateUserTable) {
            final PreparedStatement deleteOldUserStmt = connection.prepareStatement(
                    "DELETE FROM user WHERE name=? AND username=?"
            );
            deleteOldUserStmt.setString(1, currentName);
            deleteOldUserStmt.setString(2, currentUsername);
            deleteOldUserStmt.executeUpdate();
        }
    }

    private void changePassword(HttpServletRequest req, HttpServletResponse resp, Connection connection)
            throws IOException, ServletException, SQLException {
        final String currentPassText = req.getParameter("currentPassword");
        final String userSalt = PasswordUtils.getUserSaltFromDB(connection, currentUsername);
        final String hashedPassword = PasswordUtils.getSaltedHash(currentPassText, userSalt);

        if(!hashedPassword.equals(currentPassword)) //incorrect password message
        {
            req.setAttribute("messageResult", "Incorrect password!");
            req.getRequestDispatcher("updateUserData.jsp").forward(req, resp);
            return;
        }

        final String newPassword = req.getParameter("newPassword");
        final String confirmPassword = req.getParameter("confirmPassword");
        if(!confirmPassword.equals(newPassword)) //the new password and confirm password are not matched
        {
            req.setAttribute("messageResult", "Failed to change password!");
            req.getRequestDispatcher("updateUserData.jsp").forward(req, resp);
            return;
        }
        final String newHashedPassword = PasswordUtils.getSaltedHash(confirmPassword, userSalt);
        //System.out.println("New password is: " + confirmPassword + userSalt);
        //update the password
        final String sql = "UPDATE user SET password='" + newHashedPassword +
                "' WHERE username='" + currentUsername + "'";

        //final PreparedStatement pstatement = connection.prepareStatement(sql);
        //pstatement.setString(1, newHashedPassword);
        //pstatement.setString(2, currentUsername);
        //pstatement.executeUpdate(sql);
        final Statement statement = connection.createStatement();
        statement.executeUpdate(sql);

        req.setAttribute("messageResult", "The password was changed successfully!");
    }
}
