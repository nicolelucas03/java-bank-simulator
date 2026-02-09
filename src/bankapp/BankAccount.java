package bankapp;

import java.util.Scanner;

import exceptions.InsufficientFundsException;

import java.io.Serializable;
import java.util.ArrayList; 
import java.util.List;

public class BankAccount implements Serializable{
	private static final long serialVersionUID = 1L;

	private double balance;
	private String type;
	private String accountId;
	private List<Transaction> transactionHistory;


	public BankAccount(double initialBalance, String type, String accountId) {
		if (initialBalance < 0) {
			throw new IllegalArgumentException("Initial balance cannot be negative.");
		}
		if(initialBalance > 5000){
			throw new IllegalArgumentException("Deposit would exceed checking account limit of $5000.");
		}
		this.balance = initialBalance;
		this.type = type;
		this.accountId = accountId;
		this.transactionHistory = new ArrayList<>(); 
	}

	public BankAccount() {
		this.balance = 0;
		this.transactionHistory = new ArrayList<>();
	}

	public void deposit(double amount) {
		if(amount <= 0) {
			throw new IllegalArgumentException("Must deposit a positive amount.");
		}
		if ("checking".equalsIgnoreCase(this.type) && (this.balance + amount > 5000)) {
			throw new IllegalArgumentException("Deposit would exceed checking account limit of $5000.");
		}
		if (amount > 1000) {
			Scanner userInputDeposit = new Scanner(System.in);
			System.out.println("Attention: You are about to deposit a large amount: $" + amount);
			System.out.print("Do you want to proceed? (yes/no): ");
			String response = userInputDeposit.nextLine();
			if (!response.equalsIgnoreCase("yes")) {
				System.out.println("Deposit canceled.");
				return;
			}
		}
		this.balance += amount;
		transactionHistory.add(new Transaction("Deposit", amount));

	}

	public void withdraw(double amount) {
		if(amount <= 0) {
			throw new IllegalArgumentException("Must withdraw a positive amount.");
		}
		if (amount > 1000) {
			Scanner userInputWithdraw = new Scanner(System.in);
			System.out.println("Attention: You are about to withdraw a large amount: $" + amount);
			System.out.print("Do you want to proceed? (yes/no): ");
			String response = userInputWithdraw.nextLine();
			if (!response.equalsIgnoreCase("yes")) {
				System.out.println("Withdrawal canceled.");
				return;
			}
		}
		if(balance < amount) {
			throw new InsufficientFundsException("Insufficient funds.");
		}

		this.balance -= amount;
		transactionHistory.add(new Transaction("withdrawal", amount));
	}

	public double getCurrentBalance() {
		return this.balance;
	}

	@Override
	public String toString() {
		return "BankAccount{accountId=" + accountId + "}" ;
	}

	public String getType() {
		return this.type;	
	}

	public String getAccountId() {
		return this.accountId;
	}

	//Note: for Transactions 
	public List <Transaction> getTransactions(){ 
		return transactionHistory; 
	}

	public List<Transaction> getFilteredTransactions(String type){
		List<Transaction> filtered = new ArrayList<>();
		for (Transaction t : transactionHistory) { 
			if(t.getType().equalsIgnoreCase(type)) { 
				filtered.add(t);
			}
		}
		return filtered; 
	}

}

