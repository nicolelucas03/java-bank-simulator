package bankapp;

import java.util.Scanner;
import bankapp.AccountActivity;


import exceptions.InsufficientFundsException;
import exceptions.InvalidMenuOptionException;

public class BankAccountMenu {
	private static final int CHECK_BALANCE = 1;
	private static final int DEPOSIT = 2;
	private static final int WITHDRAW = 3;
	private static final int FREEZE_ACCOUNT = 4;
	private static final int UNFREEZE_ACCOUNT = 5;

	private BankAccount account;
	private AccountActivity activity; 
	private Scanner keyboardInput;


	public BankAccountMenu(BankAccount account, AccountActivity activity) {
		this.account = account;
		this.activity = activity;
		this.keyboardInput = new Scanner(System.in);
	}

	public void manageAccount() {
		boolean success = false;
		while (!success) {
			try {
				displayOptions();
				int optionInput = getUserOptionInput();
				processUserOptionInput(optionInput);
				success = true;
			} catch (InvalidMenuOptionException e) {
				System.out.println("Error: " + e.getMessage() + " Try again.");
			}
		}
	}

	public void displayOptions() {
		System.out.println("Menu Options:");
		System.out.println("1. Check Balance");
		System.out.println("2. Deposit");
		System.out.println("3. Withdraw");
		System.out.println("4. Freeze Account");
		System.out.println("5. Unfreeze Account");
		System.out.println("Enter your choice (1-5):");
	}

	public int getUserOptionInput() {
		return keyboardInput.nextInt();
	}

	public double getUserAmountInput(String prompt) {
		System.out.println(prompt);
		return keyboardInput.nextDouble();
	}

	public void processUserOptionInput(int option) {
		boolean success = false;
		while (!success) {
			try {
				switch (option) {
				case CHECK_BALANCE:
					processCheckBalance();
					break;
				case DEPOSIT:
					double depositAmount = getUserAmountInput("Enter deposit amount:");
					processDeposit(depositAmount);
					break;
				case WITHDRAW:
					double withdrawalAmount = getUserAmountInput("Enter withdrawal amount:");
					processWithdrawal(withdrawalAmount);
					break;
				case FREEZE_ACCOUNT:
					processFreezeAccount();
					break;
				case UNFREEZE_ACCOUNT:
					processUnfreezeAccount();
					break;
				default:
					throw new InvalidMenuOptionException("Must enter a valid menu option.");
				}
				success = true;
			} catch (IllegalArgumentException e) {
				System.out.println("Error: " + e.getMessage() + " Try again.");
			} catch (InsufficientFundsException e) {
				System.out.println("Error: " + e.getMessage() + " Try again.");
			}
		}
	}

	public void processDeposit(double amount) {
		activity.deposit(account.getAccountId(), amount);
	}

	public void processWithdrawal(double amount) {
		activity.withdraw(account.getAccountId(), amount);
	}

	public void processCheckBalance() {
		System.out.println("Current Balance: " + account.getCurrentBalance());
	}

	public void processFreezeAccount() {
		activity.freezeAccount(account.getAccountId());
	}

	public void processUnfreezeAccount() {
		activity.unfreezeAccount(account.getAccountId());
	}

	public BankAccount getAccount() {
		return account;
	}
}