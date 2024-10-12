package Mzanzi.Bank.System;

import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bouncycastle.crypto.generators.BCrypt;

public class Mzansi_Bank_System {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/mzanzibanksystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "HewSawchemi90@"; 

    private static final UserManager userManager = new UserManager();
    private static final Map<String, User> loggedInUsers = new HashMap<>(); // ConcurrentHashMap for thread safety
    private static ArrayList<Account> accounts = new ArrayList<>(); // Generic ArrayList for accounts (for temporary storage)

    
    public static void main(String[] args) {
        // Connect to database (replace placeholders with actual values)
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to database successfully!");

            // Sample menu (replace with user interface)
            int choice; // Declare choice outside the loop

            do {
              choice = displayMainMenu(); // Get user choice

              switch (choice) {
              case 1:
                  registerUser();
                  break;
              case 2:
                  loginUser();
                  break;
              default:
                  System.out.println("Invalid choice."); // Handle invalid menu options
              }
              
            } while (choice != 3); // Loop until user chooses to exit

            System.out.println("Exiting to registered User option");
            
            
        } 
        catch (SQLException e) {
        	
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    private static int displayMainMenu() {
        System.out.println("\n****** Welcome to Mzansi Bank ******");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice;
        do {
            try {
                choice = new Scanner(System.in).nextInt(); // Use Scanner for user input
                // Exit the loop if valid input is received
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1, 2, or 3).");
                
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            }
        } while (true); // Loop until valid input is entered

        return choice;
    }


    private static void registerUser() {
    	Scanner scanner = new Scanner(System.in); // Clear potential buffer issue
        scanner.nextLine();
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        if (username == null || username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        UserRepository userRepository = new UserRepository();
        userRepository.register(username, password);
    }

    private static void loginUser() {
    	Scanner scanner = new Scanner(System.in); // Clear potential buffer issue
        scanner.nextLine();
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = userManager.login(username, password);
            User loggedInUser = Authenticator.authenticate(user);
            BCrypt BCrypt = new BCrypt();
            if (loggedInUser != null) {
            	System.out.println("Login successful!");
                displayLoggedInMenu(user);
                 // Pass the authenticated user object
            } else {   
                
            	System.out.println(" Login failed, please try again");
            }
            
            //displayLoggedInMenu(user);
            
            loggedInUsers.remove(username); // Remove user from map after logout
        } catch (InvalidCredentialsException e) {
            System.out.println(e.getMessage());
        }
    }

    public class Account {
        private double balance;

        public Account(double balance) {
            this.balance = balance;
        }

        public double getBalance() {
            return balance;
        }

		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getAccountType() {
			// TODO Auto-generated method stub
			return null;
		}

		public int getId() {
			// TODO Auto-generated method stub
			return 0;
		}

		public void setOwner(Account currentAccount) {
			// TODO Auto-generated method stub
			
		}

        // Other methods for the Account class can go here
    }

    private static void displayLoggedInMenu(User user) throws InsufficientFundsException, SQLException, InvalidTransactionException {
    	Account currentAccount = new Account(0);
    	  currentAccount = getAccountByOwner(currentAccount); // Retrieve user's account (implementation omitted for brevity)

         System.out.println("Logged in as: " + currentAccount.getName());
         System.out.println("Account Type: " + currentAccount.getAccountType());
        int choice = 0;
        while (choice != 5) {
            System.out.println("\nWelcome, " + user.getUsername() + "!");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            Scanner scanner = null;
			choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Your balance: " + user.getAccount().checkBalance());
                    break;
                case 2:
                    deposit(user);
                    break;
                case 3:
                    withdraw(user);
                    break;
                case 4:
                    transferFunds(user, scanner);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
	// Method to retrieve user from database based on username and password (secure validation omitted)
    private static User getUserFromDatabase(String username) throws SQLException {
        return DatabaseManager.getUserFromDatabase(username);
    }
    // Method to retrieve account by owner (database interaction)
    private static Account getAccountByOwner(Account currentAccount) {//
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Accounts WHERE user_id = ?");
            statement.setInt(1, currentAccount.getId());
            ResultSet resultSet = statement.executeQuery();
            

            Account account = null;
            if (resultSet.next()) {
                int accountNumber = resultSet.getInt("account_number");
                double balance = resultSet.getDouble("balance");
                String accountType = resultSet.getString("account_type");

                if (accountType.equals("Savings Account")) {
                    account = new SavingsAccount(accountNumber, balance);
                } else if (accountType.equals("Checking Account")) {
                    account = new CheckingAccount(accountNumber, balance);
                } else {
                    // Handle unexpected account type 
                }
                account.setOwner(currentAccount); // Set account owner
            }
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors 
            return null;
        }
    }

    private static void deposit(User user) throws SQLException {
        System.out.print("Enter deposit amount: ");
        double amount = InputUtils.readDouble();

        BankAccount account = user.getAccount();
		account.deposit(amount);
		// Update database
		System.out.println("Deposit successful!");
		updateUserAccountBalance(user.getUsername(), amount);
    }

    private static void withdraw(User user) throws InsufficientFundsException, SQLException, InvalidTransactionException {
        System.out.print("Enter withdrawal amount: ");
        double amount = InputUtils.readDouble();

                     withdraw(amount);
		try {
			updateUserAccountBalance(user.getUsername(), -amount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Update database with negative for withdrawal
		System.out.println("Withdrawal successful!");
    }

    private static void withdraw(double amount) {
		// TODO Auto-generated method stub
		
	}

    
    private static void transferFunds(User user, Object amount) {
        boolean transferConfirmed = false;

        while (!transferConfirmed) {
            // Input validation loop for recipient username and amount
            while (true) {
                System.out.print("Enter recipient username: ");
                Scanner scanner = new Scanner(System.in);
                String recipientUsername = scanner.next();
                try {
                    User recipient = userManager.getUserByUsername(recipientUsername);
                    if (recipient != null) {
                        break;  // Valid recipient found
                    } else {
                        System.out.println("Recipient not found. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Error retrieving recipient: " + e.getMessage());
                }
            }

            while (true) {
                System.out.print("Enter transfer amount: ");
                try {
                    double amount1 = InputUtils.readDouble();
                    if (amount1 > 0) {
                        break;  // Valid amount entered
                    } else {
                        System.out.println("Invalid amount. Please enter a positive value.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a numerical amount.");
                }
            }

            // Transfer confirmation loop
            while (true) {
                System.out.print("Confirm transfer (y/n): ");
                Scanner scanner = new Scanner(System.in);
                String confirmTransfer = scanner.nextLine().toLowerCase();
                if ("y".equalsIgnoreCase(confirmTransfer)) {
                    transferConfirmed = true;
                    break;
                } else if ("n".equalsIgnoreCase(confirmTransfer)) {
                    System.out.println("Transfer cancelled.");
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'y' to confirm or 'n' to cancel.");
                }
            }

            if (transferConfirmed) { //Java Tutorial10:Create a simple banking system(Youtube)
                try {
                    User recipient;
					user.getAccount().transferFunds(recipient.getAccount(), amount);
                    String recipientUsername;
					updateUserBalancesInDatabase(user.getUsername(), recipientUsername, -amount, amount);
                    System.out.println("Transfer successful!");
                } catch (InsufficientFundsException | InvalidTransactionException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static void updateUserBalancesInDatabase(String senderUsername, String recipientUsername, double senderAmount, double recipientAmount) {
        // Encapsulate database update logic here
    }


    private static void updateUserAccountBalance(String username, double amount) throws SQLException {
        String sql = "UPDATE Accounts SET balance = balance + ? WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, amount);
            statement.setString(2, username);
            statement.executeUpdate();
        }
    }
}

class UserManager {

    public boolean createUser(String username, String hashedPassword) {
		return false;
        // Logic to create user in the database and return success/failure
    }

    public User login(String username, String password) throws InvalidCredentialsException {
		return null;
        // Logic to validate credentials against user data in the database
        // Throw InvalidCredentialsException if username or password is incorrect
    }

    public User getUserByUsername(String username) {
		return null;
        // Logic to retrieve user information from the database based on username
    }
}

class User {
    private String username;
    private String hashedPassword;
    private Account account;
	public BankAccount getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return (Integer) null;
	}

    // Getters, setters, and methods for password verification and account association
}

class PasswordUtil {
    public static String hashPassword(String password) {
		return password;
        // Implement secure password hashing using a reputable algorithm (e.g., bcrypt)
    }
}

class InputUtils {
    public static String readString() {
		return null;
        // Implement method to read user input as string using Scanner or other methods
    }

    public static int readInt() {
		return 0;
        // Implement method to read user input as integer using Scanner or other methods
    }

    public static double readDouble() {
		return 0;
        // Implement method to read user input as double using Scanner or other methods
    }
}

class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message, SQLException e) {
        super(message);
    }

	public InvalidCredentialsException(String string) {
		// TODO Auto-generated constructor stub
	}
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidTransactionException extends Exception {
    public InvalidTransactionException(String message) {
        super(message);
    }
}


