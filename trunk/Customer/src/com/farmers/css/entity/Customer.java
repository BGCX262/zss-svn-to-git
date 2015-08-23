package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * The persistent class for the customer database table.
 * 
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

	@Id
	@Column(name = "CUSTOMER_ID", unique = true, nullable = false)
	private int customerId;

	@Column(name = "DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name = "DATE_UPDATED")
	private Timestamp dateUpdated;

	@Column(name = "DRIVER_LICENSE", length = 10)
	private String driverLicense;

	@Column(name = "FIRST_NAME", length = 45)
	private String firstName;

	@Column(name = "LAST_NAME", length = 45)
	private String lastName;

	@Column(name = "MIDDLE_NAME", length = 5)
	private String middleName;

	@Column(name = "NAMED_INSURED", length = 1)
	private String namedInsured;

	@Column(length = 12)
	private String ssn;
	@Column(length = 40)
	private String password;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CONTACT_ID")
	private CustContact customerContact;

	// bi-directional many-to-one association to PolicyInfo
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	private Set<PolicyInfo> policyInfos;

	public Customer() {
	}

	/**
	 * @return the customerContact
	 */
	public CustContact getCustomerContact() {
		return customerContact;
	}

	/**
	 * @param customerContact
	 *            the customerContact to set
	 */
	public void setCustomerContact(CustContact customerContact) {
		this.customerContact = customerContact;
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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

	public String getDriverLicense() {
		return this.driverLicense;
	}

	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNamedInsured() {
		return this.namedInsured;
	}

	public void setNamedInsured(String namedInsured) {
		this.namedInsured = namedInsured;
	}

	public String getSsn() {
		return this.ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public Set<PolicyInfo> getPolicyInfos() {
		return this.policyInfos;
	}

	public void setPolicyInfos(Set<PolicyInfo> policyInfos) {
		this.policyInfos = policyInfos;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Customer) {
			Customer customer = (Customer) obj;
			if (this.customerId == customer.getCustomerId()
					&& this.driverLicense == customer.getDriverLicense()
					&& this.getSsn() == customer.getSsn()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Integer.valueOf(customerId).hashCode()
				+ driverLicense.hashCode() + ssn.hashCode();
	}

}