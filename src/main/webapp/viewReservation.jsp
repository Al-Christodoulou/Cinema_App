<%@ page import="java.sql.*" %>
<%@ page import="com.ergasia3.utils.DbUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Reservation</title>
    <link rel="stylesheet" href="cssFiles/navBar.css">
    <link rel="stylesheet" href="cssFiles/css.css">
  <link rel="stylesheet" href="cssFiles/tables.css">
</head>
<body>
  <ul class="navbar">
    <li class="navbar"><a href="CustomerMenu.html">Main page</a></li>
    <li class="navbar"><a href="previewFilms.jsp">Show Available Films</a></li>
    <li class="navbar"><a href="makeReservation.jsp">Make Reservation</a></li>
    <li class="navbar"><a class="active" href="viewReservation.jsp">View Reservation</a></li>
    <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
    <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
  </ul>
  <div class="divBody">
  <%try {
    //print customer's reservations (if he made at least one)
    Connection connection = DbUtil.getConnection();
    String uname = (String)session.getAttribute("uname");
    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    ResultSet res = statement.executeQuery("SELECT ID FROM cinema.customers WHERE user_username = '"+uname+"'");
    res.next();
    ResultSet resultSet = statement.executeQuery("SELECT presentations_movies_ID, presentations_movies_title, presentations_cinemas_ID, number_of_seats FROM cinema.reservations WHERE customers_ID = '"+res.getString("ID")+"'"); //get reservations for the matching ID%>
    <% if (!resultSet.next()) { %> <h2>You have not made a reservation yet</h2> <% //check if we got any reservations
    } else { resultSet.beforeFirst(); //customer made at last one reservation %>
    <h1>Your reservations</h1>
  <table>
    <tr>
      <th>Film ID</th>
      <th>Film Title</th>
      <th>Cinema</th>
      <th>Number of Seats</th>
    </tr>
      <% while (resultSet.next()) { %>
    <tr>
      <th><%= resultSet.getString("presentations_movies_ID") %></th>
      <th><%= resultSet.getString("presentations_movies_title") %></th>
      <th><%= resultSet.getString("presentations_cinemas_ID") %></th>
      <th><%= resultSet.getString("number_of_seats") %></th>
    </tr>
    <% }}
    %> </table> <%
      resultSet.close();
    statement.close();
    res.close(); %><%
    } catch (SQLException e) { e.printStackTrace();
    %> <h2>There has been an error trying to connect to the database!</h2> <%
    } %>
  </div>
</body>
</html>
