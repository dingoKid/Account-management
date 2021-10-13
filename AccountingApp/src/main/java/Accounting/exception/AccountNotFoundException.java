package Accounting.exception;

public class AccountNotFoundException extends RuntimeException {

	private static final String message = "Account number not found: ";
	private long accountNumber;
	
	public AccountNotFoundException(long accountNumber) {
		super(message);
		this.accountNumber = accountNumber;
	}

	public String getMessage() {
		return message + accountNumber;
	}
	
}
