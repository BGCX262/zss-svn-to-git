package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the premium database table.
 * 
 */
@Entity
@Table(name = "premium")
public class Premium implements Serializable {

	@Id
	@Column(name = "PREMIUM_ID", unique = true, nullable = false)
	private int premiumId;

	@Column(name = "DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name = "DATE_UPDATED")
	private Timestamp dateUpdated;

	@Temporal(TemporalType.DATE)
	@Column(name = "DUE_DATE")
	private Date dueDate;

	@Column(name = "MIN_DUE_AMOUNT")
	private double minDueAmount;

	@Column(name = "TRANS_PREMIUM_AMOUNT")
	private double transPremiumAmount;

	@Column(length = 45)
	private String type;

	// bi-directional many-to-one association to Billing
	@ManyToOne
	@JoinColumn(name = "BILLING_ACCOUNT_NUMBER")
	private Billing billing;

	public Premium() {
	}

	public int getPremiumId() {
		return this.premiumId;
	}

	public void setPremiumId(int premiumId) {
		this.premiumId = premiumId;
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

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public double getMinDueAmount() {
		return this.minDueAmount;
	}

	public void setMinDueAmount(double minDueAmount) {
		this.minDueAmount = minDueAmount;
	}

	public double getTransPremiumAmount() {
		return this.transPremiumAmount;
	}

	public void setTransPremiumAmount(double transPremiumAmount) {
		this.transPremiumAmount = transPremiumAmount;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Billing getBilling() {
		return this.billing;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}

}