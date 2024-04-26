package com.ergasia3;

import com.ergasia3.utils.DateUtils;
import com.ergasia3.utils.DbUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Date;

@WebServlet("/SchedulePresentationServlet")
public class SchedulePresentationServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String filmTitle = req.getParameter("films");
		final int cinemaRoomID = Integer.parseInt(req.getParameter("cinemaRooms"));
		final String scheduleDateStr = req.getParameter("scheduleDate");
		final String scheduledTimeStr = req.getParameter("scheduleTime");

		final Date scheduledDate = DateUtils.convToDateFromHTMLParam(scheduleDateStr);
		final Date scheduledTime = DateUtils.convToTimeFromHTMLParam(scheduledTimeStr);

		final Date actualScheduledTime = DateUtils.combineDateTime(scheduledDate, scheduledTime);

		try {
			DbUtil.addPresentation(
					filmTitle,
					cinemaRoomID,
					actualScheduledTime,
					true
			);
			resp.getWriter().println("<html><body><h2>The Presentation has been made!</h2></body><a href=\"schedulePresentations.jsp\">Go back</a></html>");
		}
		catch (InvalidParameterException e) {
			e.printStackTrace();
			req.setAttribute("errorMsg", e.getMessage());
			final RequestDispatcher dispatcher = req.getRequestDispatcher("schedulePresentations.jsp");
			dispatcher.forward(req, resp);
		}
		req.setAttribute("errorMsg", "");
	}
}
