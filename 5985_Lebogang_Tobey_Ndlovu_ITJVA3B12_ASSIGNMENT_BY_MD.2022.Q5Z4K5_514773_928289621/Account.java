package Mzanzi.Bank.System;

public class Account {

    public Account(int accountNumber, double balance, User currentUser) {
        // Constructor implementation
    }

    public void deposit(double amount) {
        // Method implementation
    }

    public String getAccountType() {
        // Method implementation
        return null;
    }

    public static class Main {

        public static void main(String[] args) {
            // Create a user object
            User user = new User();

            // Create a bank account object with the user
            BankAccount bankAccount = new BankAccount();
            bankAccount.setOwner(user);

            // Create a new savings account for the user
            Account savingsAccount = bankAccount.new Account(1234, 5000, user) {
                @Override
                public String getAccountType() {
                    return "SAVINGS";
                }
            

            // Print the account number and balance of the savings account
            // Assuming BankAccount provides methods to access these properties
            System.out.println("Account number: " + savingsAccount.getAccountNumber());
            System.out.println("Balance: R" + savingsAccount.checkBalance());

            // Perform a deposit
            savingsAccount.deposit(500);
            System.out.println("New balance: R" + savingsAccount.checkBalance());
        
    }

    class SavingsAccount extends Account {
        private double interestRate;

        public SavingsAccount(int accountNumber, double balance, User currentUser) {
            super(accountNumber, balance, currentUser);
        }

        @Override
        public String getAccountType() {
            return "Savings Account";
        }

        @Override
        public void deposit(double amount) {
            super.deposit(amount);
            // Apply interest if applicable.
        }
    }

    class CheckingAccount extends Account {
        public CheckingAccount(int accountNumber, double balance, User currentUser) {
            super(accountNumber, balance, currentUser);
        }

        @Override
        public String getAccountType() {
            return "Checking Account";
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
        }
    }
}

