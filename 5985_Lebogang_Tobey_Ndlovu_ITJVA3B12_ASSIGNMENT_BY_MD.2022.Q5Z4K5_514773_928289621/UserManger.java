package Mzanzi.Bank.System;
import java.security.MessageDigest;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.bouncycastle.crypto.generators.BCrypt;


public class UserManger {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/mzanzibanksystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "HewSawchemi90@";

    public User getUserByUsername(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User login(String username, String password) throws InvalidCredentialsException {
        User user = getUserByUsername(username);

        if (user == null) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Check if the provided password matches the stored hash
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return user;
    }
    public static String generateHash(String plaintextPassword) {
        return BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
    }
}
