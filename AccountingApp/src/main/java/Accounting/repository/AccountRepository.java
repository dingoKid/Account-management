package Accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Accounting.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
	Account findByAccountNumber(long accountNumber);

}
