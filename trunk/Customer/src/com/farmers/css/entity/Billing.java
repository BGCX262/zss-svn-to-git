package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the billing database table.
 * 
 */
@Entity
@Table(name="billing")
public class Billing implements Serializable {
 
	@Id
	@Column(name="BILLING_ACCOUNT_NUMBER", unique=true, nullable=false)
	private Long billingAccountNumber;

	@Column(name="DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name="DATE_UPDATED")
	private Timestamp dateUpdated;

	@Column(length=25)
	private String type;

	//bi-directional many-to-one association to PolicyInfo
    @ManyToOne
	@JoinColumn(name="POLICY_CONTRACT_NUMBER")
	private PolicyInfo policyInfo;

	//bi-directional many-to-one association to Payment
	@OneToMany(mappedBy="billing", fetch=FetchType.EAGER)
	private Set<Payment> payments;

	//bi-directional many-to-one association to Premium
	@OneToMany(mappedBy="billing", fetch=FetchType.EAGER)
	private Set<Premium> premiums;

    public Billing() {
    }

	public Long getBillingAccountNumber() {
		return this.billingAccountNumber;
	}

	public void setBillingAccountNumber(Long billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Timestamp dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PolicyInfo getPolicyInfo() {
		return this.policyInfo;
	}

	public void setPolicyInfo(PolicyInfo policyInfo) {
		this.policyInfo = policyInfo;
	}
	
	public Set<Payment> getPayments() {
		return this.payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}
	
	public Set<Premium> getPremiums() {
		return this.premiums;
	}

	public void setPremiums(Set<Premium> premiums) {
		this.premiums = premiums;
	}
	
}