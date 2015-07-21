package com.cwmd.finance.service;

import java.math.BigDecimal;

import org.stefan.snrpc.domain.AccountHistory.Indicator;

/**
 * Created by CWMD . 
 * @author: chaoyang.ren  
 * @date:2015年7月14日  
 * @time:下午2:50:12   
 * @email:chaoyang.ren@foxmail.com  
 * @version: 1.0
 */
public interface FinanceAccountService {
	/**
	 * 账户出账
	 * @author: chaoyang.ren 
	 * @date:2015年6月1日  下午2:09:44
	 * @param accountId
	 * @param cost
	 * @param indicator
	 * @return
	 */
	public boolean reduction(Long accountId, BigDecimal cost, Indicator indicator);
    
	/**
	 * 账户入账
	 * @author: chaoyang.ren 
	 * @date:2015年6月1日  下午2:09:44
	 * @param accountId
	 * @param cost
	 * @param indicator
	 * @return
	 */
    public boolean add(Long accountId, BigDecimal incomingItem, Indicator indicator);
}

