package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the property database table.
 * 
 */
@Entity
@Table(name="property")
public class Property implements Serializable {
	 
	@Id
	@Column(name="PROPERTY_ID", unique=true, nullable=false)
	private int propertyId;

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

	//bi-directional many-to-one association to PropertyAddress
	@OneToMany(mappedBy="property", fetch=FetchType.EAGER)
	private Set<PropertyAddress> propertyAddresses;

    public Property() {
    }

	public int getPropertyId() {
		return this.propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
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
	
	public Set<PropertyAddress> getPropertyAddresses() {
		return this.propertyAddresses;
	}

	public void setPropertyAddresses(Set<PropertyAddress> propertyAddresses) {
		this.propertyAddresses = propertyAddresses;
	}
	
}