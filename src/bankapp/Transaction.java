package bankapp;
import java.io.Serializable;
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable{
	private static final long serialVersionUID = 1L;

	private String type; 
	private double amount;
	private LocalDateTime timestamp;

	public Transaction(String type, double amount) {
		this.type = type;
		this.amount = amount;
		this.timestamp = LocalDateTime.now(); 
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

	public String getFormattedTimestamp() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return timestamp.format(formatter);
	}

	@Override
	public String toString() {
		return getFormattedTimestamp() + " - " + type + ": $" + amount;
	}
}