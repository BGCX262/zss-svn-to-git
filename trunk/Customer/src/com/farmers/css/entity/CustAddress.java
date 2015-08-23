package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the cust_address database table.
 * 
 */
@Entity
@Table(name="cust_address")
public class CustAddress implements Serializable {
	
	@Id
	@Column(name="ADDRESS_ID", unique=true, nullable=false)
	private int addressId;

	@Column(length=20)
	private String city;

	@Column(length=60)
	private String country;

	@Column(name="COUNTY_OR_DISTRICT", length=30)
	private String countyOrDistrict;

	@Column(name="DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name="DATE_UPDATED")
	private Timestamp dateUpdated;

	@Column(length=2)
	private String state;

	@Column(name="STREET_ADDR1", length=45)
	private String streetAddr1;

	@Column(name="STREET_ADDR2", length=25)
	private String streetAddr2;

	@Column(name="STREET_NUMBER", length=10)
	private String streetNumber;

	@Column(length=15)
	private String zipcode;

	//bi-directional many-to-one association to CustContact
    @ManyToOne
	@JoinColumn(name="CONTACT_ID")
	private CustContact custContact;

    public CustAddress() {
    }

	public int getAddressId() {
		return this.addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountyOrDistrict() {
		return this.countyOrDistrict;
	}

	public void setCountyOrDistrict(String countyOrDistrict) {
		this.countyOrDistrict = countyOrDistrict;
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

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreetAddr1() {
		return this.streetAddr1;
	}

	public void setStreetAddr1(String streetAddr1) {
		this.streetAddr1 = streetAddr1;
	}

	public String getStreetAddr2() {
		return this.streetAddr2;
	}

	public void setStreetAddr2(String streetAddr2) {
		this.streetAddr2 = streetAddr2;
	}

	public String getStreetNumber() {
		return this.streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public CustContact getCustContact() {
		return this.custContact;
	}

	public void setCustContact(CustContact custContact) {
		this.custContact = custContact;
	}
	
}