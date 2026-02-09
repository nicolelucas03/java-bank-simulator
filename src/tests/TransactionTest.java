package tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import bankapp.BankAccount;
import bankapp.Customer;
import bankapp.Menu;
import exceptions.InsufficientFundsException;
import bankapp.Transaction;
import java.io.*;
import java.util.*;

import java.util.Date;

public class TransactionTest {
	@Test
	public void testTransactionCreation() {
		Transaction transaction = new Transaction("Deposit", 100.0);
		assertEquals("Deposit", transaction.getType());
		assertEquals(100.0, transaction.getAmount());
	}

	@Test
	void testTransactionNegativeAmount() {
		BankAccount account = new BankAccount(500.0, "Checking", "12345");
		assertThrows(IllegalArgumentException.class, () -> {
			account.deposit(-100.0);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			account.withdraw(-50.0);
		});
	}


}