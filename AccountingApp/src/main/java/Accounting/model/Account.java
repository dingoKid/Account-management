package Accounting.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
public class Account {

	@Id
	@GeneratedValue
	private long id;
	
	@Min(1)
	private long accountNumber;
	
	@NotEmpty
	private String owner;
	private double balance;
	
	public Account() {}
	
	public Account(String owner) {
		this.owner = owner;
	}
	
	public synchronized void deposit(double amount) {
		this.balance += amount;
	}
	
	public synchronized boolean withdraw(double amount) {
		if(amount <= this.balance) {
			this.balance -= amount;
			return true;
		}
		return false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double deposit) {
		this.balance = deposit;
	}
}
