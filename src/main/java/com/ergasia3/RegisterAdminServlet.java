package com.ergasia3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/registerAdmin")
public class RegisterAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String username = req.getParameter("registerAdmin");

        try {
            Connection connection =  DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "qwerty");

            Statement statement = connection.createStatement();
            //update the role of user from content admin to admin in case of registration of an admin
            String sql = "UPDATE `cinema`.`user`\n" +
                    "SET `role`= 'Admin'" +
                    "WHERE `username`= '"+ username +"';";
            statement.executeUpdate(sql);
            ResultSet resultSet;
            sql = "SELECT `user_name` , `user_username` FROM cinema.content_admins "+
                    "WHERE `user_username`= '"+ username +"'";
            resultSet = statement.executeQuery(sql);
            if(resultSet.next())
            {
                String name = resultSet.getString("user_name");
                sql = "INSERT INTO cinema.admins(`user_name` ,`user_username`)" +
                        "VALUES (?, ?);";
                PreparedStatement prepared = connection.prepareStatement(sql);
                prepared.setString(1, name);
                prepared.setString(2, username);
                prepared.execute();
                prepared.close();
                resp.getWriter().println("<html><body><h2>Successful registration of Admin</h2>" +
                        "<div>The user with username <b>"+ username +"</b> became an <b>Admin</b><br><a href=\"registerAdmin.jsp\">Go Back</a></div></body></html>");
            }
            sql = "DELETE FROM `cinema`.`content_admins` WHERE `user_username`='"+ username +"';";
            statement.executeUpdate(sql);

            statement.close();
            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            resp.getWriter().println("<html><body><h2>There has been an error trying to connect to the database!</h2><br><a href=\"registerAdmin.jsp\">Go Back</a></body></html>");
        }
    }
}
