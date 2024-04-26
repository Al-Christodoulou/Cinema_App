<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.ergasia3.UserData" %>
<%@ page import="java.sql.SQLException" %><%--
  Created by IntelliJ IDEA.
  User: patsa
  Date: 5/18/2023
  Time: 11:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Delete User</title>
        <link rel="stylesheet" href="cssFiles/navBar.css">
        <link rel="stylesheet" href="cssFiles/tables.css">
        <link rel="stylesheet" href="cssFiles/css.css">
    </head>
    <body>
        <ul class="navbar">
            <li class="navbar"><a href="AdminMenu.html">Main page</a></li>
            <li class="navbar"><a href="viewUsers.jsp">View all users</a></li>
            <li class="navbar"><a href="searchUser.jsp">Search user</a></li>
            <!--<li><a href="previewFilms.jsp">Create user</a></li>!-->
            <li class="navbar"><a class="active" href="deleteUser.jsp">Delete user</a></li>
            <li class="navbar"><a href="registerContentAdmin.html">Register content admin</a></li>
            <li class="navbar"><a href="registerAdmin.jsp">Register admin</a></li>
            <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
            <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
        </ul>
        <div class="divBody">
            <h1>Select the user which is going to be deleted</h1>
                <form action="delete">
                    <table>
                        <tr>
                            <th></th>
                            <th>Username</th>
                            <th>Role</th>
                        </tr>
                        <%
                            ResultSet resultSet = UserData.selectAllUsers(true);
                            if(resultSet!=null)
                            {
                                try {
                                while (resultSet.next()) //appears the users to the admin, who is called to choose and delete any user
                                { %>
                                    <tr>
                                        <td><input required type="radio" id="deleteUserId" name="deleteUser" value="<%= resultSet.getString("username") + "/"+ resultSet.getString("role")%>"> </td>
                                        <td> <%= resultSet.getString("username") %></td>
                                        <td> <%= resultSet.getString("role")%> </td>
                                    </tr>
                                <% }
                            } catch (SQLException e) { e.printStackTrace(); }
                            }%>
                    </table>
                    <input type="submit">
                </form>
            <h2>${errorMsg}</h2>
        </div>
    </body>
</html>
