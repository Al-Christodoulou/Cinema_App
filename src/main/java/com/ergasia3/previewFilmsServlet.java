package com.ergasia3;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/previewFilms")
public class previewFilmsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
        Date start_date;
        Date end_date;
        try { //parse user's input as dates
            start_date = sdfo.parse(req.getParameter("scheduleDate"));
            end_date = sdfo.parse(req.getParameter("scheduleDate2"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if(start_date.compareTo(end_date) < 0) { //data check in case the dates are opposite
            req.setAttribute("start_date",start_date);
            req.setAttribute("end_date",end_date);
            RequestDispatcher dispatcher = req.getRequestDispatcher("previewFilms.jsp?action=view");
            dispatcher.forward(req, resp);
        } else {
            resp.getWriter().println("<html><body><h2>The dates are not valid!</h2><br><h3>(Start date should be before end date)</h3><br><a href=\"viewReservation.jsp\">Go Back</a></body></html>");
        }
    }
}
