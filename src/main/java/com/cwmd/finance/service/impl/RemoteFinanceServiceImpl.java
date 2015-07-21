package com.cwmd.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cwmd.finance.service.AutoProfitShareService;
import com.cwmd.remoting.finance.FinanceService;

/**
 * Created by CWMD . 
 * @author: chaoyang.ren  
 * @date:2015年7月20日  
 * @time:下午2:55:32   
 * @email:chaoyang.ren@foxmail.com  
 * @version: 1.0
 */
@Transactional
public class RemoteFinanceServiceImpl implements FinanceService{

	@Autowired
	private AutoProfitShareService autoProfitShareService;
	
	@Override
	public void startingProfitSharing() {
		autoProfitShareService.shareProfit();
	}
}

