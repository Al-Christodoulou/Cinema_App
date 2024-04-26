<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.ergasia3.UserData" %><%--
  Created by IntelliJ IDEA.
  User: patsa
  Date: 5/19/2023
  Time: 2:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>Register admin</title>
      <link rel="stylesheet" href="cssFiles/navBar.css">
      <link rel="stylesheet" href="cssFiles/formModify.css">
    <link rel="stylesheet" href="cssFiles/tables.css">
    <link rel="stylesheet" href="cssFiles/css.css">
  </head>
  <body>
    <ul class="navbar">
      <li class="navbar"><a href="AdminMenu.html">Main page</a></li>
      <li class="navbar"><a href="viewUsers.jsp">View all users</a></li>
      <li class="navbar"><a href="searchUser.jsp">Search user</a></li>
      <!--<li><a href="previewFilms.jsp">Create user</a></li>!-->
      <li class="navbar"><a href="deleteUser.jsp">Delete user</a></li>
      <li class="navbar"><a href="registerContentAdmin.html">Register content admin</a></li>
      <li class="navbar"><a class="active" href="registerAdmin.jsp">Register admin</a></li>
      <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
      <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
    </ul>
    <div class="divBody">
      <h1>Users of content admin and admin</h1>
        <form action="registerAdmin">
          <table>
            <tr>
              <th></th>
              <th>Username</th>
              <th>Role</th>
            </tr>
            <%
              try {
              ResultSet resultSet = UserData.selectAllUsers(false);
              if(resultSet!=null)
              {
                while (resultSet.next())
                { %>
                <tr>
                  <td><input required type="radio" id="registerAdminId" name="registerAdmin" value=<%= resultSet.getString("username") %>></td>
                  <td> <%= resultSet.getString("username")%> </td>
                  <td> <%= resultSet.getString("role")%> </td>
                </tr>
                <% }}
              } catch (Exception e) {
                e.printStackTrace();
                }%>




          </table>
          <input type="submit">
        </form>
    </div>
  </body>
</html>
