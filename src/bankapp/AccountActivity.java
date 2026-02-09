package bankapp;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import exceptions.InsufficientFundsException;

public class AccountActivity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Map<String, BankAccount> unfrozenAccounts;
	private Map<String, BankAccount> frozenAccounts;

	public AccountActivity() {
		unfrozenAccounts = new HashMap<>();
		frozenAccounts = new HashMap<>();
	}

	public void addAccount(BankAccount account) {
		if (account != null && account.getAccountId() != null) {
			unfrozenAccounts.put(account.getAccountId(), account);
		}
	}

	public void freezeAccount(String accountId) {

		BankAccount account = unfrozenAccounts.remove(accountId);
		if (account != null) {
			frozenAccounts.put(accountId, account);
			account.getTransactions().add(new Transaction("Freeze", 0));
			System.out.println("Account " + accountId + " has been frozen.");
		} else {
			System.out.println("Account " + accountId + " not found in active accounts or is already frozen.");
		}

	}
	public void unfreezeAccount(String accountId) {
		BankAccount account = frozenAccounts.remove(accountId);
		if (account != null) {
			unfrozenAccounts.put(accountId, account);
			account.getTransactions().add(new Transaction("Unfreeze", 0));
			System.out.println("Account " + accountId + " has been unfrozen.");
		} else {
			System.out.println("Account " + accountId + " not found in frozen accounts.");
		}
	}

	public void deposit(String accountId, double amount) {
		if (frozenAccounts.containsKey(accountId)) {
			System.out.println("Cannot deposit to frozen account " + accountId + ".");
			return;
		}
		BankAccount account = unfrozenAccounts.get(accountId);
		if (account != null) {
			account.deposit(amount);
			System.out.println("Deposited $" + amount + " into account " + accountId + ".");
		} else {
			System.out.println("Account " + accountId + " not found.");
		}
	}
	public void withdraw(String accountId, double amount) {
		if (frozenAccounts.containsKey(accountId)) {
			System.out.println("Cannot withdraw from frozen account " + accountId + ".");
			return;
		}
		BankAccount account = unfrozenAccounts.get(accountId);
		if (account != null) {
			try {
				account.withdraw(amount);
				System.out.println("Withdrew $" + amount + " from account " + accountId + ".");
			} catch (Exception e) {
				System.out.println("Withdrawal failed: " + e.getMessage());
			}
		} else {
			System.out.println("Account " + accountId + " not found.");
		}
	}
	public void transfer(String fromAccountId, String toAccountId, double amount)
	        throws InsufficientFundsException {
	    if (frozenAccounts.containsKey(fromAccountId)) {
	        System.out.println("Cannot transfer from frozen account " + fromAccountId + ".");
	        return;
	    }
	    if (frozenAccounts.containsKey(toAccountId)) {
	        System.out.println("Cannot transfer to frozen account " + toAccountId + ".");
	        return;
	    }
	    BankAccount from = unfrozenAccounts.get(fromAccountId);
	    BankAccount to   = unfrozenAccounts.get(toAccountId);
	    if (from == null || to == null) {
	        System.out.println("One or both accounts not found.");
	        return;
	    }
	    from.withdraw(amount);    
	    to.deposit(amount);
	    System.out.println("Transferred $" + amount
	        + " from account " + fromAccountId
	        + " to account "   + toAccountId + ".");
	}
}
