package bankapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import exceptions.InvalidMenuOptionException;

public class LoginMenu {
	private static final String LOGIN_FILE = "users.txt";

	private Scanner userInput;

	public LoginMenu() {
		this.userInput = new Scanner(System.in);
	}

	public void run() {
		boolean exit = false;

		while(!exit) {
			String name = getUser();

			if(name != null) {
				Customer customer = retrieveCustomerInfo(name);
				Menu menu = new Menu(customer);

				boolean logOut = false;
				while(!logOut) {
					logOut = menu.run();
					storeCustomerInfo(customer);
				}

				System.out.println(name + " was successfully logged out.");

				exit = false;
			}
			else {
				exit = true;
			}
		}

		System.out.println("You have exited the program. Have a good day!");
	}

	public void storeCustomerInfo(Customer customer) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("usersInfo/" + customer.getName() + ".ser"));
			oos.writeObject(customer);
			oos.close();
		}
		catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public Customer retrieveCustomerInfo(String name) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("usersInfo/" + name + ".ser"));
			Customer customer = (Customer) ois.readObject();
			System.out.println("Welcome back, " + customer.getName() + "!");
			ois.close();
			return customer;
		}
		catch(FileNotFoundException e) {
			return new Customer(name);
		}
		catch(Exception e) {
			System.out.println("Error: " + e.getMessage());			
		}

		return null;
	}

	public String getUser() {
		boolean success = false;

		while(!success) {
			try {
				displayOptions();

				int optionInput = getUserOptionInput();
				String username = processUserInput(optionInput);

				success = true;

				return username;

			}
			catch(IllegalArgumentException e) {
				System.out.println("Attempt failed: " + e.getMessage());
			}	
			catch(InvalidMenuOptionException e) {
				System.out.println("Attempt failed: " + e.getMessage());
			}
		}

		return null;
	}

	public void displayOptions() {
		System.out.println("Welcome!");
		System.out.println("1. Register");
		System.out.println("2. Log In");
		System.out.println("3. Exit");
		System.out.println("Choose an option (1-3): ");
	}

	public int getUserOptionInput() {
		int option = userInput.nextInt();

		userInput.nextLine();

		return option;
	}

	private String processUserInput(int option) {
		if (option == 1) {
			return register();
		} 
		else if(option == 2) {
			return logIn();
		}
		else if(option == 3) {
			return null;
		}
		else {
			throw new InvalidMenuOptionException("Must enter a valid menu option.");
		}
	}

	public String register() {
		System.out.println("Choose a username: ");
		String username = userInput.nextLine();

		if (userExists(username)) {
			throw new IllegalArgumentException("Username already exists.");
		}

		System.out.println("Choose a password: ");
		String password = userInput.nextLine();
 
		System.out.println("Set a security question (e.g pet's name/maiden name/hometown): ");
		String securityQuestion = userInput.nextLine();
		
		System.out.println("Answer to the security question: ");
		String securityAnswer = userInput.nextLine();

		System.out.println("You entered:");
		System.out.println("  Username: " + username);
		System.out.println("  Password: " + password);
		System.out.print("Does this look correct? (yes/no): ");
		String confirmLogIn = userInput.nextLine().trim();

		if (!confirmLogIn.equalsIgnoreCase("yes")) {
			System.out.println("Okay, let’s start over.\n");
			return register();            
		}
 

		try {
			FileWriter fileWriter = new FileWriter(LOGIN_FILE, true);
			fileWriter.write(username + "," + password + "," + securityQuestion + "," + securityAnswer + "\n");
			System.out.println("User registered successfully.");
			fileWriter.close();
			return username;
		}
		catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return null;
	}

	public String logIn() {
		System.out.println("Enter username: ");
		String username = userInput.nextLine();

		System.out.println("Enter password: ");
		String password = userInput.nextLine();

		if(checkCredentials(username, password)) {
			System.out.println("Log in successful!");
			return username;
		}
		else {
			throw new IllegalArgumentException("Invalid username or password.");
		}
	}

	public boolean userExists(String username) {
		try {
			Scanner fileReader = new Scanner(new File(LOGIN_FILE));

			while(fileReader.hasNextLine()) {
				String[] loginInfo = fileReader.nextLine().split(",");

				if (loginInfo.length > 0 && loginInfo[0].equals(username)) {
					return true;
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return false;
	}

	public boolean checkCredentials(String username, String password) {
		try {
			Scanner fileReader = new Scanner(new File(LOGIN_FILE));

			while(fileReader.hasNextLine()) {
				String[] loginInfo = fileReader.nextLine().split(",");

				if (loginInfo.length > 0 && loginInfo[0].equals(username) && loginInfo[1].equals(password)) {
					return true;
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return false;
	}

	public void resetPassword() {
		System.out.println("Enter your username: ");
		String username = userInput.nextLine();

		if (!userExists(username)) {
			System.out.println("User does not exist.");
			return;
		}

		File file = new File(LOGIN_FILE);
		StringBuilder updatedContent = new StringBuilder();
		boolean resetSuccess = false;

		try (Scanner fileReader = new Scanner(file)) {
			while (fileReader.hasNextLine()) {
				String line = fileReader.nextLine();
				String[] loginInfo = line.split(",");

				if (loginInfo[0].equals(username)) {
					String storedPassword = loginInfo[1];
					String question = loginInfo.length > 2 ? loginInfo[2] : null;
					String answer = loginInfo.length > 3 ? loginInfo[3] : null;

					if (question == null || answer == null) {
						System.out.println("Security question not set for this user.");
						updatedContent.append(line).append("\n");
						continue;
					}

					System.out.println("Security Question: " + question);
					System.out.print("Your Answer: ");
					String userAnswer = userInput.nextLine();

					if (!userAnswer.equalsIgnoreCase(answer)) {
						System.out.println("Incorrect answer. Password reset failed.");
						updatedContent.append(line).append("\n");
						continue;
					}

					System.out.print("Enter your new password: ");
					String newPassword = userInput.nextLine();

					if (newPassword.equals(storedPassword)) {
						System.out.println("New password cannot be the same as the old one.");
						updatedContent.append(line).append("\n");
						continue;
					}

					updatedContent.append(username).append(",").append(newPassword).append(",")
					.append(question).append(",").append(answer).append("\n");
					System.out.println("Password reset successful.");
					resetSuccess = true;
				} else {
					updatedContent.append(line).append("\n");
				}
			}

			if (resetSuccess) {
				FileWriter writer = new FileWriter(LOGIN_FILE);
				writer.write(updatedContent.toString());
				writer.close();
			}
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}