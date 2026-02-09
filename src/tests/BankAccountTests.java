package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import bankapp.BankAccount;
import bankapp.Transaction;
import exceptions.InsufficientFundsException;

public class BankAccountTests {

	@Test
	public void testSimpleDeposit() {
		BankAccount account = new BankAccount();
		account.deposit(25);
		assertEquals(25.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	public void testNegativeDeposit() {
		BankAccount account = new BankAccount();

		try {
			account.deposit(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}

	@Test 
	public void testNegativeInitialBalance() { 

		try { 
			//public BankAccount(double initialBalance, String type, String accountId)
			BankAccount account = new BankAccount(-3, "checking", "checking1");
			fail(); 
		}
		catch(IllegalArgumentException e) { 
			assertTrue(e != null); 
		}
	}

	@Test
	public void testSimpleWithdrawal() {
		BankAccount account = new BankAccount();
		account.deposit(20);
		account.withdraw(5);
		assertEquals(15.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	public void testExactWithdrawal() {
		BankAccount account = new BankAccount();
		account.deposit(20);
		account.withdraw(20);
		assertEquals(0.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	public void testZeroWithdrawal() {
		BankAccount account = new BankAccount();

		try {
			account.deposit(20);
			account.withdraw(0);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
			assertEquals(account.getCurrentBalance(), 20.0, 0.005);
		}
	}

	@Test
	public void testNegativeWithdrawal() {
		BankAccount account = new BankAccount();

		try {
			account.deposit(20);
			account.withdraw(-5);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
			assertEquals(20.0, account.getCurrentBalance(), 0.005);
		}
	}

	@Test
	public void testOverdraftWithdrawal() {
		BankAccount account = new BankAccount();

		try {
			account.deposit(20);
			account.withdraw(25);
			fail();
		} catch (InsufficientFundsException e) {
			assertTrue(e != null);
			assertEquals(20.0, account.getCurrentBalance(), 0.005);
		}
	}

	@Test
	public void testGetType() {
		BankAccount account = new BankAccount(100, "savings", "12345");
		assertEquals("savings", account.getType());
	}

	@Test
	public void testGetAccountId() {
		BankAccount account = new BankAccount(100, "checking", "acc123");
		assertEquals("acc123", account.getAccountId());
	}

	@Test
	public void testToString() {
		BankAccount account = new BankAccount(100, "checking", "acc456");
		assertEquals("BankAccount{accountId=acc456}", account.toString());
	}
	@Test
	public void testDepositExceedAccountLimit() {
	    BankAccount account = new BankAccount(100, "checking", "testChecking");
	    
	    try {
	        account.deposit(5000);
	        fail("Expected IllegalArgumentException when deposit exceeds checking account limit");
	    } catch (IllegalArgumentException e) {
	        assertEquals("Deposit would exceed checking account limit of $5000.", e.getMessage());
	       
	        assertEquals(100.0, account.getCurrentBalance(), 0.005);
	    }
	}


}