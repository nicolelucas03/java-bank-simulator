package bankapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable{
	private static final long serialVersionUID = 1L;

	private String name;
	private List<BankAccount> accounts;
	private CustomerProfile profile;
	private AccountActivity activity;

	public Customer(String name) {
		this.name = name;
		this.accounts = new ArrayList<>();
		this.activity = new AccountActivity();
	}

	public List<BankAccount> getAccounts() {
		return accounts;
	}

	public String getName() {
		return name;
	}
	public CustomerProfile getProfile() {
		return profile;
	}

	public void setProfile(CustomerProfile profile) {
		this.profile = profile;
	}
	
	public AccountActivity getActivity() {
		return activity;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"name='" + name + '\'' +
				", accounts=" + accounts +
				", profile=" + profile +
				'}';
	}

	public List<BankAccount> getAccountsByType(String accountType) {
		List<BankAccount> filtered = new ArrayList<>();
		for (BankAccount account : accounts) {
			if (account.getType().equalsIgnoreCase(accountType)) {
				filtered.add(account);
			}
		}
		return filtered;
	}

}
