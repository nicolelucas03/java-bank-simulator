package bankapp;

import java.io.Serializable;

public class CustomerProfile implements Serializable{
	private static final long serialVersionUID = 1L;

	private String occupation;
	private String address;

	public CustomerProfile(String occupation, String address) {
		this.occupation = occupation;
		this.address = address;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "CustomerProfile{" +
				"occupation='" + occupation + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}