package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the coverage database table.
 * 
 */
@Entity
@Table(name="coverage")
public class Coverage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COVERAGE_ID", unique=true, nullable=false)
	private int coverageId;

	@Column(name="COVERAGE_AMOUNT")
	private double coverageAmount;

	@Column(name="COVERAGE_CODE", length=25)
	private String coverageCode;

    @Lob()
	@Column(name="COVERAGE_DESCRIPTION")
	private String coverageDescription;

	@Column(name="DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name="DATE_UPDATED")
	private Timestamp dateUpdated;

	@Column(name="DEDUCTION_AMOUNT")
	private double deductionAmount;

	@Column(name="MAX_LIMIT")
	private int maxLimit;

	@Column(name="MIN_LIMIT")
	private int minLimit;

	//bi-directional many-to-one association to PolicyInfo
    @ManyToOne
	@JoinColumn(name="POLICY_CONTRACT_NUMBER")
	private PolicyInfo policyInfo;

    public Coverage() {
    }

	public int getCoverageId() {
		return this.coverageId;
	}

	public void setCoverageId(int coverageId) {
		this.coverageId = coverageId;
	}

	public double getCoverageAmount() {
		return this.coverageAmount;
	}

	public void setCoverageAmount(double coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public String getCoverageCode() {
		return this.coverageCode;
	}

	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}

	public String getCoverageDescription() {
		return this.coverageDescription;
	}

	public void setCoverageDescription(String coverageDescription) {
		this.coverageDescription = coverageDescription;
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

	public double getDeductionAmount() {
		return this.deductionAmount;
	}

	public void setDeductionAmount(double deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public int getMaxLimit() {
		return this.maxLimit;
	}

	public void setMaxLimit(int maxLimit) {
		this.maxLimit = maxLimit;
	}

	public int getMinLimit() {
		return this.minLimit;
	}

	public void setMinLimit(int minLimit) {
		this.minLimit = minLimit;
	}

	public PolicyInfo getPolicyInfo() {
		return this.policyInfo;
	}

	public void setPolicyInfo(PolicyInfo policyInfo) {
		this.policyInfo = policyInfo;
	}
	
}