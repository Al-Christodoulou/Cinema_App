package com.ergasia3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;


@WebServlet("/delete")
public class DeleteUserServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Splits the string below to the username and userType which are both in the same string
        String deleteUserData = req.getParameter("deleteUser");
        String[] splited = deleteUserData.split("/");
        String deleteUsername = splited[0];
        String userType = splited[1];
        int DeletedRows;
        //delete the user
        DeletedRows = deleteUser(deleteUsername, userType);
        //appears the result of deletion depends on the deletedRows, which in this case will be 0 or 1
        if(DeletedRows == 0)
        {
            req.setAttribute("errorMsg", "Oops! " + deleteUsername + " is the only admin!");
            req.getRequestDispatcher("deleteUser.jsp").forward(req, resp);
        }
        //resp.getWriter().println("<html><body><h1>The user with username:" + deleteUsername + "</h1></body></html>");
        else
        {
            req.setAttribute("errorMsg", deleteUsername + " was deleted successfully!");
            req.getRequestDispatcher("deleteUser.jsp").forward(req, resp);
        }
    }

    //delete the user with the username == name
    private int deleteUser(String username, String userType)
    {
        try
        {
            //connection string to database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "qwerty");
            Statement statement = connection.createStatement();
            //In this case checks the count of the admins. When the count is 1, the deletion will fail
            if(userType.equals("Admin"))
            {
                ResultSet resultSet;
                String sql1 = "SELECT `username` FROM `cinema`.`user` WHERE `role`='"+ userType +"'";
                resultSet = statement.executeQuery(sql1);
                int count = 0;
                while (resultSet.next()){count++;}
                System.out.println(count);
                if(count==1){return 0;}
                sql1 = "DELETE FROM `cinema`.`admins`\n" +
                        "WHERE `user_username`= '" + username + "';";
                statement.executeUpdate(sql1);
            }
            else if(userType.equals("Content admin"))
            {
                String sql1 = "DELETE FROM `cinema`.`content_admins`\n" +
                        "WHERE `user_username`= '" + username + "';";
                statement.executeUpdate(sql1);
            }
            else if(userType.equals("Customer"))
            {
                String sql1 = "DELETE FROM `cinema`.`customers`\n" +
                        "WHERE `user_username`= '" + username + "';";
                statement.executeUpdate(sql1);
            }
            //In other cases deletes the chosen row of table user
            String sql2 = "DELETE FROM `cinema`.`user`\n" +
                    "WHERE `username`= '" + username + "';";

            int rowAffected = statement.executeUpdate(sql2);

            connection.close();
            return  rowAffected;
        }
        catch(SQLException e)
        {e.printStackTrace();}
        return  0;
    }
}
