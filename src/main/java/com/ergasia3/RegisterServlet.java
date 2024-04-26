package com.ergasia3;

import com.ergasia3.utils.DbUtil;
import com.ergasia3.utils.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private void redirectToSelfWithError(HttpServletResponse resp, String error) { //used for handling/printing errors
		final PrintWriter pw;
		try {
			pw = resp.getWriter();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		resp.setContentType("text/html");
		pw.println("<!DOCTYPE HTML>");
		pw.println("<link rel=\"stylesheet\" href=\"./css.css\">");
		pw.println("<html>");
		pw.println("<h2 style=\"font-size: 30px; color: red; text-align: center\">" + error + "</h2>");
		pw.println("</html>");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//selects all the data from the sign up form
		PrintWriter pw = resp.getWriter();
		String name = req.getParameter("name");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String confirmPassword = req.getParameter("confirmPassword");
		String userType = req.getParameter("userType");

		if(userType.equals("ContentAdmin")) userType = "Content Admin";
		if(password.equals(confirmPassword))//successful matching of the password
		{
			try {
				//inserting all the data to the table users and customers or content_admins
				final Connection connection = DbUtil.getConnection();
				final String sql1 = "INSERT INTO user(`name`,`username`, `password`, `salt`, `role`)" +
						"VALUES (?, ?, ?, ?, ?);";

				final String randomSalt = PasswordUtils.createRandomSalt();
				final String hashedPassword = PasswordUtils.getSaltedHash(password, randomSalt);

				final PreparedStatement preparedStmt2 = connection.prepareStatement(sql1);
				preparedStmt2.setString(1, name);
				preparedStmt2.setString(2, username);
				preparedStmt2.setString(3, hashedPassword);
				preparedStmt2.setString(4, randomSalt);
				preparedStmt2.setString(5, userType);
				try {
					preparedStmt2.executeUpdate();
				}
				catch (SQLIntegrityConstraintViolationException e) {
					redirectToSelfWithError(resp, "A user with the same name already exists!");
					return;
				}

				if(userType.equals("Customer"))//insert data to the table customers
				{
					final String sql2 = "INSERT INTO customers(`user_name` ,`user_username`, `reservations_made`)" +
							"VALUES (?, ?, ?);";
					PreparedStatement prepared = connection.prepareStatement(sql2);
					prepared.setString(1, name);
					prepared.setString(2, username);
					prepared.setInt(3, 0);
					prepared.execute();
				}
				else if(userType.equals("Content Admin"))//insert data to the table content_admins
				{
					final String sql2 = "INSERT INTO content_admins(`user_name` ,`user_username`)" +
							"VALUES (?, ?);";
					PreparedStatement prepared = connection.prepareStatement(sql2);
					prepared.setString(1, name);
					prepared.setString(2, username);
					prepared.execute();
				}
				resp.setContentType("text/html");
				pw.println("<!DOCTYPE HTML>");
				pw.println("<link rel=\"stylesheet\" href=\"./css.css\">");
				pw.println("<html>");
				pw.println("<h2>Hello " + username + " the type of "+ userType +"!</h2>");
				pw.println("</html>");
			}
			catch (SQLException e) {
				redirectToSelfWithError(resp, "There was an error trying to connect to the database!");
			}
			catch (Exception e) {
				redirectToSelfWithError(resp, e.getMessage());
			}

		}
		else //not matching the password with confirm password
			redirectToSelfWithError(resp, "Wrong password confirmation!");
	}
}

