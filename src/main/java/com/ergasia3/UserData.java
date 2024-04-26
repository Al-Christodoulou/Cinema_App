package com.ergasia3;

import com.ergasia3.utils.DbUtil;

import java.sql.*;

public class UserData {
	//selects all the users from the table users or the users, which are type of contentAdmin
	public static ResultSet selectAllUsers(Boolean all)
	{
		ResultSet resultSet;
		try {
			Connection connection = DbUtil.getConnection();

			Statement statement = connection.createStatement();
			String sql;
			if(all) //select all users (is used for the admin operations: view users, delete user)
				sql = "SELECT `username`, `role` FROM cinema.user";
			else //select the users which are contentAdmin(is used for register admin)
				sql = "SELECT `username`, `role` FROM cinema.user " +
					"WHERE `role`= 'Content admin'";

			resultSet = statement.executeQuery(sql);
			return resultSet;
		}
		catch (SQLException e) {e.printStackTrace();}
		return null;
	}

}
