package com.cwmd.finance.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @Description: 系统角色类型枚举
 * @author cy.ren
 * @date 2015年5月5日 下午2:23:26
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Role{
	
//	@JsonIgnore
    @NonNull
    @Column(name = "role", length = 10)
    @Enumerated(value = EnumType.STRING)
	private RoleEnum role = getDefaultRole();


    public static final Role OPERATOR_ROLE=new Role(RoleEnum.O);//运营方
    public static final Role MEMBER_ROLE=new Role(RoleEnum.M);//学员
    public static final Role AUTHORG_ROLE=new Role(RoleEnum.W);//授权机构
    public static final Role COMPANY_ROLE=new Role(RoleEnum.D);//学员所属机构
    public static final Role CLIENT_ROLE=new Role(RoleEnum.C);//学员推荐的在neofortune注册的用户
    public static final Role CUSTOMER_ROLE=new Role(RoleEnum.G);//在neofortune自行注册的用户
    public static final Role PROVIDER_ROLE=new Role(RoleEnum.P);//供应商
    public static final Role FINAICAL_PROVIDER_ROLE=new Role(RoleEnum.F);//金融产品供应商
    public static final Role BIZ_UNIT_ROLE=new Role(RoleEnum.A);//事业部
	public enum RoleEnum{
        O, M, W, D, C, G, P, F, A;
    }
	
	public static RoleEnum getDefaultRole(){
		return RoleEnum.G;
	}


    public String name(){
        return this.role.name();
    }
}
