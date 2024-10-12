package Mzanzi.Bank.System;


public class user { // ITJVA3-B12 Class Link Group 1;March 26, 2024


    private int id; // Database ID for the user
    private String name;
    private String username;
    private String hashedPassword;
	// Getters and setters for user attributes
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

	public String checkBalance() {
		// TODO Auto-generated method stub
		return null;
	}

	public static <CheckingAccount> CheckingAccount getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

public void setAccount(BankAccount account) {
}

}






