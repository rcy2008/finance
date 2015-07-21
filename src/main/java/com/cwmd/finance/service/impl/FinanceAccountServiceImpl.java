package com.cwmd.finance.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stefan.snrpc.domain.Account;
import org.stefan.snrpc.domain.AccountHistory;
import org.stefan.snrpc.domain.AccountHistory.Direction;
import org.stefan.snrpc.domain.AccountHistory.Indicator;

import com.cwmd.finance.repository.AccountHistoryRepository;
import com.cwmd.finance.repository.AccountRepository;
import com.cwmd.finance.service.FinanceAccountService;

/**
 * Created by CWMD . 
 * @author: chaoyang.ren  
 * @date:2015年7月14日  
 * @time:下午2:50:41   
 * @email:chaoyang.ren@foxmail.com  
 * @version: 1.0
 */
@Transactional
@Service
public class FinanceAccountServiceImpl implements FinanceAccountService{
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AccountHistoryRepository accountHistoryRepository;
	
	@Override
	@Transactional
	public boolean reduction(Long accountId, BigDecimal cost, Indicator indicator) {
		Account account = accountRepository.getOne(accountId);
		account.setBalance(account.getBalance().subtract(cost));
		account.setLastModified(System.currentTimeMillis());
		accountRepository.save(account);
		Direction direction = Direction.OUT;
		addHistory(account, cost, direction, indicator);
		return true;
	}

	@Override
	@Transactional
	public boolean add(Long accountId, BigDecimal incomingItem, Indicator indicator) {
		Account account = accountRepository.getOne(accountId);
		account.setBalance(account.getBalance().add(incomingItem));
		account.setLastModified(System.currentTimeMillis());
		accountRepository.save(account);
		Direction direction = Direction.IN;
		addHistory(account, incomingItem, direction, indicator);
		return true;
	}
	
	@Transactional
	private void addHistory(Account account, BigDecimal flowAmount, Direction direction, Indicator indicator) {
        AccountHistory accountHistory = new AccountHistory(account, flowAmount, account.getBalance(), direction, indicator);
        accountHistoryRepository.save(accountHistory);
    }
}

