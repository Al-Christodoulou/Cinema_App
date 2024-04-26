<%@ page import="com.ergasia3.cinemaclasses.Presentation" %>
<%@ page import="com.ergasia3.utils.DbUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.ergasia3.utils.DateUtils" %><%--
  Created by IntelliJ IDEA.
  User: Alex Chr
  Date: 28-May-23
  Time: 10:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Presentation</title>
    <link rel="stylesheet" href="cssFiles/css.css">
    <link rel="stylesheet" href="cssFiles/navBar.css">
</head>
<body>
<ul class="navbar">
    <li class="navbar"><a href="contentAdminMenu.html">Main page</a></li>
    <li class="navbar"><a href="viewFilms.jsp">Preview films</a></li>
    <li class="navbar"><a href="filmCreation.html">Insert film</a></li>
    <li class="navbar"><a href="schedulePresentations.jsp">Schedule film</a></li>
    <li class="navbar"><a class="active" href="editPresentation.jsp">Edit Scheduled film</a></li>
    <li class="navbar"><a href="updateUserData.jsp">Update user data</a></li>
    <li style="float: right" class="navbar"><form method="get" action="logout"><input type="submit" value="Log out"></form></li>
</ul>
<!--
    Two types of forms appear in this JSP file:
    1) Presentation selection
    2) Presentation modification
    Regardless of the type of form, there's a submit button at the end, which is
    why this entire page is wrapped in a <form>
-->
<div class="divBody">
<form action="EditPresentationServlet">

    <h2 style="font-size: 20px; color: red">${errorMsg}</h2>
    <% if (request.getParameter("action") == null || request.getParameter("action").equals("selectPresentation")) {
        ArrayList<Presentation> presentations = DbUtil.getPresentations();
        int index = 0;
        if (!presentations.isEmpty()) {
            for (Presentation presentation : presentations) {
    %>
    <input required type="radio" id="presentationID" name="selectedPresentationButton" value="<%= index %>">
    <label for="presentationID"><%= presentation.toString() %></label>
    <br>
    <%
            ++index;
        }
    %>
    <!-- This is required because the parameter gets lost when hitting the submit button -->
    <input type="hidden" name="action" value="selectPresentation">
    <%
    }
    else {
    %>
    <h1 style="color: red">There's no presentations to edit!</h1>
    <%
        }
    }
    else if ((request.getParameter("action").equals("editPresentation"))) {
        Presentation selectedPresentation = (Presentation)session.getAttribute("selectedPresentation");
        if (selectedPresentation == null) {
    %>
    <h1 style="color: red">Error selecting presentation!</h1>
    <a href="editPresentation.jsp?action=selectPresentation">Go back</a>
    <%
    } else {
    %>
    <h3>Film Title:
        <input required type="text" name="movieTitle" value="<%= selectedPresentation.getFilm().getTitle() %>">
    </h3>
    <h3>Cinema ID:
        <input required type="text" name="cinemaID" value="<%= selectedPresentation.getCinema().getID() %>">
    </h3>
    <h3>Is available:
        <% if (selectedPresentation.isAvailable()) { %>
        <input type="checkbox" name="isAvailable" checked>
        <% } else { %>
        <input type="checkbox" name="isAvailable">
        <% } %>
    </h3>
    <h3>Start Date: <input required type="date" name="startDate" value=
            "<%= DateUtils.convToHTMLDateFromDate(selectedPresentation.getStartDate()) %>">
    </h3>
    <h3>Start Time: <input required type="time" name="startTime" value=
            "<%= DateUtils.convToHTMLTimeFromDate(selectedPresentation.getStartDate()) %>">
    </h3>
    <input type="hidden" name="action" value="editPresentation">
    <%
            }
        }
    %>
    <br><br>
    <input type="submit" value="Submit!">
</form>
</div>
</body>
</html>
