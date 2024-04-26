<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="com.ergasia3.utils.DbUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Film Preview</title>
    <link rel="stylesheet" href="cssFiles/css.css">
    <link rel="stylesheet" href="cssFiles/navBar.css">
    <link rel="stylesheet" href="cssFiles/tables.css">
</head>
<body>
    <ul class="navbar">
        <li class="navbar"><a href="CustomerMenu.html">Main page</a></li>
        <li class="navbar"><a class="active" href="previewFilms.jsp">Show Available Films</a></li>
        <li class="navbar"><a href="makeReservation.jsp">Make Reservation</a></li>
        <li class="navbar"><a href="viewReservation.jsp">View Reservation</a></li>
        <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
        <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
    </ul>
    <div class="divBody">
        <form action="previewFilms">
            <h2>Select Movies:</h2>
            <label>From: <input required type="date" name="scheduleDate"></label>
            <br>

            <label>To: <input required type="date" name="scheduleDate2"></label>
            <br>
            <input type="submit">
        </form>
    </div>
    <% if (request.getParameter("action") != null) {
        Date start_date = (Date) request.getAttribute("start_date"); //user's date range
        Date end_date = (Date) request.getAttribute("end_date");
        try {
            Connection connection = DbUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM cinema.movies"); //get all movies
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE); //type_scroll_sensitive so we can go backwards
        ResultSet res = statement.executeQuery("SELECT presentations.movies_ID, presentations.start_date FROM cinema.presentations WHERE is_available = 1"); //get all available presentations
        if (!res.next()) { %> <h2>There is nothing available right now!</h2> <% }
        else { res.beforeFirst();
    %><div class="divBody"><table>
        <% boolean start_printing = true;
        while (resultSet.next()) {
            while (res.next()) { //print movie data if the movie has an available presentation which is in the date range
                SimpleDateFormat sdfo = new SimpleDateFormat("dd-MM-yyyy");
                Date presentation_time = null;
                try {
                    presentation_time = sdfo.parse(res.getString("start_date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(Objects.equals(resultSet.getString("ID"), res.getString("movies_ID")) && presentation_time.compareTo(start_date) >= 0 && presentation_time.compareTo(end_date) <= 0) {
                    if(start_printing) { %>
                        <tr>
                            <th>Film ID</th>
                            <th>Film Title</th>
                            <th>Film Category</th>
                            <th>Film Description</th>
                            <th>Presentation Time:</th>
                        </tr> <% start_printing=false; } %>
                    <tr>
                        <td><%= resultSet.getString("ID") %></td>
                        <td><%= resultSet.getString("title") %></td>
                        <td><%= resultSet.getString("category") %></td>
                        <td><%= resultSet.getString("description") %></td>
                        <td><%= res.getString("start_date") %></td>
                    </tr>
        <%  res.last();}}
            res.beforeFirst(); }
            resultSet.close();
            res.close();
            statement.close();
        } %><%
    } catch (SQLException e) {
        e.printStackTrace();
    %> <h2>There has been an error trying to connect to the database!<br><a href="CustomerMenu.html">Go Back</a></h2> <%
        }} %></table></div>
</body>
</html>