package Mzanzi.Bank.System; //https://www.geeksforgeeks.org/exceptions-in-java/?ref=shm
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Authenticator {
	 private static final String DB_URL = "jdbc:mysql://localhost:3306/mzanzibanksystem";
	    private static final String DB_USER = "root";
	    private static final String DB_PASSWORD = "HewSawchemi90@"; 

	    public static User authenticate(User user) throws InvalidCredentialsException {
	        if (user == null) {
	            throw new InvalidCredentialsException("User object is null.");
	        }

	        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
	            ps.setString(1, user.getUsername());
	            ps.setString(2, user.getPassword());

	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                // Create a User object with data from the result set
	                User authenticatedUser = new User();  // Replace with logic to extract user data
	                return authenticatedUser;
	            } else {
	                throw new InvalidCredentialsException("Authentication failed");
	            }

	        } catch (SQLException e) {
	            throw new InvalidCredentialsException("Authentication failed.", e);
	        }
	    }


}
