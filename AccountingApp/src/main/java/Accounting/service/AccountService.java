package Accounting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import Accounting.exception.AccountNotFoundException;
import Accounting.exception.NotEnoughFundsException;
import Accounting.model.Account;
import Accounting.model.Transfer;
import Accounting.repository.AccountRepository;

@Service
public class AccountService {
	
	@Autowired
	AccountRepository accountRepository;
	
	public List<Account> findAllAccounts() {
		return accountRepository.findAll();
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public boolean createAccount(Account account) {
		if(accountRepository.findByAccountNumber(account.getAccountNumber()) == null) {
			accountRepository.save(account);
			return true;
		}
		return false;
	}
	
	public double getBalance(long accountNumber) {
		return findAccount(accountNumber).getBalance();
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public double depositAccount(long accountNumber, double amount) {
		Account account = findAccount(accountNumber);
		if(amount < 0) throw new IllegalArgumentException("Amount can not be negative!");
		account.deposit(amount);
		return account.getBalance();
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public double withdrawAccount(long accountNumber, double amount) {
		Account account = findAccount(accountNumber);
		if(amount < 0) throw new IllegalArgumentException("Amount can not be negative!");
		if(!account.withdraw(amount))
			throw new NotEnoughFundsException();
		return account.getBalance();
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public double transferMoney(Transfer transfer) {
		Account sourceAccount = findAccount(transfer.getSourceAccountNumber());
		Account destinationAccount = findAccount(transfer.getDestinationAccountNumber());		
		double amount = transfer.getAmount();
		if(amount < 0) throw new IllegalArgumentException("Amount can not be negative!");
		if(!sourceAccount.withdraw(amount))
			throw new NotEnoughFundsException();
		destinationAccount.deposit(amount);
		return sourceAccount.getBalance();
	}
	
	private Account findAccount(long accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber);
		if(account == null) {
			throw new AccountNotFoundException(accountNumber);
		}
		return account;
	}
	

}
