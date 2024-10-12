package Mzanzi.Bank.System;  //https://www.geeksforgeeks.org/java-database-connectivity-with-mysql/
import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
public class DatabaseManager {
	

	 private static final String DB_URL = "jdbc:mysql://localhost:3306/mzanzibanksystem";
	    private static final String DB_USER = "root";
	    private static final String DB_PASSWORD = "HewSawchemi90@";  

	    public static Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	    }

	    public static Account getAccountByOwner1(User currentUser) throws SQLException {
	        Connection connection = getConnection();
	        PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE userId = ?");
	        statement.setInt(1, currentUser.getId());
	        ResultSet resultSet = statement.executeQuery();

	        // Modify this to instantiate the appropriate account type based on the `accountType` column
	        Account account = null;
	        if (resultSet.next()) {
	            int accountNumber = resultSet.getInt("accountNumber");
	            double balance = resultSet.getDouble("balance");
	            String accountType = resultSet.getString("accountType");

	            if (accountType.equals("SAVINGS")) {
	                account = new Account(accountNumber, balance, currentUser);
	            } else if (accountType.equals("CHECKING")) {
	                account = new Account(accountNumber, balance, currentUser);
	            }
	        }

	        connection.close();
	        return account;
	    }

	    public static String getHashedPasswordFromDatabase(String username) throws SQLException {
	        Connection connection = getConnection();
	        PreparedStatement statement = connection.prepareStatement("SELECT hashedPassword FROM Users WHERE username = ?");
	        statement.setString(1, username);
	        ResultSet resultSet = statement.executeQuery();

	        String hashedPassword = null;
	        if (resultSet.next()) {
	            hashedPassword = resultSet.getString("hashedPassword");
	        }

	        connection.close();
	        return hashedPassword;
	    }

	    // Methods for account-related database interactions (replace with your implementation)
	    public static Account getAccountByOwner(User currentUser) throws SQLException {
			return null;
	        // ... (implementation to retrieve account from database based on user ID) ...
	    }

	    public static void createAccount(User user, String accountType, double initialDeposit) throws SQLException {
	        // ... (implementation to create a new account for the user) ...
	    }

	    public static void updateAccountBalance(Account account, double amount) throws SQLException {
	        // ... (implementation to update account balance in the database) ...
	    }

		public static String[] getAllowedPasswords(Object username) {
			// TODO Auto-generated method stub
			return null;
		}

		public static User getUserFromDatabase(String username) {
			// TODO Auto-generated method stub
			return null;
		}

		

}
