package Accounting.model;

import javax.validation.constraints.Min;

public class Transfer {
	
	@Min(1)
	private long sourceAccountNumber;
	
	@Min(1)
	private long destinationAccountNumber;
	
	@Min(1)
	private double amount;
		
	public long getSourceAccountNumber() {
		return sourceAccountNumber;
	}
	
	public void setSourceAccountNumber(long sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}
	
	public long getDestinationAccountNumber() {
		return destinationAccountNumber;
	}
	
	public void setDestinationAccountNumber(long destinationAccountNumber) {
		this.destinationAccountNumber = destinationAccountNumber;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
