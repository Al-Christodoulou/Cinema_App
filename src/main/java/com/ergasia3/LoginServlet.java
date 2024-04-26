package com.ergasia3;

import com.ergasia3.utils.DbUtil;
import com.ergasia3.utils.PasswordUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	public static HttpSession session;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//get credentials from login.html
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		try {
			final Connection connection = DbUtil.getConnection();
			final String userSalt = PasswordUtils.getUserSaltFromDB(connection, username);
			final String hashedPassword = PasswordUtils.getSaltedHash(password, userSalt);
			//get all users with matching credentials (should only be one)
			final PreparedStatement pstatement = connection.prepareStatement("SELECT * FROM user WHERE username=? AND password=?");
			pstatement.setString(1, username);
			pstatement.setString(2, hashedPassword);
			final ResultSet resultSet = pstatement.executeQuery();
			if(resultSet.next()) {
				//open session,get some columns for later use and proceed to the next menu
				if(Objects.equals(resultSet.getString("role"), "Customer")) {
					session = req.getSession();
					session.setAttribute("uname",username);
					session.setMaxInactiveInterval(60*30);
					session.setAttribute("role","Customer");
					resp.sendRedirect("CustomerMenu.html");
				} else if(Objects.equals(resultSet.getString("role"), "Content Admin")) {
					session = req.getSession();
					session.setMaxInactiveInterval(60*30);
					session.setAttribute("uname",username);
					session.setAttribute("role","Content Admin");
					resp.sendRedirect("contentAdminMenu.html");
				} else if(Objects.equals(resultSet.getString("role"), "Admin")) {
					session = req.getSession();
					session.setMaxInactiveInterval(60*30);
					session.setAttribute("uname",username);
					session.setAttribute("role","Admin");
					resp.sendRedirect("AdminMenu.html");
				}
			} else { //wrong username/password
				resp.getWriter().println("<html><body><h2>Wrong Username and/or Password!</h2><a href=\"login.html\">Go Back</a>Go Back</body></html>");
			}
			pstatement.close();
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			resp.getWriter().println("<html><body><h2>There has been an error trying to connect to the database!</h2><a href=\"login.html\">Go Back</a></body></html>");
		}
	}
}
