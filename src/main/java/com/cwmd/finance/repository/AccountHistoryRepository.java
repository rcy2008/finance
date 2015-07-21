package com.cwmd.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stefan.snrpc.domain.AccountHistory;

/**
 * Created by CWMD . 
 * @author: chaoyang.ren  
 * @date:2015年7月16日  
 * @time:下午2:01:21   
 * @email:chaoyang.ren@foxmail.com  
 * @version: 1.0
 */
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long>{

}

