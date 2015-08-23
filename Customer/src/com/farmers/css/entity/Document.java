package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the documents database table.
 * 
 */
@Entity
@Table(name="documents")
public class Document implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOCUMENT_ID", unique=true, nullable=false)
	private int documentId;

	@Column(name="DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name="DATE_UPDATED")
	private Timestamp dateUpdated;

    @Lob()
	@Column(name="DOCUMENT_DESCRIPTION")
	private String documentDescription;

	@Column(name="DOCUMENT_REFERENCE_ID", length=500)
	private String documentReferenceId;

	//bi-directional many-to-one association to PolicyInfo
    @ManyToOne
	@JoinColumn(name="POLICY_CONTRACT_NUMBER")
	private PolicyInfo policyInfo;

    public Document() {
    }

	public int getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
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

	public String getDocumentDescription() {
		return this.documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public String getDocumentReferenceId() {
		return this.documentReferenceId;
	}

	public void setDocumentReferenceId(String documentReferenceId) {
		this.documentReferenceId = documentReferenceId;
	}

	public PolicyInfo getPolicyInfo() {
		return this.policyInfo;
	}

	public void setPolicyInfo(PolicyInfo policyInfo) {
		this.policyInfo = policyInfo;
	}
	
}