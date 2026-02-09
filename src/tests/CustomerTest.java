package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bankapp.Customer;
import bankapp.BankAccount;

import java.util.List;

class CustomerTest {
	private Customer customer;

	@BeforeEach
	void setUp() {
		customer = new Customer("Nicole Lucas");
	}

	@Test
	void testCustomerCreation() {
		assertEquals("Nicole Lucas", customer.getName());
		assertTrue(customer.getAccounts().isEmpty(), "New customer should have no accounts initially.");
	}

}
