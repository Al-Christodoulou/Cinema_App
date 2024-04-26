<%@ page import="com.ergasia3.cinemaclasses.Film" %>
<%@ page import="com.ergasia3.cinemaclasses.Cinema" %>
<%@ page import="com.ergasia3.utils.DbUtil" %><%--
  Created by IntelliJ IDEA.
  User: Alex Chr
  Date: 07-May-23
  Time: 10:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Schedule Presentations</title>
    <link rel="stylesheet" href="cssFiles/css.css">
    <link rel="stylesheet" href="cssFiles/navBar.css">
</head>
<body>
<ul class="navbar">
    <li class="navbar"><a href="contentAdminMenu.html">Main page</a></li>
    <li class="navbar"><a href="viewFilms.jsp">Preview films</a></li>
    <li class="navbar"><a href="filmCreation.html">Insert film</a></li>
    <li class="navbar"><a class="active" href="schedulePresentations.jsp">Schedule film</a></li>
    <li class="navbar"><a href="editPresentation.jsp">Edit Scheduled film</a></li>
    <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
    <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
</ul>
<h2 style="font-size: 20px; color: red">${errorMsg}</h2>
<div class="divBody">
<form action="SchedulePresentationServlet">
    <div class="columns">
        <div>
            <h2>Films:</h2>
            <%
                for (Film film : DbUtil.getAvailableFilms()) {
            %>
            <input required type="radio" id="filmsID" name="films" value="<%= film.getTitle() %>">
            <label for="filmsID"><%= film.getTitle() %></label>
            <br>
            <% } %>
        </div>

        <div>
            <h2>Cinema Rooms:</h2>
            <%
                for (Cinema cinema : DbUtil.getCinemaRooms()) {
            %>
            <input required type="radio" id="cinemaRoomID" name="cinemaRooms" value="<%= cinema.getID() %>">
            <label for="cinemaRoomID"><%= cinema.toString() %></label>
            <br>
            <% } %>

            <br><br><br><br>
            <input type="submit" style="background: linear-gradient(30deg, red, mediumaquamarine);
                padding: 14px 40px; border-radius: 12px; font-size: 20px"
                   value="Schedule!">
        </div>

        <div>
            <h2>Schedule Date:</h2>
            <label>Select a date: <input required type="date" name="scheduleDate"></label>
            <br>

            <h2>Schedule Time:</h2>
            <label>Select a time: <input required type="time" name="scheduleTime"></label>
            <br>
        </div>

    </div>
</form>
</div>
</body>
</html>
