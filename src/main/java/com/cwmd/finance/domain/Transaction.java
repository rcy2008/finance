package com.cwmd.finance.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by penghongqin on 14-11-18.
 */
@Entity
@Table(name = "transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true, exclude = {"order", "buyer", "seller"})
@EqualsAndHashCode(callSuper = true, exclude = {"order", "buyer", "seller"})
public class Transaction extends PersistentDomain<Long> {

	private static final long serialVersionUID = -8248115565387927165L;

	@Setter(AccessLevel.PRIVATE)
    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    @NonNull
    private Order order;

    @Setter(AccessLevel.PRIVATE)
    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "buyer")
    @NotNull
    private Customer buyer;

    @Setter(AccessLevel.PRIVATE)
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "seller")
    @NotNull
    private Customer seller;

    @Setter(AccessLevel.PRIVATE)
    @Column(name = "serial_no", length = 50)
    @NotNull
    private String serialNo;

    @Setter(AccessLevel.PRIVATE)
    @Column(name = "amount", precision = 16, scale = 2)
    @NotNull
    private BigDecimal amount;

    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = true)
    private String comments;

    @Setter(AccessLevel.PRIVATE)
    @JsonIgnore
    @Column
    private Type type;

    @JsonProperty(value = "p_t")
    @Column(name="payment_time")
    @Setter(AccessLevel.PRIVATE)
    private Long paymentTime;

    @JsonProperty(value = "s")
    @Embedded
    private TransactionStatus status = TransactionStatus.PENDING_FOR_PAY;

    @JsonProperty(value = "o")
    @Transient
    public List<TransactionStatus> getAllowedOperation(){
        return this.status.getAllowedTransactionStatus();
    }

	/**
	 * 是否需要发票
	 */
    @JsonIgnore
    @Column(name = "is_need_invoice", nullable = false)
    @Setter
    @NotNull
    private boolean isNeedInvoice = false;
    
    @Override
    @JsonProperty(value = "c_t")
    public Long getCreated(){
        return super.getCreated();
    }

    public enum Type {
        UPGRADE, SELLS;
    }

    @JsonIgnore
    public Date getCreateDate(){
        return new Date(getCreated());
    }

    @JsonIgnore
    public Date getPaymentTimeDate(){
        return new Date(getPaymentTime());
    }
    
}
