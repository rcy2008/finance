package com.cwmd.finance.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "account_profit_sharing")
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"profitShare"})
@EqualsAndHashCode(callSuper = true, exclude = {"profitShare"})
public class AccountProfitSharing extends PersistentDomain<Long> {
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name = "profit_share_id")
	@NotNull
    private ProfitSharing profitShare;
	
	/**
	 * 账户ID
	 */
	@Column(name="account_id")
	@NotNull
	private Long accountId;
	
	/**
	 * 账户分润比例
	 */
	@Column(name="percent")
	@NotNull
	private BigDecimal percent;
	
	/**
	 * 账户分润金额
	 */
	@Column(name="amount")
	@NotNull
	private BigDecimal amount;
	
	/**
	 * 账户分润时角色
	 */
	@NotNull
	@Embedded
	private Role role;
	
	/**
	 * @author: chaoyang.ren 
	 * @date:2015年7月17日  下午1:16:27
	 * @param profitShare
	 * @param account
	 * @param amount
	 * @param percent
	 * @param role
	 * @return
	 */
	public static AccountProfitSharing of(ProfitSharing profitShare,Long accountId,BigDecimal amount,BigDecimal percent,Role role){
		AccountProfitSharing aps = new AccountProfitSharing();
		aps.setProfitShare(profitShare);
		aps.setAccountId(accountId);
		aps.setAmount(amount);
		aps.setActive(true);
		aps.setCreated(System.currentTimeMillis());
		aps.setLastModified(System.currentTimeMillis());
		aps.setPercent(percent);
		aps.setRole(role);
		return aps;
	} 
	
}
