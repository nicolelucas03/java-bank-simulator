package bankapp;

import java.util.List;


import java.util.Scanner;

import exceptions.InsufficientFundsException;
import exceptions.InvalidMenuOptionException;

public class Menu {
	private List<BankAccount> accounts;
	private Customer customer; 
	private Scanner scanner; 
	private AccountActivity activity;

	public Menu(Customer customer) {
		this.accounts = customer.getAccounts();
		this.customer = customer;
		this.scanner = new Scanner(System.in);
		this.activity = customer.getActivity();
	}

	public BankAccount findAccount(String accountId) {
		for(int i = 0; i < accounts.size(); i++) {
			BankAccount account = accounts.get(i);
			if(account.getAccountId().compareTo(accountId) == 0) {
				return account;
			}
		}

		throw new IllegalArgumentException("Invalid account id.");
	}

	private String getAccountType() {
		System.out.println("Which type of account would you like to manage?");
		System.out.println("1: Checking");
		System.out.println("2: Savings");
		System.out.print("Enter your selection (1 or 2): ");

		int accountTypeSelection = scanner.nextInt();
		scanner.nextLine();

		if (accountTypeSelection == 1) {
			return "checking";
		} else if (accountTypeSelection == 2) {
			return "savings";
		} else {
			throw new IllegalArgumentException("Invalid account type.");
		}
	}

	private void displayAccounts(String accountType) {
		System.out.println("Your " + accountType + " accounts:");
		for (int i = 0; i < accounts.size(); i++) {
			BankAccount account = accounts.get(i);
			if(account.getType().equals(accountType)) {
				System.out.println(accounts.get(i).toString());	
			}
		}
	} 

	public BankAccount openAccount() {
		String type = getAccountType();

		System.out.println("Enter a unique id for this account.");
		String accountId = scanner.nextLine();

		checkForUniqueId(accountId);

		System.out.println("How much would you like to deposit initially?");
		int initialDeposit = scanner.nextInt();
		scanner.nextLine();

		BankAccount newAccount = new BankAccount(initialDeposit, type, accountId);

		accounts.add(newAccount);
		activity.addAccount(newAccount);

		return newAccount;
	}

	public void checkForUniqueId(String id) {
		for(int i = 0; i < accounts.size(); ++i) {
			BankAccount account = accounts.get(i);

			if(id.compareTo(account.getAccountId()) == 0) {
				throw new IllegalArgumentException("Account ID must be unique.");
			}
		}
	}

	public BankAccount selectAccount() {
		String type = getAccountType();
		displayAccounts(type);

		System.out.println("Enter the account id.");
		String closeAccountId = scanner.nextLine();
		return findAccount(closeAccountId);
	}

	public boolean closeAccount() {
		if(accounts.size() > 0){
			BankAccount account = selectAccount();

			if (account == null) {
				System.out.println("Invalid account.");
				return false;
			}

			if (!accounts.contains(account)) {
				System.out.println("Account not found.");
				return false;
			}

			if (account.getCurrentBalance() != 0) {
				System.out.println("Cannot close account. Please withdraw all funds first.");
				return false;
			}

			return accounts.remove(account);
		}
		else{
			throw new IllegalArgumentException("No accounts to manage.");
		}
	}

	public void transfer() throws InsufficientFundsException {
		if(accounts.size() > 0){
			System.out.println("Enter the id of the account to transfer money from:");
			String transferFromId = scanner.nextLine();
			BankAccount transferFromAccount = findAccount(transferFromId);

			System.out.println("Enter the id of the account to transfer money to:");
			String transferToId = scanner.nextLine();
			BankAccount transferToAccount = findAccount(transferToId);

			System.out.println("How much would you like to transfer?");
			int transferAmount = scanner.nextInt();
			scanner.nextLine();

			System.out.println("\nInitiating transfer of " + transferAmount + " from account " 
					+ transferFromAccount + " to account " + transferToAccount);

			 activity.transfer(
			            transferFromId.trim(),
			            transferToId.trim(),
			            transferAmount); 
		}
		else{
			throw new IllegalArgumentException("No accounts to manage.");
		}
	}

	public void manageAccount() {
		if(accounts.size() > 0) {
			BankAccount account = selectAccount();
			BankAccountMenu bankAccountMenu = new BankAccountMenu(account, activity);
			bankAccountMenu.manageAccount();
		}
		else {
			throw new IllegalArgumentException("No accounts to manage.");
		}
	}

	public void updateProfile() {
		System.out.println("Enter your occupation:");
		String occupation = scanner.nextLine();
		System.out.println("Enter your address:");
		String address = scanner.nextLine();

		CustomerProfile profile = new CustomerProfile(occupation, address);
		customer.setProfile(profile);
		System.out.println("Profile updated: " + profile);
	}

	public void displayMenuOptions() {
		System.out.println("What would you like to do?");
		System.out.println("1. Open an account");
		System.out.println("2. Close an account");
		System.out.println("3. Transfer between accounts");
		System.out.println("4. Choose an account to manage");
		System.out.println("5. Update profile");
		System.out.println("6. View (or filter) transaction history");
		System.out.println("7. Reset Password");
		System.out.println("8. Help");
		System.out.println("9. Log Out");

		System.out.println("Enter your selection (1-9):");
	}

	public void viewTransactionHistory() {
		System.out.println("Enter account ID to view transaction history:");
		String accountId = scanner.nextLine();

		try {
			BankAccount account = findAccount(accountId);
			System.out.println("Would you like to filter the history? (yes/no)");
			String response = scanner.nextLine();

			List<Transaction> history;

			if (response.equalsIgnoreCase("yes")) {
				System.out.println("Enter filter type (Deposit or Withdrawal):");
				String type = scanner.nextLine();
				history = account.getFilteredTransactions(type);
			} else {
				history = account.getTransactions();
			}

			if (history.isEmpty()) {
				System.out.println("No transactions found.");
			} else {
				System.out.println("Transaction history for account " + accountId + ":");
				for (Transaction t : history) {
					System.out.println(t);
				}
			}

		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void showHelpMenu(Scanner scanner) {
		boolean viewingHelp = true;

		while (viewingHelp) {
			System.out.println("\n--- HELP / FAQ ---");
			System.out.println("Q: How do I deposit money?");
			System.out.println("A: Choose option 4 from the menu to find accounts to manage. Find your account and enter the amount to deposit.");
			System.out.println();
			System.out.println("Q: What happens if I try to withdraw more than my balance?");
			System.out.println("A: The system will prevent overdrawing your account.");
			System.out.println();
			System.out.println("Q: What is considered a large deposit?");
			System.out.println("A: Any deposit over $1000 will prompt for confirmation.");
			System.out.println();
			System.out.println("Type 'back' to return to the main menu.");

			String input = scanner.nextLine().trim().toLowerCase();
			if (input.equals("back")) {
				viewingHelp = false;
			} else {
				System.out.println("Invalid input. Type 'back' to go back.");
			}
		}
	}

	public boolean getMenuOptionInput() {
		int menuOptionSelection = scanner.nextInt();
		scanner.nextLine();

		if (menuOptionSelection == 1) {
			openAccount();
		} else if (menuOptionSelection == 2) {
			closeAccount();
		} else if(menuOptionSelection == 3) {
			transfer();
		}
		else if(menuOptionSelection == 4) {
			manageAccount();
		}
		else if (menuOptionSelection == 5) {
			updateProfile();
		} 
		else if (menuOptionSelection == 6) {
			viewTransactionHistory();
		} else if (menuOptionSelection == 7) {
			LoginMenu loginMenu = new LoginMenu();
			loginMenu.resetPassword();
		}
		else if (menuOptionSelection == 8) { 
			showHelpMenu(scanner);
		} else if (menuOptionSelection == 9) {
			return true;
		}else {
			throw new InvalidMenuOptionException("Invalid menu option.");
		}

		return false;
	}

	public boolean run() {
		boolean success = false;

		while(!success) {
			try {
				displayMenuOptions();

				boolean exit = getMenuOptionInput();

				success = true;
				return exit;
			}
			catch(InvalidMenuOptionException e) {
				System.out.println("Error: " + e.getMessage() + " Try again.");
			}
			catch(IllegalArgumentException e) {
				System.out.println("Error: " + e.getMessage() + " Try again.");
			}
		}

		return false;
	}

}