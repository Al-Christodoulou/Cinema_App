package com.ergasia3;

import com.ergasia3.utils.DbUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

@WebServlet("/makeReservationServlet")
public class MakeReservationServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //get data to make the reservation
        String movie = request.getParameter("reserve");
        int seats = Integer.parseInt(request.getParameter("seats"));
        try {
            Connection connection = DbUtil.getConnection();
            Statement state = connection.createStatement();
            ResultSet result = state.executeQuery("SELECT movies_ID, movies_title, cinemas_ID, number_of_reservations, total_seats FROM cinema.presentations WHERE movies_title = '" + movie + "'"); //get the data for the corresponding movie
            if (result.next()) {
                state = connection.createStatement();
                ResultSet resultSet = state.executeQuery("SELECT seats FROM cinema.cinemas WHERE ID = '" + result.getString("cinemas_ID") + "'"); //get the seats for the corresponding cinema
                if (resultSet.next() && seats <= resultSet.getInt("seats")) { //if we get all the matches make the reservation and update the seats and reservation counters
                    HttpSession session = request.getSession(false);
                    String uname = (String) session.getAttribute("uname");
                    state = connection.createStatement();
                    ResultSet res = state.executeQuery("SELECT ID FROM cinema.customers WHERE user_username = '" + uname + "'"); //get the id for the corresponding user/customer
                    res.next();
                    state = connection.createStatement();
                    state.executeUpdate("INSERT INTO cinema.reservations (presentations_movies_ID, presentations_movies_title, presentations_cinemas_ID, customers_ID, number_of_seats) VALUES ('" + result.getString("movies_ID") + "', '" + movie + "', '" + result.getString("cinemas_ID") + "', '" + res.getString("ID") + "', '" + seats + "');");
                    state = connection.createStatement();
                    state.executeUpdate("UPDATE cinema.presentations SET number_of_reservations = number_of_reservations + 1, total_seats = total_seats + '" + seats + "' WHERE movies_title = '" + movie + "'");

                    if (result.getInt("total_seats") + seats == resultSet.getInt("seats")) { state.executeUpdate("UPDATE cinema.presentations SET is_available = 0 WHERE movies_title = '" + movie + "'"); }

                    state = connection.createStatement();
                    state.executeUpdate("UPDATE cinema.customers SET reservations_made = reservations_made + 1 WHERE ID = '"+res.getString("ID")+"'");
                    response.getWriter().println("<html><body><h2>The reservation was successful</h2><br><a href=\"CustomerMenu.html\">Main page</a></body></html>");
                    res.close();
                } else { response.getWriter().println("<html><body><h2>Sorry! There are not any available seats right now!</h2><a href=\"makeReservation.jsp\">Go Back</a></body></html>"); }
                resultSet.close();
            }
            result.close();
            state.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<html><body><h2>There has been an error trying to connect to the database!</h2><a href=\"viewReservation.jsp\">View</a></body></html>");
        }
    }
}