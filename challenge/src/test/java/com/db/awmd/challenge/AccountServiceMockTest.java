package com.db.awmd.challenge;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.db.awmd.challenge.service.AccountsService;

@SpringBootTest
public class AccountServiceMockTest {
	
	@Autowired
	private AccountsService accountsService;
	
	
	@Test
	public void transactionMockTest() throws Exception {
		/*accountsService = Mockito.spy(new AccountsService());
		Thread thread = Mockito.mock(Thread.class);
		PowerMockito.whenNew(Thread.class).withParameterTypes(Runnable.class).withArguments(captor.capture())
				.thenReturn(thread);
		accountsService.doSomthingDifferent();
		captor.getValue().run();
		Mockito.verify(accountsService, Mockito.times(1)).doSomthingDifferent();*/
	}
}
