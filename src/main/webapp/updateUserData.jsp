<%--
  Created by IntelliJ IDEA.
  User: patsa
  Date: 5/29/2023
  Time: 6:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update user data</title>
    <link rel="stylesheet" href="cssFiles/navBar.css">
</head>
<body>
<%if(session.getAttribute("role").equals("Admin")){ %>
<ul class="navbar">
    <li class="navbar"><a href="AdminMenu.html">Main page</a></li>
    <li class="navbar"><a href="viewUsers.jsp">View all users</a></li>
    <li class="navbar"><a href="searchUser.jsp">Search user</a></li>
    <!--<li><a href="previewFilms.jsp">Create user</a></li>!-->
    <li class="navbar"><a href="deleteUser.jsp">Delete user</a></li>
    <li class="navbar"><a href="registerContentAdmin.html">Register content admin</a></li>
    <li class="navbar"><a href="registerAdmin.jsp">Register admin</a></li>
    <li class="navbar"><a class="active" href="updateUserData.jsp">Update user data</a></li>
    <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
</ul>
<%}else if(session.getAttribute("role").equals("Customer")){ %>
<ul class="navbar">
    <li class="navbar"><a href="CustomerMenu.html">Main page</a></li>
    <li class="navbar"><a href="previewFilms.jsp">Show Available Films</a></li>
    <li class="navbar"><a href="makeReservation.jsp">Make Reservation</a></li>
    <li class="navbar"><a href="viewReservation.jsp">View Reservation</a></li>
    <li class="navbar"><a class="active" href="updateUserData.jsp">Update user data</a></li>
    <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
</ul>
<%}else if(session.getAttribute("role").equals("Content Admin")) { %>
<ul class="navbar">
    <li class="navbar"><a href="contentAdminMenu.html">Main page</a></li>
    <li class="navbar"><a href="viewFilms.jsp">Preview films</a></li>
    <li class="navbar"><a href="filmCreation.html">Insert film</a></li>
    <li class="navbar"><a href="schedulePresentations.jsp">Schedule film</a></li>
    <li class="navbar"><a href="editPresentation.jsp">Edit Scheduled film</a></li>
    <li class="navbar"><a class="active" href="updateUserData.jsp">Update user data</a></li>
    <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
</ul>
<% } %>
<div class="divBody">
    <h1>Update user data</h1>
    <!--Form for choosing the type of update user(username/name or password)!-->
    <form method="get" action="updateUserData">
        <div>
            <label for="typeUpdateId">Update the:</label>
            <select id="typeUpdateId" name="typeUpdate">
                <option value="nameUsername">name & username</option>
                <option value="password">password</option>
            </select>
        </div>
        <input type="submit">
    </form>
    <div>
        <!--form for changing username/name or password!-->
        <form method="post" action="updateUserData">
            <h3>${updating}</h3>
            <div>${updateInput}

            </div>

        </form>
    </div>
    <p>${messageResult}</p>
</div>
</body>
</html>
