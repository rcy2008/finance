package com.cwmd.finance.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 订单 分润
 */
@Entity
@Table(name="order_profit")
@Getter
@Setter
@NoArgsConstructor
public class OrderProfit extends PersistentDomain<Long>{
	private static final long serialVersionUID = -1372606062383254840L;
	/**
	 * 订单号
	 */
	@OneToOne
	@JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
	@NotNull
	private Order order;
	/**
	 * 学员比例
	 */
	@JsonIgnore
	@Column(name="member_percent",nullable=false)
	private BigDecimal memberPercent = BigDecimal.ZERO;
	
	/**
	 * 授权机构比例
	 */
	@JsonIgnore
	@Column(name="authorg_percent",nullable=false)
	private BigDecimal authorgPercent = BigDecimal.ZERO;
	
	/**
	 * 鼎砥比例
	 */
	@JsonIgnore
	@Column(name="org_percent",nullable=false)
	private BigDecimal orgPercent = BigDecimal.ZERO;
	
	/**
	 * 供应商比例
	 */
	@JsonIgnore
	@Column(name="provider_percent",nullable=false)
	private BigDecimal providerPercent = BigDecimal.ZERO;
	
	/**
	 * 事业部比例
	 */
	@JsonIgnore
	@Column(name="bizunit_percent",nullable=false)
	private BigDecimal bizUnitPercent = BigDecimal.ZERO;
	
	/**
	 * 角色
	 */
	@Embedded
	private Role role;
}
