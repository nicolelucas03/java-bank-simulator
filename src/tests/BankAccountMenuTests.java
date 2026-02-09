package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import bankapp.AccountActivity;
import bankapp.BankAccount;
import bankapp.BankAccountMenu;
import exceptions.InvalidMenuOptionException;

public class BankAccountMenuTests {

	@Test
	public void testUserDeposit() {
		BankAccount account = new BankAccount(0, "checking", "acc1");
		AccountActivity activity = new AccountActivity();
		activity.addAccount(account);
		BankAccountMenu menu = new BankAccountMenu(account, activity);

		menu.processDeposit(20);

		assertEquals(20.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	public void testUserWithdrawal() {
		BankAccount account = new BankAccount(0, "checking", "acc2");
		AccountActivity activity = new AccountActivity();
		activity.addAccount(account);
		BankAccountMenu menu = new BankAccountMenu(account, activity);

		menu.processDeposit(50);
		menu.processWithdrawal(20);

		assertEquals(30.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	public void testUserCheckBalance() {
		BankAccount account = new BankAccount(50, "savings", "acc3");
		BankAccountMenu menu = new BankAccountMenu(account, new AccountActivity());

		PrintStream originalOutputStream = System.out;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));

		menu.processCheckBalance();

		System.setOut(originalOutputStream);

		assertEquals("Current Balance: 50.0", outputStream.toString().trim());
	}

	@Test
	void testProcessUserOptionInput_Deposit() {       
		BankAccount account = new BankAccount(0, "checking", "acc4");
		String input = "20\n";

		InputStream originalInputStream = System.in;
		InputStream testInputStream = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInputStream);

		// Mute output
		PrintStream originalOutputStream = System.out;
		System.setOut(new PrintStream(new ByteArrayOutputStream()));

		AccountActivity activity = new AccountActivity();
		activity.addAccount(account);
		BankAccountMenu menu = new BankAccountMenu(account, activity);		
		menu.processUserOptionInput(2);

		System.setIn(originalInputStream);
		System.setOut(originalOutputStream);

		assertEquals(20.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	void testProcessUserOptionInput_Withdrawal() {
		BankAccount account = new BankAccount(50, "checking", "acc5");
		String input = "20\n";

		InputStream originalInputStream = System.in;
		InputStream testInputStream = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInputStream);

		// Mute output
		PrintStream originalOutputStream = System.out;
		System.setOut(new PrintStream(new ByteArrayOutputStream()));

		AccountActivity activity = new AccountActivity();
		activity.addAccount(account);
		BankAccountMenu menu = new BankAccountMenu(account, activity);
		menu.processUserOptionInput(3);

		System.setIn(originalInputStream);
		System.setOut(originalOutputStream);

		assertEquals(30.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	public void testInvalidMenuOption() {
		BankAccount account = new BankAccount(100, "checking", "acc6");
		// Instantiate with AccountActivity
		BankAccountMenu menu = new BankAccountMenu(account, new AccountActivity());

		try {
			// Option 6 is invalid because valid options are 1-5.
			menu.processUserOptionInput(6);
			fail("Expected an InvalidMenuOptionException to be thrown.");
		} catch (InvalidMenuOptionException e) {
			assertTrue(e != null);
		}
	}

	@Test
	public void testLargeDepositConfirmed() {
		InputStream originalInput = System.in;
		try {
			String simulatedUserInput = "yes\n";
			ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
			System.setIn(testIn);

			BankAccount account = new BankAccount(100, "savings", "largeAccount1");
			account.deposit(1500);

			assertEquals(1600.0, account.getCurrentBalance(), 0.005);
		} finally {
			System.setIn(originalInput);
		}
	}

	@Test
	public void testLargeDepositCancel() {
		InputStream originalInput = System.in;
		try {
			String simulatedUserInput = "no\n";
			ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
			System.setIn(testIn);

			BankAccount account = new BankAccount(100, "savings", "largeAccount2");
			account.deposit(1500);

			assertEquals(100.0, account.getCurrentBalance(), 0.005);
		} finally {
			System.setIn(originalInput);
		}
	}

	@Test
	public void testLargeWithdrawalConfirm() {
		InputStream originalInput = System.in;
		try {
			String simulatedUserInput = "yes\n";
			ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
			System.setIn(testIn);

			BankAccount account = new BankAccount(2000, "savings", "largeAccount3");
			account.withdraw(1500);

			assertEquals(500.0, account.getCurrentBalance(), 0.005);
		} finally {     
			System.setIn(originalInput);
		}
	}

	@Test
	public void testLargeWithdrawalCancel() {
		InputStream originalInput = System.in;
		try {
			String simulatedUserInput = "no\n";
			ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
			System.setIn(testIn);

			BankAccount account = new BankAccount(2000, "savings", "largeAccount4");
			account.withdraw(1500);

			assertEquals(2000.0, account.getCurrentBalance(), 0.005);
		} finally {
			System.setIn(originalInput);
		}
	}

}
