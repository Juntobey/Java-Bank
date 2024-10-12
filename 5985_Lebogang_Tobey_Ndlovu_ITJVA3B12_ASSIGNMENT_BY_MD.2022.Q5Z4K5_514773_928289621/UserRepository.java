package Mzanzi.Bank.System;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepository {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mzanzibanksystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "HewSawchemi90@";


    public void register(String username, String password) {
    	 if (username == null) {
    	        throw new IllegalArgumentException("Username cannot be null.");
    	    }
        String query = "INSERT INTO user (username, hashedPassword) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, PasswordUtil.hashPassword(password));
            statement.executeUpdate();

            System.out.println("Registration successful!");

        } catch (SQLException e) {
            System.out.println("Registration failed. Username might already exist.");
            e.printStackTrace();
        }
    }
}
