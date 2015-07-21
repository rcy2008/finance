package com.cwmd.finance.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stefan.snrpc.domain.Account;
import org.stefan.snrpc.domain.AccountHistory.Indicator;
import org.stefan.snrpc.domain.UserDetails;
import org.stefan.snrpc.domain.UserRelations;
import org.stefan.snrpc.service.AccountService;
import org.stefan.snrpc.service.UserDetailsService;
import org.stefan.snrpc.service.UserRelationService;

import com.cwmd.finance.domain.AccountProfitSharing;
import com.cwmd.finance.domain.Customer;
import com.cwmd.finance.domain.Order;
import com.cwmd.finance.domain.OrderProfit;
import com.cwmd.finance.domain.ProfitSharing;
import com.cwmd.finance.domain.Role;
import com.cwmd.finance.domain.Transaction;
import com.cwmd.finance.domain.TransactionStatus;
import com.cwmd.finance.repository.ProfitSharingRepository;
import com.cwmd.finance.repository.TransactionRepository;
import com.cwmd.finance.service.AutoProfitShareService;
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
@Slf4j
public class AutoProfitShareServiceImpl implements AutoProfitShareService{
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private FinanceAccountService financeAccountService;
	@Autowired
	private ProfitSharingRepository profitSharingRepository;
	
	@Transactional
    @Override
    @Scheduled(cron="1/180 * * * * ?")
    public void autoSharingProfit() {
        log.info("====start auto sharing profit job====");
        this.shareProfit();
        log.info("====end auto sharing profit job====");
    }
	
	@Transactional
	@Override
	public void shareProfit(){
		final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
        //所有当前已完成交易
        List<Transaction> transactions = transactionRepository.findByStatusAndLastModifiedBetween(TransactionStatus.COMPLETED,System.currentTimeMillis()-24*60*60*1000-1,System.currentTimeMillis());
        log.info("====get "+transactions.size()+" "+(transactions.size()>1?"transactions":"transaction")+" for profit sharing!====");
        int count = 0;
        for (Transaction transaction : transactions) {
        	ProfitSharing exsited = profitSharingRepository.findByTransaction(transaction);
        	if(exsited != null){
        		log.info("====transaction:"+transaction.getSerialNo()+" has shared profit before,will not do it again!====");
        		continue;
        	}
        	BigDecimal totalAmount = transaction.getAmount();
        	BigDecimal currentAmt = BigDecimal.ZERO;
        	ProfitSharing profitShare = new ProfitSharing();
        	profitShare.setTransaction(transaction);
        	profitShare.setAmount(transaction.getAmount());
        	profitShare.setCreated(System.currentTimeMillis());
        	profitShare.setLastModified(System.currentTimeMillis());
        	Set<AccountProfitSharing>  accountDetails = new HashSet<AccountProfitSharing>();
        	
        	//买家
        	Customer buyer = transaction.getBuyer();
        	Order o = transaction.getOrder();
        	OrderProfit op = o.getOrderProfit();
        	UserRelations buyerRelations = userRelationService.getByUsername(buyer.getUsername());
        	//推荐人
        	UserDetails referee = buyerRelations.getReferee();
        	BigDecimal refereePercent = op.getMemberPercent().divide(ONE_HUNDRED);
        	if(referee != null){
        		Account refereeAccount = accountService.findByUsername(referee.getUsername());
        		BigDecimal refereeAmount = totalAmount.multiply(refereePercent);
        		financeAccountService.add(refereeAccount.getId(), refereeAmount, Indicator.PROFIT_SHARING);
        		currentAmt = currentAmt.add(refereeAmount);
        		AccountProfitSharing aps = AccountProfitSharing.of(profitShare, refereeAccount.getId(), refereeAmount, refereePercent, Role.MEMBER_ROLE);
        		accountDetails.add(aps);
        		
        		//授权机构
        		UserRelations refereeRelations = userRelationService.getByUsername(referee.getUsername());
        		Set<UserDetails> authOrgs = refereeRelations == null?null:refereeRelations.getAuthOrgs();
        		int authOrgSize = authOrgs == null?0:authOrgs.size();
        		//分润比例无法除清时，直接舍位取2位小数以保证有足够余额支付利润
        		if(authOrgSize > 0){
        			BigDecimal authOrgPercent = op.getAuthorgPercent().divide(BigDecimal.valueOf(authOrgSize), new MathContext(2, RoundingMode.FLOOR)).divide(ONE_HUNDRED);
        			for (UserDetails authOrg : authOrgs) {
        				Account authOrgAccount = accountService.findByUsername(authOrg.getUsername());
        				BigDecimal authOrgAmount = totalAmount.multiply(authOrgPercent);
        				financeAccountService.add(authOrgAccount.getId(), authOrgAmount, Indicator.PROFIT_SHARING);
        				currentAmt = currentAmt.add(authOrgAmount);
        				AccountProfitSharing apfs = AccountProfitSharing.of(profitShare, authOrgAccount.getId(), authOrgAmount, authOrgPercent, Role.AUTHORG_ROLE);
                		accountDetails.add(apfs);
        			}
        		}else{
        			//无授权机构信息，但是授权机构分润比例大于0
            		if(op.getAuthorgPercent().compareTo(BigDecimal.ZERO) > 0){
            			log.warn("====warn:::authOrg percent is: "+op.getAuthorgPercent()+",but no auth org found!!!!");
            		}
        		}
        	}else{
        		//无推荐人信息，但是推荐人分润比例大于0
        		if(refereePercent.compareTo(BigDecimal.ZERO) > 0){
        			log.warn("====warn:::referee percent is: "+refereePercent+",but no referee found!!!!");
        		}
        	}
        	//供应商
        	Customer supplier = transaction.getSeller();
        	UserRelations supplierRelations = userRelationService.getByUsername(supplier.getUsername());
        	Account supplierAccount = accountService.findByUsername(supplier.getUsername());
        	BigDecimal supplierPercent = op.getProviderPercent().divide(ONE_HUNDRED);
        	BigDecimal supplierAmount = totalAmount.multiply(supplierPercent);
        	financeAccountService.add(supplierAccount.getId(), supplierAmount, Indicator.PROFIT_SHARING);
        	currentAmt = currentAmt.add(supplierAmount);
        	AccountProfitSharing aps = AccountProfitSharing.of(profitShare, supplierAccount.getId(), supplierAmount, supplierPercent, Role.PROVIDER_ROLE);
    		accountDetails.add(aps);
    		
        	//事业部，事业部已收全额货款，分润时扣除其不应得部分
        	UserDetails bizDepart = supplierRelations.getBizDepart();
        	Account bizDepartAccount = accountService.findByUsername(bizDepart.getUsername());
        	BigDecimal bizPercent = op.getBizUnitPercent().divide(ONE_HUNDRED);
        	BigDecimal bizAmount = totalAmount.multiply(bizPercent);
        	financeAccountService.reduction(bizDepartAccount.getId(), totalAmount.subtract(bizAmount), Indicator.PROFIT_SHARING);
        	currentAmt = currentAmt.add(bizAmount);
        	AccountProfitSharing apsb = AccountProfitSharing.of(profitShare, bizDepartAccount.getId(), bizAmount, bizPercent, Role.BIZ_UNIT_ROLE);
    		accountDetails.add(apsb);
        	//鼎砥平台
        	BigDecimal accountingPercent = op.getOrgPercent().divide(ONE_HUNDRED);
        	BigDecimal accountingAmount = totalAmount.multiply(accountingPercent);
        	financeAccountService.add(Account.NEO_ADMIN_ACCOUNT, accountingAmount, Indicator.PROFIT_SHARING);
        	currentAmt = currentAmt.add(accountingAmount);
        	AccountProfitSharing apsa = AccountProfitSharing.of(profitShare, Account.NEO_ADMIN_ACCOUNT, accountingAmount, accountingPercent, Role.OPERATOR_ROLE);
    		accountDetails.add(apsa);
    		
    		profitShare.setActualAmt(currentAmt);
    		profitShare.setBalance(totalAmount.subtract(currentAmt));
    		profitShare.setAccountDetails(accountDetails);
    		profitSharingRepository.save(profitShare);
    		count++;
		}
        log.info("====profit sharing finished,total:"+count+"====");
	}
}

