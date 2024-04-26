package com.ergasia3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/search")
public class SearchUserServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchUsername = req.getParameter("searchUser");
        String response;
        response = searchUser(searchUsername);
        //prints out the result of searching in the same page
        req.setAttribute("errorMsg", response);
        req.getRequestDispatcher("searchUser.jsp").forward(req, resp);
    }
    //searching user if exists by the name or username
    private String searchUser(String name) {

        try
        {
            //connection string to database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "qwerty");
            Statement statement = connection.createStatement();
            String sql = "SELECT `username`, `role` FROM cinema.user \n" +
                    "WHERE `username` = '" + name +"';";
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()) return "The user with value "+ name +" who you are searching is:</h1>" +
                    "<div><b>username:</b> "+ resultSet.getString("username") +" <b>role:</b> "+ resultSet.getString("role");
        }
        catch(Exception e)
        {e.printStackTrace();}
        return "The user with value "+ name +" who you are searching has not found!";
    }
}
