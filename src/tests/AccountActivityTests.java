package tests;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.*;

import bankapp.BankAccount;
import bankapp.Customer;
import bankapp.Menu;
import bankapp.Transaction;
import bankapp.AccountActivity; 
import exceptions.InsufficientFundsException;
import exceptions.InvalidMenuOptionException;

import java.io.*;
import java.util.List;

public class AccountActivityTests {
	
	@Test
    public void testFreezeAccount() {
        AccountActivity activity = new AccountActivity();
        BankAccount account = new BankAccount(0, "checking", "account11");
        activity.addAccount(account);

        activity.freezeAccount("account11");

        activity.deposit("account11", 100);
        assertEquals(0, account.getCurrentBalance(), 0.001, "Frozen account should not accept deposits");

        List<Transaction> transactionReview = account.getTransactions();
        assertFalse(transactionReview.isEmpty(), "Transactions should not be empty after freezing");
        Transaction last = transactionReview.get(transactionReview.size() - 1);
        assertEquals("Freeze", last.getType(), "Last transaction type should be 'Freeze'");
        assertEquals(0, last.getAmount(), 0.001, "Freeze transaction amount should be 0");
    }

    @Test
    public void testUnfreezeAccount() {
    	 AccountActivity activity = new AccountActivity();
         BankAccount account = new BankAccount(0, "checking", "account1");
         activity.addAccount(account);

         activity.freezeAccount("account1");
         activity.unfreezeAccount("account1");

         List<Transaction> transactionReview = account.getTransactions();
         assertTrue(
        		 transactionReview.stream().anyMatch(t -> "Unfreeze".equals(t.getType())),
             "Transactions should include an 'Unfreeze' record"
         );

         activity.deposit("account1", 100);
         assertEquals(100, account.getCurrentBalance(), 0.001, "Unfrozen account should accept deposits");
    }
    @Test
    public void testWithdrawFrozenAccount() {
        AccountActivity activity = new AccountActivity();
        BankAccount account = new BankAccount(100.0, "checking", "account2");
        activity.addAccount(account);

        activity.freezeAccount("account2");

        activity.withdraw("account2", 50);
        assertEquals(100.0, account.getCurrentBalance(), 0.001, "Frozen account should not allow withdrawals");
    }

    @Test
    public void testWithdrawUnfrozenAccount() {
        AccountActivity activity = new AccountActivity();
        BankAccount account = new BankAccount(200.0, "checking", "account3");
        activity.addAccount(account);

        activity.withdraw("account3", 50);
        assertEquals(150.0, account.getCurrentBalance(), 0.001, "Unfrozen account should allow withdrawals");

        List<Transaction> transactionHistoryTest = account.getTransactions();
        assertFalse(transactionHistoryTest.isEmpty(), "Transactions should not be empty after withdrawal");
        Transaction last = transactionHistoryTest.get(transactionHistoryTest.size() - 1);
        assertEquals("withdrawal", last.getType(), "Last transaction type should be 'withdrawal'");
        assertEquals(50, last.getAmount(), 0.001, "Withdrawal transaction amount should match");
    }
    @Test
    public void testTransferFromFrozenAccountIsBlocked() {
        AccountActivity activity = new AccountActivity();
        BankAccount from = new BankAccount(100.0, "checking", "fromAcc");
        BankAccount to   = new BankAccount(0.0,   "checking",  "toAcc");
        activity.addAccount(from);
        activity.addAccount(to);

        activity.freezeAccount("fromAcc");

        assertDoesNotThrow(() -> activity.transfer("fromAcc", "toAcc", 50.0));

        assertEquals(100.0, from.getCurrentBalance(), 0.001,
                     "Frozen source account should keep its full balance");
        assertEquals(0.0, to.getCurrentBalance(), 0.001,
                     "Target account should not receive any funds when source is frozen");
    }
}