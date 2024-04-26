package com.ergasia3.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class PasswordUtils {
    static public String createRandomSalt() {
        final int saltLength = 16;

        final char[] buffer = new char[saltLength];
        final Random random = new Random();
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (char)(random.nextInt(126 - 48) + 48);
        }
        return new String(buffer);
    }

    static private String hashPassword(String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes(), 0, password.length());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static public String getSaltedHash(String password, String salt) {
        return hashPassword(password + salt);
    }

    static public String getUserSaltFromDB(final Connection connection, final String username) {
        try {
            final String sql1 = "SELECT salt FROM user WHERE username=?";
            final PreparedStatement preparedStmt1 = connection.prepareStatement(sql1);
            preparedStmt1.setString(1, username);
            final ResultSet rs = preparedStmt1.executeQuery();
            if (rs.next())
                return rs.getString(1);
            else
                return "err_finding_user";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
