<%--
  Created by IntelliJ IDEA.
  User: patsa
  Date: 5/18/2023
  Time: 11:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>Search user</title>
      <link rel="stylesheet" href="cssFiles/navBar.css">
    <link rel="stylesheet" href="cssFiles/css.css">
  </head>
  <body>
    <ul class="navbar">
      <li class="navbar"><a href="AdminMenu.html">Main page</a></li>
      <li class="navbar"><a href="viewUsers.jsp">View all users</a></li>
      <li class="navbar"><a class="active" href="searchUser.jsp">Search user</a></li>
      <!--<li><a href="previewFilms.jsp">Create user</a></li>!-->
      <li class="navbar"><a href="deleteUser.jsp">Delete user</a></li>
      <li class="navbar"><a href="registerContentAdmin.html">Register content admin</a></li>
      <li class="navbar"><a href="registerAdmin.jsp">Register admin</a></li>
      <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
      <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
    </ul>
    <div class="divBody">
      <h1>Search any user</h1>
      <div>
          <!--search of a user form!-->
          <form action="search">
          <label>Search a username:</label>
          <input type="text" name="searchUser">
          <input type="submit">
        </form>
        <h2>${errorMsg}</h2>
      </div>
    </div>
  </body>
</html>
