package tests;

import bankapp.CustomerProfile;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bankapp.Customer;
import bankapp.BankAccount;

import java.util.List;

public class CustomerProfileTest {
	@Test
	public void testOccupationAndAddressUpdate() {

		CustomerProfile customer = new CustomerProfile("Marketing Executive", "200 Unicorn Street");

		customer.setOccupation("CEO");
		customer.setAddress("100 Unicorn Street");

		assertEquals("CEO", customer.getOccupation(), "Occupation should be updated to CEO");
		assertEquals("100 Unicorn Street", customer.getAddress(), "Address should be updated to 100 Unicorn Street");
	}

}