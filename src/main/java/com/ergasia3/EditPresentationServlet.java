package com.ergasia3;

import com.ergasia3.cinemaclasses.Presentation;
import com.ergasia3.utils.DateUtils;
import com.ergasia3.utils.DbUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;

@WebServlet("/EditPresentationServlet")
public class EditPresentationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("action").equals("selectPresentation")) {
            final int selectedRadio = Integer.parseInt(req.getParameter("selectedPresentationButton"));
            final ArrayList<Presentation> presentations = DbUtil.getPresentations();
            if (!presentations.isEmpty()) {
                final Presentation selectedPresentation = presentations.get(selectedRadio);
                // store the selected Presentation to the current session
                final HttpSession session = req.getSession();
                session.setAttribute("selectedPresentation", selectedPresentation);

                final RequestDispatcher dispatcher = req.getRequestDispatcher("editPresentation.jsp?action=editPresentation");
                dispatcher.forward(req, resp);
            }
        }
        else if (req.getParameter("action").equals("editPresentation")) {
            final String editedMovieTitle = req.getParameter("movieTitle");
            final int editedCinemaID = Integer.parseInt(req.getParameter("cinemaID"));
            final boolean editedIsAvailable = req.getParameter("isAvailable") != null;
            final String editedStartDate = req.getParameter("startDate");
            final String editedStartTime = req.getParameter("startTime");

            final Date convertedStartDate = DateUtils.convToDateFromHTMLParam(editedStartDate);
            final Date convertedStartTime = DateUtils.convToTimeFromHTMLParam(editedStartTime);
            final Date actualScheduledTime = DateUtils.combineDateTime(convertedStartDate, convertedStartTime);

            final HttpSession session = req.getSession();
            final Presentation selectedPresentation = (Presentation)session.getAttribute("selectedPresentation");

            try {
                DbUtil.editPresentation(selectedPresentation, editedMovieTitle, editedCinemaID, editedIsAvailable,
                        actualScheduledTime);
                resp.getWriter().println("<html><body><h2>The changes have been applied!</h2><a href=\"editPresentation.jsp\">Go Back</a></body></html>");
            }
            catch (InvalidParameterException e) {
                e.printStackTrace();
                req.setAttribute("errorMsg", e.getMessage());
                final RequestDispatcher dispatcher = req.getRequestDispatcher("editPresentation.jsp?action=editPresentation");
                dispatcher.forward(req, resp);
            }
        }
    }
}
