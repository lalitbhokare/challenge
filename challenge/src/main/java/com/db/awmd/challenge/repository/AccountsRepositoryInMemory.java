package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.service.NotificationService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();
  
  @Autowired
  private NotificationService notificationService;
  
  public NotificationService getNotificationService() {
	  return notificationService;
  }

  public void setNotificationService(NotificationService notificationService) {
	  this.notificationService = notificationService;
  }
  
  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }


  synchronized  public void transferAmount(String accountFromId, String accountToId, BigDecimal amount) {
	  // TODO Auto-generated method stub
	  if(getAccount(accountFromId)!= null) {
		  if(getAccount(accountToId)!= null) {
			  Account accountFrom = getAccount(accountFromId);
			  boolean flag = debitFromAccount(accountFrom, amount);
			  if(flag) {
				  Account accountTo = getAccount(accountToId);
				  boolean creditFlag = creditToAccount(accountTo, amount);
				  if(creditFlag) {
					  //implement send notification functionality
					  sendNotification(accountFrom,accountTo,amount);
				  } else {

				  }
			  } else {
				  throw new InsufficientBalanceException(
					        "Insufficient balance !!!");
			  }

		  } else {
			  throw new AccountNotFoundException(
				        "Account " + accountToId + " not found!");
		  }
	  } else {
		  throw new AccountNotFoundException(
			        "Account " + accountFromId + " not found!");
	  }
	  
  }
  
  public boolean debitFromAccount(Account account, BigDecimal amount) {
	  if(amount.doubleValue() < account.getBalance().doubleValue()) {
		  account.setBalance(account.getBalance().subtract(amount));
		  return true;
	  } else {
		  return false;
	  }
  }

  public boolean creditToAccount(Account account, BigDecimal amount) {
	  account.setBalance(account.getBalance().add(amount));
	  return true;
  }
  
  public void sendNotification(Account accountFrom, Account accountTo, BigDecimal amount) {
	  try {
		  String accountFromNotification = amount +  " has been transfered to " + accountTo.getAccountId() + " & your Current Balance is : " + accountFrom.getBalance();
		  String accountToNotification = amount +  " has been transfered from " + accountFrom.getAccountId() + " & your Current Balance is : " + accountTo.getBalance();
		  notificationService.notifyAboutTransfer(accountFrom, accountFromNotification);
		  notificationService.notifyAboutTransfer(accountTo, accountToNotification);
	  } catch(RuntimeException e) {
		  e.printStackTrace();
	  }
  }

}
