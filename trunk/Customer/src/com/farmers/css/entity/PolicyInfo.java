package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * The persistent class for the policy_info database table.
 * 
 */
@Entity
@Table(name = "policy_info")
public class PolicyInfo implements Serializable {

	@Id
	@Column(name = "POLICY_CONTRACT_NUMBER", unique = true, nullable = false)
	private long policyContractNumber;

	@Column(name = "POLICY_NICKNAME")
	private String policyNickname;

	@Column(length = 15)
	private String aor;

	@Column(name = "DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name = "DATE_UPDATED")
	private Timestamp dateUpdated;

	@Column(name = "EXPIRY_DATE")
	private Timestamp expiryDate;

	@Column(length = 15)
	private String lob;

	@Column(name = "POLICY_STATUS", length = 15)
	private String policyStatus;

	@Column(name = "SOURCE_SYSTEM", length = 30)
	private String sourceSystem;

	private int term;

	@Column(length = 10)
	private String type;

	// bi-directional many-to-one association to Agent
	@OneToMany(mappedBy = "policyInfo", fetch = FetchType.EAGER)
	private Set<Agent> agents;

	// bi-directional many-to-one association to Billing
	@OneToMany(mappedBy = "policyInfo", fetch = FetchType.EAGER)
	private Set<Billing> billings;

	// bi-directional many-to-one association to Coverage
	@OneToMany(mappedBy = "policyInfo", fetch = FetchType.EAGER)
	private Set<Coverage> coverages;

	// bi-directional many-to-one association to Document
	@OneToMany(mappedBy = "policyInfo", fetch = FetchType.EAGER)
	private Set<Document> documents;

	// bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;

	// bi-directional many-to-one association to Property
	@OneToMany(mappedBy = "policyInfo", fetch = FetchType.EAGER)
	private Set<Property> properties;

	// bi-directional many-to-one association to Vehicle
	@OneToMany(mappedBy = "policyInfo", fetch = FetchType.EAGER)
	private Set<Vehicle> vehicles;

	public PolicyInfo() {
	}

	public long getPolicyContractNumber() {
		return this.policyContractNumber;
	}

	public void setPolicyContractNumber(long policyContractNumber) {
		this.policyContractNumber = policyContractNumber;
	}

	public String getAor() {
		return this.aor;
	}

	public void setAor(String aor) {
		this.aor = aor;
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

	public Timestamp getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getLob() {
		return this.lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getPolicyStatus() {
		return this.policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public String getSourceSystem() {
		return this.sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public int getTerm() {
		return this.term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Agent> getAgents() {
		return this.agents;
	}

	public void setAgents(Set<Agent> agents) {
		this.agents = agents;
	}

	public Set<Billing> getBillings() {
		return this.billings;
	}

	public void setBillings(Set<Billing> billings) {
		this.billings = billings;
	}

	public Set<Coverage> getCoverages() {
		return this.coverages;
	}

	public void setCoverages(Set<Coverage> coverages) {
		this.coverages = coverages;
	}

	public Set<Document> getDocuments() {
		return this.documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Property> getProperties() {
		return this.properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}

	public Set<Vehicle> getVehicles() {
		return this.vehicles;
	}

	public void setVehicles(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	/**
	 * @return the policyNickname
	 */
	public String getPolicyNickname() {
		return policyNickname;
	}

	/**
	 * @param policyNickname
	 *            the policyNickname to set
	 */
	public void setPolicyNickname(String policyNickname) {
		this.policyNickname = policyNickname;
	}

}