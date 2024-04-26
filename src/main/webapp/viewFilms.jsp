<%@ page import="com.ergasia3.cinemaclasses.Film" %>
<%@ page import="com.ergasia3.utils.DbUtil" %><%--
  Created by IntelliJ IDEA.
  User: Alex Chr
  Date: 07-May-23
  Time: 10:01 PM
  To change this template use File | Settings | File Templates.
--%>
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
    <li class="navbar"><a href="contentAdminMenu.html">Main page</a></li>
    <li class="navbar"><a class="active" href="viewFilms.jsp">Preview films</a></li>
    <li class="navbar"><a href="filmCreation.html">Insert film</a></li>
    <li class="navbar"><a href="schedulePresentations.jsp">Schedule film</a></li>
    <li class="navbar"><a href="editPresentation.jsp">Edit Scheduled film</a></li>
    <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
    <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
</ul>
<div class="divBody">
    <table>
        <tr>
            <th>Film ID</th>
            <th>Film Title</th>
            <th>Film Category</th>
            <th>Film Description</th>
        </tr>
        <%
            for (Film film : DbUtil.getAvailableFilms()) { %>
          <tr>
             <td> <%= film.getId() %> </td>
              <td> <%= film.getTitle() %> </td>
              <td> <%= film.getCategory() %> </td>
              <td> <%= film.getDescription() %> </td>
          </tr>
          <% }
        %>
    </table>
</div>

</body>
</html>
