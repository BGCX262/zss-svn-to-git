package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * The persistent class for the cust_contact database table.
 * 
 */
@Entity
@Table(name = "cust_contact")
public class CustContact implements Serializable {

	@Id
	@Column(name = "CONTACT_ID", unique = true, nullable = false)
	private int contactId;

	@Column(name = "BUSINESS_PHONE", length = 20)
	private String businessPhone;

	@Column(name = "DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name = "DATE_UPDATED")
	private Timestamp dateUpdated;

	@Column(name = "EMAIL_ADDRESS", length = 45)
	private String emailAddress;

	@Column(name = "PERSONAL_PHONE", length = 20)
	private String personalPhone;

	@Column(length = 1)
	private String type;

	// bi-directional many-to-one association to CustAddress
	@OneToMany(mappedBy = "custContact", fetch = FetchType.EAGER)
	private Set<CustAddress> custAddresses;

	// bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;

	public CustContact() {
	}

	public int getContactId() {
		return this.contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
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

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<CustAddress> getCustAddresses() {
		return this.custAddresses;
	}

	public void setCustAddresses(Set<CustAddress> custAddresses) {
		this.custAddresses = custAddresses;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the businessPhone
	 */
	public String getBusinessPhone() {
		return businessPhone;
	}

	/**
	 * @param businessPhone
	 *            the businessPhone to set
	 */
	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	/**
	 * @return the personalPhone
	 */
	public String getPersonalPhone() {
		return personalPhone;
	}

	/**
	 * @param personalPhone
	 *            the personalPhone to set
	 */
	public void setPersonalPhone(String personalPhone) {
		this.personalPhone = personalPhone;
	}

}