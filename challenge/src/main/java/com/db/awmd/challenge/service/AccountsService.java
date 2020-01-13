package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;

import lombok.Getter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

  @Getter
  private AccountsRepository accountsRepository;
  
  public AccountsService() {
	// TODO Auto-generated constructor stub
}

@Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public AccountsRepository getAccountsRepository() {
	  return accountsRepository;
  }

  public Account getAccount(String accountId) {
	  return this.accountsRepository.getAccount(accountId);
  }
  
  public void transferAmount(String accountFromId, String accountToId, BigDecimal amount) {
	  this.accountsRepository.transferAmount(accountFromId, accountToId, amount);
  }
  
}
