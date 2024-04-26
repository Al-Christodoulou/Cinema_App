package com.ergasia3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//logout, end session and clear cache
        HttpSession session = request.getSession();
        session.invalidate();
        response.setHeader("Cache-Control"," no-cache, no-store, must-revalidate");
        response.setHeader("Pragma"," no-cache");
        response.setDateHeader("Expires",0);
        response.sendRedirect("login.html");
    }
}
