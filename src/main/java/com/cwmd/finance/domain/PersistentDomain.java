package com.cwmd.finance.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

@MappedSuperclass
@Data
public abstract class PersistentDomain<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = -4833703794114199066L;

	@ApiModelProperty(value = "Entity Identity", dataType = "long", required = true, position = -1)
    @JsonProperty("id")
    @Id
    @GeneratedValue
    private T id;

    @JsonIgnore
    @Column(nullable = false, updatable = false)
    @NotNull
    private Long created;

    @JsonIgnore
    @Column(nullable = false, name = "last_modified")
    @NotNull
    private Long lastModified;

    @JsonIgnore
    @Column(nullable = false)
    @NotNull
    private boolean active = true;

    public void updateLastModified(){
        setLastModified(System.currentTimeMillis());
    }

    @JsonIgnore
    @Transient
    public Date getCreatedDate(){
        return new Date(this.created);
    }

    @JsonIgnore
    @Transient
    public Date getLastModifiedDate(){
        return new Date(this.lastModified);
    }
}
