<%@ page import="java.sql.ResultSet" %>
<%@ page import="static com.ergasia3.UserData.selectAllUsers" %><%--
  Created by IntelliJ IDEA.
  User: patsa
  Date: 5/18/2023
  Time: 11:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>View users</title>
        <link rel="stylesheet" href="cssFiles/navBar.css">
        <link rel="stylesheet" href="cssFiles/tables.css">
        <link rel="stylesheet" href="cssFiles/css.css">
    </head>
    <body>
        <ul class="navbar">
            <li class="navbar"><a href="AdminMenu.html">Main page</a></li>
            <li class="navbar"><a class="active" href="viewUsers.jsp">View all users</a></li>
            <li class="navbar"><a href="searchUser.jsp">Search user</a></li>
            <!--<li><a href="previewFilms.jsp">Create user</a></li>!-->
            <li class="navbar"><a href="deleteUser.jsp">Delete user</a></li>
            <li class="navbar"><a href="registerContentAdmin.html">Register content admin</a></li>
            <li class="navbar"><a href="registerAdmin.jsp">Register admin</a></li>
            <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
            <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
        </ul>
        <div class="divBody">
            <h1>Users</h1>
            <table class="viewTable">
                <tr>
                    <th>Username</th>
                    <th>Role</th>
                </tr>
                <% //appears all the users and its attributes on a html table
                    try {
                    ResultSet resultSet = selectAllUsers(true);
                    if(resultSet!=null)
                    {
                        while (resultSet.next())
                        { %>
                         <tr>
                            <td> <%= resultSet.getString("username")%> </td>
                            <td> <%= resultSet.getString("role")%> </td>
                        </tr>
                        <% }
                    }} catch (Exception e) {
                        e.printStackTrace();
                    }%>
            </table>
        </div>
    </body>
</html>
