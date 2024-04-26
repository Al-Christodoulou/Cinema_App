<%@ page import="java.sql.*" %>
<%@ page import="com.ergasia3.utils.DbUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Make Reservation</title>
    <link rel="stylesheet" href="cssFiles/navBar.css">
    <link rel="stylesheet" href="cssFiles/css.css">
</head>
<body>
    <ul class="navbar">
        <li class="navbar"><a href="CustomerMenu.html">Main page</a></li>
        <li class="navbar"><a href="previewFilms.jsp">Show Available Films</a></li>
        <li class="navbar"><a class="active" href="makeReservation.jsp">Make Reservation</a></li>
        <li class="navbar"><a href="viewReservation.jsp">View Reservation</a></li>
        <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
        <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
    </ul>
    <div class="divBody">
        <form action="makeReservationServlet">
            <% try {
                Connection connection = DbUtil.getConnection();
            Statement state = connection.createStatement();
            String uname = (String)session.getAttribute("uname");
            //check if the user is allowed to make another reservation
            ResultSet result = state.executeQuery("SELECT `reservations_made` FROM `cinema`.`customers` WHERE `user_username` = '"+uname+"'");
            if(result.next() && result.getInt("reservations_made") == 10) { %> <h1>You've reached the limit for allowed reservations!</h1> <% }
            else {

            state = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet res = state.executeQuery("SELECT `movies_title`, `start_date` FROM `cinema`.`presentations` WHERE `is_available` = 1"); //get available presentations

            if (res.next()) { res.beforeFirst(); //available presentations found %>
                <h2>Select the movie and the number of seats</h2><br><%
                while (res.next()) {
                    %>
                    <input required type="radio" name="reserve" value="<%= res.getString("movies_title") %>">
                    <label><%= res.getString("movies_title") + " - " + res.getString("start_date") %></label><br>
                <% } %>
                <label for="seats">Select the Number of Seats (max 9):</label>
                <input required type="number" id="seats" name="seats" min="1" max="9">
                <input type="submit" name="Submit">
            <% } else { //available presentations not found %> <h3>Sorry! There isn't any available thing right now! Try again another time</h3> <% }
            res.close();
            state.close();
            result.close();
            } %><%
            } catch (SQLException e) {
                e.printStackTrace();
                %> <h2>There has been an error trying to connect to the database!<br></h2> <%
            } %><br>
        </form>
    </div>
</body>
</html>
