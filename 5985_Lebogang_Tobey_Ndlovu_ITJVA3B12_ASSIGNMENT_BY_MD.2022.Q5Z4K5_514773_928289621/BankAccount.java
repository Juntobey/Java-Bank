package Mzanzi.Bank.System;

public class BankAccount implements BankingOperations {

    public interface BankingOperations {
        double checkBalance();

        void deposit(double amount) throws InvalidTransactionException;

        void withdraw(double amount) throws InsufficientFundsException, InvalidTransactionException;

        void transferFunds(Account toAccount, double amount) throws InsufficientFundsException, InvalidTransactionException;
    }

    
    public static abstract class Account implements BankingOperations {
        protected int accountNumber;
        protected double balance;
        protected Mzanzi.Bank.System.User user;
		
        public Account(int accountNumber, double balance, Mzanzi.Bank.System.User user2) {
            this.accountNumber = accountNumber;
            this.balance = balance;
            this.user = user2;
        }

        public abstract String getAccountType(); // To differentiate account types

        @Override
        public double checkBalance() {
            return balance;
        }

        @Override
        public void deposit(double amount) throws InvalidTransactionException {
            if (amount <= 0) {
                throw new InvalidTransactionException("Deposit amount cannot be negative");
            }
            balance += amount;
        }

        @Override
        public void withdraw(double amount) throws InsufficientFundsException, InvalidTransactionException {
            if (amount <= 0) {
                throw new InvalidTransactionException("Withdrawal amount cannot be negative");
            }
            if (balance >= amount) {
                balance -= amount;
            } else {
                throw new InsufficientFundsException("Insufficient funds");
            }
        }

        public void transferFunds(Account toAccount, double amount) throws InsufficientFundsException, InvalidTransactionException {
            // Implement transfer logic with validations
            withdraw(amount); // Check for sufficient funds first
            toAccount.deposit(amount);
        }
    }

    public static double transactionFee = 1.50; // Default transaction fee
    public static double savingsInterestRate = 0.09; // Default savings interest rate

    public void setOwner(Mzanzi.Bank.System.User user) {
        // TODO Auto-generated method stub
    }

    public static class User {
        // User details
    }

    public static class InvalidTransactionException extends Exception {
        public InvalidTransactionException(String message) {
            super(message);
        }
    }

    public static class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

	public void transferFunds(BankAccount account, Object amount) {
		// TODO Auto-generated method stub
		
	}

	public String checkBalance() {
		// TODO Auto-generated method stub
		return null;
	}
}