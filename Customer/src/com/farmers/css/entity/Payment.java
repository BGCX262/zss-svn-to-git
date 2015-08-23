package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the payment database table.
 * 
 */
@Entity
@Table(name="payment")
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PAYMENT_ID", unique=true, nullable=false)
	private int paymentId;

	@Column(name="AMOUNT_PAID")
	private double amountPaid;

	@Column(name="DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name="DATE_UPDATED")
	private Timestamp dateUpdated;

	//bi-directional many-to-one association to Billing
    @ManyToOne
	@JoinColumn(name="BILLING_ACCOUNT_NUMBER")
	private Billing billing;

    public Payment() {
    }

	public int getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public double getAmountPaid() {
		return this.amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
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

	public Billing getBilling() {
		return this.billing;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}
	
}