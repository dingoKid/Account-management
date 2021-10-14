package Accounting.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import Accounting.model.Account;
import Accounting.model.Transfer;
import Accounting.repository.AccountRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Test
	void testThatAccountIsCreated() throws Exception {		
		List<Account> listBefore = accountRepository.findAll();
		
		webTestClient
			.post()
			.uri("/accounts/new")
			.bodyValue(new Account("xy", 1))
			.exchange();
		
		List<Account> listAfter = accountRepository.findAll();		
		Account account = accountRepository.findByAccountNumber(1);
		
		assertThat(listBefore.size()).isNotEqualTo(listAfter.size());
		assertThat(account.getOwner()).isEqualTo("xy");
	}	
	
	@Test
	void testGetBalance() throws Exception {
		Account account = new Account("xy", 2);
		account.setBalance(1500);
		accountRepository.save(account);
		
		Double balance = webTestClient
			.get()
			.uri("/accounts/2")
			.exchange()
			.expectBody(Double.class)
			.returnResult()
			.getResponseBody();
		
		assertThat(balance).isEqualTo(1500);
		
	}
	
	@Test
	void testThatGetBalanceFailsIfAccountNumberNotExists() throws Exception {
		webTestClient
			.get()
			.uri("/accounts/222222")
			.exchange()
			.expectStatus().isNotFound();
	}
	
	@Test
	void testBalanceDeposit() throws Exception {
		Account account = new Account("xy", 3);
		account.setBalance(1500);
		accountRepository.save(account);
		
		Double balance = webTestClient
			.get()
			.uri("/accounts/3/deposit?amount=250")
			.exchange()
			.expectBody(Double.class)
			.returnResult()
			.getResponseBody();
		
		assertThat(balance).isEqualTo(1750);
	}
	
	@Test
	void testBalanceWithdraw() throws Exception {
		Account account = new Account("xy", 4);
		account.setBalance(1500);
		accountRepository.save(account);
		
		Double balance = webTestClient
			.get()
			.uri("/accounts/4/withdraw?amount=250")
			.exchange()
			.expectBody(Double.class)
			.returnResult()
			.getResponseBody();
		
		assertThat(balance).isEqualTo(1250);
	}
	
	@Test
	void testMoneyTransfer() throws Exception {
		Account acc1 = new Account("ab", 5);
		acc1.setBalance(2000);
		Account acc2 = new Account("abc", 6);
		acc2.setBalance(3000);
		accountRepository.saveAll(List.of(acc1, acc2));
		
		Transfer transfer = new Transfer();
		transfer.setSourceAccountNumber(5);
		transfer.setDestinationAccountNumber(6);
		transfer.setAmount(750);
		
		webTestClient
			.post()
			.uri("/accounts/transfer")
			.bodyValue(transfer)
			.exchange();
		
		Account persistedAccount1 = accountRepository.findByAccountNumber(5);
		Account persistedAccount2 = accountRepository.findByAccountNumber(6);
		
		assertThat(persistedAccount1.getBalance()).isEqualTo(1250);
		assertThat(persistedAccount2.getBalance()).isEqualTo(3750);
		
	}
	
}
