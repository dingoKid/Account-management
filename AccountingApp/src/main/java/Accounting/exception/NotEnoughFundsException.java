package Accounting.exception;

public class NotEnoughFundsException extends RuntimeException {

	private static final String message = "Balance is too low for this transfer!";
	
	public NotEnoughFundsException() {
		super(message);
	}

	public String getMessage() {
		return message;
	}
	
	
}
