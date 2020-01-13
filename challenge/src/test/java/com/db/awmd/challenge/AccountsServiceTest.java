package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  
  @Test
  public void transferAmount() throws Exception {
	  Account account = new Account("Id-1234");
	  account.setBalance(new BigDecimal(12000));
	  this.accountsService.createAccount(account);

	  Account account1 = new Account("Id-111");
	  account1.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(account1);
	  String accountFromId = "Id-1234";
	  String accountToId = "Id-111";
	  BigDecimal amount = new BigDecimal(1200);
	  try {
		  this.accountsService.transferAmount(accountFromId, accountToId, amount);
	  } catch(RuntimeException e) {
		  e.printStackTrace();
	  }

  }
  
  @Test
  public void transferAmount_InsufficientFund() throws Exception {
	  
	  String accountFromId = "Id-123";
	  String accountToId = "Id-111";
	  BigDecimal amount = new BigDecimal(1200000);
	  
	  try {
		  this.accountsService.transferAmount(accountFromId, accountToId, amount);
	    } catch (InsufficientBalanceException ex) {
	      assertThat(ex.getMessage()).isEqualTo("Insufficient balance !!!");
	    } catch (AccountNotFoundException ex) {
	    	ex.printStackTrace();
	    }
  }
  
  
}
