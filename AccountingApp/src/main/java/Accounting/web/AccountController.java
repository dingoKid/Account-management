package Accounting.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import Accounting.exception.AccountNotFoundException;
import Accounting.exception.NotEnoughFundsException;
import Accounting.model.Account;
import Accounting.model.Transfer;
import Accounting.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	/*
	 * Here is the assignment:  You are tasked with creating a simple banking application that will handle accounts for customers.  
	 * The target is functionality wise a MVP solution for handling the current account balance.  
	 * REST/JSON API that allows for the following:  
	 * 		- Opening accounts (creating new accounts) 		
	 * 		- Depositing amount to account 					
	 * 		- Withdrawing amount from account 				
	 * 		- Transferring amount between accounts 			
	 * 		- Requesting the current balance of account   	
	 *  
	 * Some additional notes:  
	 * 		* should be a spring/spring-boot application. 
	 * 		* Some storage is required, an embedded SQL sounds like it'll be enough. 
	 * 		* Transaction management will probably be required and/or making sure simultaneous requests for the same account are handled properly.  
	 * 		* Don't spend too much time on this, focus on the important things:  
	 * 			- Traceability and historical data, for instance, is not really a requirement. 
	 * 			- Just keeping track of the current balance associated with an account is fine for this MVP. 
	 * */

	@Autowired
	AccountService accountService;
	
	@GetMapping("/all")
	public List<Account> getAll() {
		return accountService.findAllAccounts();
	}
	
	@PostMapping("/new")
	public boolean createAccount(@RequestBody @Valid Account account) {
		return accountService.createAccount(account);
	}
	
	@GetMapping("/{accountNumber}")
	public double getBalance(@PathVariable long accountNumber) {
		try {
			return accountService.getBalance(accountNumber);
		} catch (AccountNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping("/{accountNumber}/deposit")
	public double deposit(@PathVariable long accountNumber, @RequestParam double amount) {
		try {
			return accountService.depositAccount(accountNumber, amount);
		} catch (AccountNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/{accountNumber}/withdraw")
	public double withdraw(@PathVariable long accountNumber, @RequestParam double amount) {
		try {
			return accountService.withdrawAccount(accountNumber, amount);
		} catch (AccountNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NotEnoughFundsException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PostMapping("/transfer")
	public double transferMoney(@RequestBody @Valid Transfer transfer) {
		try {
			return accountService.transferMoney(transfer);
		} catch (AccountNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NotEnoughFundsException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
		
	
	
	
	
}
