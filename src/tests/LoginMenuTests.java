package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

import org.junit.jupiter.api.*;

import bankapp.Customer;
import bankapp.LoginMenu;
import bankapp.Menu;

public class LoginMenuTests {

	private static final String USERS_FILE = "users.txt";

	public static void clearUserFile() {
		try {
			FileWriter writer = new FileWriter(USERS_FILE, false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeEach
	public void clearFileBeforeEachTest() {
		clearUserFile();
	}

	@AfterAll
	public static void clearFileAfterAllTests() {
		clearUserFile();
	}

	@AfterAll
	public static void clearUserInfoFiles() {
		File folder = new File("usersInfo");
		if (folder.exists()) {
			for (File file : folder.listFiles()) {
		        if (!file.getName().equals(".gitkeep")) {
		            file.delete();
		        }
			}
		}
	}

	@Test
	public void testRegister() {
		String input = String.join("\n",
				"test_user",        
				"pw123",            
				"pet's name?",     
				"Fluffy",           
				"yes"               
				) + "\n";

		provideInput(input);

		LoginMenu loginMenu = new LoginMenu();
		String username = loginMenu.register();

		assertEquals("test_user", username);
		assertTrue(userExistsInFile("test_user"));
	}

	@Test
	public void testUserExists() {
		String input = String.join("\n",
				"test_user",        
				"pw123",            
				"pet's name?",     
				"Fluffy",           
				"yes"               
				) + "\n";

		provideInput(input);

		LoginMenu loginMenu = new LoginMenu();
		loginMenu.register();

		assertTrue(loginMenu.userExists("test_user"));
		assertFalse(loginMenu.userExists("nonexistent_user"));
	}

	@Test
	public void testValidLogIn() {
		String input = String.join("\n",
				"test_user",        
				"pw123",            
				"pet's name?",      
				"Fluffy",           
				"yes",              
				"test_user",        
				"pw123"             
				) + "\n";

		provideInput(input);

		LoginMenu loginMenu = new LoginMenu();
		loginMenu.register();
		String username = loginMenu.logIn();

		assertEquals("test_user", username);
	}

	@Test
	public void testCheckCredentials() {
		String input = String.join("\n",
				"test_user",      
				"pw123",           
				"pet's name?",      
				"Fluffy",           
				"yes"               
				) + "\n";

		provideInput(input);

		LoginMenu loginMenu = new LoginMenu();
		loginMenu.register();

		assertTrue(loginMenu.checkCredentials("test_user", "pw123"));
		assertFalse(loginMenu.checkCredentials("test_user", "wrongpw"));
		assertFalse(loginMenu.checkCredentials("nonexistent_user", "pw123"));
	}

	@Test
	public void testResetPassword() {
		String input = String.join("\n",
				"test_user",        
				"pw123",            
				"pet's name?",      
				"Fluffy",           
				"yes",              
				"test_user",        
				"Fluffy",           
				"newpw123"          
				) + "\n";

		provideInput(input);

		LoginMenu loginMenu = new LoginMenu();
		loginMenu.register();
		loginMenu.resetPassword();

		assertTrue(loginMenu.checkCredentials("test_user", "newpw123"));
		assertFalse(loginMenu.checkCredentials("test_user", "pw123"));
	}

	@Test
	public void testStoreAndRetrieveCustomerInfo() {
		String input = "software engineer\n123 candy cane lane\n"+ "1\ncheck123\n50\n" + "2\nsave123\n500\n"; 
		provideInput(input);

		LoginMenu loginMenu = new LoginMenu();
		String username = "test_user";
		Customer customer = new Customer(username);
		Menu menu = new Menu(customer);
		menu.updateProfile();
		menu.openAccount();
		loginMenu.storeCustomerInfo(customer);
		Customer retrievedCustomer = loginMenu.retrieveCustomerInfo(username);

		assertNotNull(retrievedCustomer);
		assertEquals(customer.toString(), retrievedCustomer.toString());
	}

	@Test
	public void testRetrieveNewCustomerInfo() {
		String input = "software engineer\n123 candy cane lane\n"+ "1\nsave123\n500\n"; 
		provideInput(input);

		LoginMenu loginMenu = new LoginMenu();
		String username = "test_user";
		Customer customer = new Customer(username);
		Menu menu = new Menu(customer);
		menu.updateProfile();
		menu.openAccount();
		Customer retrievedCustomer = loginMenu.retrieveCustomerInfo(username);
		
		assertNotNull(retrievedCustomer);
		assertEquals(customer.getName(), retrievedCustomer.getName());
		assertNotEquals(customer.toString(), retrievedCustomer.toString());
	}
	// Helper methods

	private void provideInput(String data) {
		InputStream testInput = new ByteArrayInputStream(data.getBytes());
		System.setIn(testInput);
	}

	private boolean userExistsInFile(String username) {
		try (Scanner scanner = new Scanner(new File("users.txt"))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith(username + ",")) {
					return true;
				}
			}
		} catch (IOException e) {
			fail("IOException occurred: " + e.getMessage());
		}
		return false;
	}
}