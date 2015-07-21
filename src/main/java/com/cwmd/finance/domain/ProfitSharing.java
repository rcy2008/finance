package com.cwmd.finance.domain;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分润信息记录表
 * Created by CWMD .
 * @author: chaoyang.ren
 * @date:2015年7月16日
 * @time:下午5:56:16
 * @email:chaoyang.ren@foxmail.com
 * @version: 1.0
 */
@Entity
@Table(name = "profit_sharing")
@Getter
@Setter
@ToString(callSuper = true, exclude = {"transaction"})
@EqualsAndHashCode(callSuper = true, exclude = {"transaction"})
public class ProfitSharing extends PersistentDomain<Long> {
	private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "transaction_id")
	@NotNull
    private Transaction transaction;
	
	/**
	 * 订单应分润基数
	 */
	@Column(name="amount")
	@NotNull
	private BigDecimal amount;
	
	/**
	 * 订单实际分润总额
	 */
	@Column(name="actual_amt")
	@NotNull
	private BigDecimal actualAmt;
	
	/**
	 * 订单分润后余额
	 * 正常分润订单余额为零
	 */
	@Column(name="balance")
	@NotNull
	private BigDecimal balance;
	
	
	/**
	 * 订单分润的账户明细
	 */
	@OneToMany(mappedBy = "profitShare",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	private Set<AccountProfitSharing> accountDetails;
}
