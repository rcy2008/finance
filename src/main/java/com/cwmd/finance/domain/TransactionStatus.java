package com.cwmd.finance.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by penghongqin on 14-12-9.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
@Embeddable
public class TransactionStatus {

    public static final TransactionStatus PENDING_FOR_PAY = new TransactionStatus(Status.PENDING_FOR_PAY);
    public static final TransactionStatus PENDING_PAYMENT_CONFIRM = new TransactionStatus(Status.PENDING_PAYMENT_CONFIRM);
    public static final TransactionStatus  PENDING_FOR_SHIP = new TransactionStatus(Status.PENDING_FOR_SHIP);
    public static final TransactionStatus PENDING_RECEIVE_CONFIRM = new TransactionStatus(Status.PENDING_RECEIVE_CONFIRM);
    public static final TransactionStatus COMPLETED = new TransactionStatus(Status.COMPLETED);
    public static final TransactionStatus CANCELLED = new TransactionStatus(Status.CANCELLED);

    @ApiModelProperty(value = "Transaction Status", dataType = "enum", required = true)
    @NonNull
    @NotNull
    @JsonProperty("c")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private Status code;

    @JsonIgnore
    public List<TransactionStatus> getAllowedTransactionStatus(){
        List<TransactionStatus> statusList = new ArrayList <TransactionStatus>();
        switch (this.code){
            case PENDING_FOR_PAY: statusList.add(PENDING_PAYMENT_CONFIRM);
                break;
            case PENDING_PAYMENT_CONFIRM: statusList.add(PENDING_FOR_SHIP);
                break;
            case PENDING_FOR_SHIP: statusList.add(PENDING_RECEIVE_CONFIRM);
                break;
            case PENDING_RECEIVE_CONFIRM: statusList.add(COMPLETED);
                break;
            default:
        }
        if(code.compareTo(Status.PENDING_FOR_SHIP) < 0){
            statusList.add(CANCELLED);
        }
        return statusList;
    }

    public enum Status {
        PENDING_FOR_PAY, PENDING_PAYMENT_CONFIRM, PENDING_FOR_SHIP,
        PENDING_RECEIVE_CONFIRM, COMPLETED, CANCELLED;


    }
}
