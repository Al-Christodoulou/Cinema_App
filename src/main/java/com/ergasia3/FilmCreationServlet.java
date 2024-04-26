package com.ergasia3;

import com.ergasia3.utils.DbUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/FilmCreationServlet")
public class FilmCreationServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String filmTitle = req.getParameter("title");
		String filmCategory = req.getParameter("category");
		String filmDescription = req.getParameter("description");

		DbUtil.addFilm(filmTitle, filmCategory, filmDescription);

		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		pw.println("<!DOCTYPE HTML>");
		pw.println("<link rel=\"stylesheet\" href=\"./css.css\">");
		pw.println("<html>");
		pw.println("<h2>The film " + filmTitle + "has been created!</h2><br>");
		pw.println("<a href=\"filmCreation.html\">Go Back</a>");
		pw.println("</html>");
	}
}
