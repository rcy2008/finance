package com.cwmd.finance.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by penghongqin on 14-11-14.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Order extends PersistentDomain<Long> {
	private static final long serialVersionUID = 2712778126633131885L;

	@JsonIgnore
    @OneToOne
    @JoinColumn(name = "transaction_id", updatable = false)
    @NotNull
    private Transaction transaction;
	
	@JsonIgnore
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private OrderProfit orderProfit;

    @JsonProperty("r_c")
    @Column(name = "receiver_cell_no", nullable = true, length = 16)
    @NotNull
    private String receiverCellNo;

    @JsonProperty("r_n")
    @Column(name = "receiver_name", nullable = true, length = 16)
    @NotNull
    private String receiverName;

    @JsonProperty("r_a")
    @Column(name = "receiver_address",nullable = true)
    @NotNull
    private String  receiverAddress;

    @JsonProperty("d")
    @Column(name = "district",nullable = true)
    @NotNull
    private String  district;

    @JsonIgnore
    @Column(name = "product_price", nullable = false, precision = 16, scale = 2)
    private BigDecimal productsPrice = BigDecimal.ZERO;

    @JsonIgnore
    @Column(name = "delivery_charge", nullable = false, precision = 16, scale = 2)
    private BigDecimal deliveryCharge = BigDecimal.ZERO;

}
