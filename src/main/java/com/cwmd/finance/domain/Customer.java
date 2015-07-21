package com.cwmd.finance.domain;

/**
 * @author: Penghong.qin
 * migrate to CWMD cwmd_user_base;
 */


//import com.dsecet.foundation.model.Password;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.stefan.snrpc.domain.Authentication;
import org.stefan.snrpc.domain.Password;
import org.stefan.snrpc.domain.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer extends PersistentDomain<Long>{

    @Column(name = "sid", nullable = false, unique = true)
    private Long sid;

    @NotBlank
    @Column(name="user_name", nullable = false, unique = true, updatable = false, length = 32)
    @Size(max = 32, min = 4)
    @Pattern(regexp = "[\\d\\w@]*")
    private String username;
    
    @Column(name="real_name", nullable = true, unique = false, updatable = true, length = 32)
    private String realName;

    @Embedded
//    @Transient
    private Role role = Role.CUSTOMER_ROLE;

    @JsonIgnore
//    @Column(name = "open_id", length = 60)
    @Transient
    private String openId;

    @JsonIgnore
//    @Column(name = "type", length = 1)
    @Transient
    private String type;

//    @Column(name = "gender", length = 8)
    @Transient
    private UserDetails.Gender gender;
    
    @Transient
    private String position;
    
    @Transient
    private String belongOrgName;

//    @Column(name = "age_group", length = 10)
    @Transient
    private String ageGroup;

//    @Column(name = "work_phone", length = 20)
    @Transient
    private String workPhone;

//    @Column(name = "work_address", length = 40)
    @Transient
    private String workAddress;
    
//    @Column(name = "is_platform_user",nullable = false)
    @Transient
    private Boolean isPlatformUser = false;

//    @Embedded
//    @NotNull
    @Transient
    private Authentication authentication = new Authentication();
    
    @Setter(AccessLevel.PRIVATE)
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "referee_id")
    private Customer referee;

    @OneToMany(mappedBy = "referee")
    private Set<Customer> referrals = new HashSet<Customer>();

    @OneToMany(mappedBy = "seller")
    private Set<Transaction> sellTransactions = new HashSet<Transaction>();

    @OneToMany(mappedBy = "buyer") 
    private Set<Transaction> buyTransactions = new HashSet<Transaction>();



    @JsonIgnore
//    @Embedded
//    @NotNull
    @Transient
    private Password password = new Password();

    @JsonIgnore
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="salt", column=@Column(nullable = true, length = 64, name="pay_salt")),
        @AttributeOverride(name="hash", column=@Column(nullable = true, length = 128, name="pay_hash"))
    })

    public String getRealName(){
    	String authName = authentication.getRealName().getData();
        return StringUtils.isBlank(authName)?this.realName:authName;
    }

    public void setRealName(@NotBlank String realName){
        authentication.getRealName().setData(realName);
        this.realName = realName;
    }

    public String getIdCard(){
        return authentication.getIdCard().getData();
    }

    public UserDetails.IdCardType getIdCardType(){
        if(StringUtils.isEmpty(authentication.getIdCard().getToken())){
            return UserDetails.IdCardType.IDENTITYCARD;
        }
        return Enum.valueOf(UserDetails.IdCardType.class, authentication.getIdCard().getToken());
    }

    public void setIdCardType(UserDetails.IdCardType idCardType){
        authentication.getIdCard().setToken(idCardType.toString());
    }

    public void setIdCard(@NotBlank String idCard){
        authentication.getIdCard().setData(idCard);
    }

    public void setUserDetails(UserDetails userDetails){
        if(sid == null) {
            this.setSid(userDetails.getId());
        }
        this.authentication = userDetails.getAuthentication();
        this.username = userDetails.getUsername();
        this.realName = userDetails.getRealName();
        this.isPlatformUser = userDetails.getIsPlatformuser();
        this.ageGroup = userDetails.getAgeGroup();
        this.gender = userDetails.getGender();
        this.type = userDetails.getType();
        this.workPhone = userDetails.getWorkPhone();
        this.workAddress = userDetails.getWorkAddress();
        this.belongOrgName = userDetails.getBelongOrgName();
        this.position = userDetails.getPosition();
        this.password.setHash(userDetails.getPassword());
        this.password.setSalt(userDetails.getSalt());
        this.setLastModified(userDetails.getLastModified());
        this.setCreated(userDetails.getCreated());
        this.setActive(userDetails.isActive());
    }

    public UserDetails exportUserDetails(){
        UserDetails userDetails = new UserDetails();
        userDetails.setId(this.sid);
        userDetails.setAuthentication(this.authentication);
        if(StringUtils.isNotBlank(realName)){
        	userDetails.setRealName(realName);
        }
        userDetails.setUsername(this.username);
        userDetails.setIsPlatformuser(this.isPlatformUser);
        userDetails.setAgeGroup(this.ageGroup);
        userDetails.setGender(this.gender);
        userDetails.setType(this.type);
        userDetails.setActive(this.isActive());
        userDetails.setWorkPhone(this.workPhone);
        userDetails.setWorkAddress(this.workAddress);
        userDetails.setPassword(this.password);
        userDetails.setLastModified(getLastModified());
        userDetails.setCreated(getCreated());
        return userDetails;
    }

    /*public void  refund(BigDecimal amount){
        Account account = this.getAccount();
        account.add(amount,AccountFlowState.REFUND);
    }*/
    
    /*void charge(BigDecimal amount){
    	Account account = this.getAccount();
        account.add(amount,AccountFlowState.CHARGE_GAIN);
    }*/
    
    /*
     * @Description: 用户支付
     * @param @param amount
     * @return void
     * @throws
     */
    /*void payOut(BigDecimal amount){
    	Account account = this.getAccount();
        account.reduction(amount, AccountFlowState.PAY_OUT);
    }*/

//    public enum Gender{
//        MALE, FEMALE
//    }
//
//    public enum IdCardType{
//        IDENTITYCARD, PASSPORT
//    }
}

