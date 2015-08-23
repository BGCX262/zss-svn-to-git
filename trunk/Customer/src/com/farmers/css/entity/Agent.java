package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the agent database table.
 * 
 */
@Entity
@Table(name="agent")
public class Agent implements Serializable {
 
	@Id
	@Column(name="AGENT_ID", unique=true, nullable=false)
	private int agentId;

	@Column(name="DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name="DATE_UPDATED")
	private Timestamp dateUpdated;

	@Column(name="FIRST_NAME", length=45)
	private String firstName;

	@Column(name="LAST_NAME", length=45)
	private String lastName;

	@Column(name="MIDDLE_NAME", length=45)
	private String middleName;

	@Column(length=25)
	private String upn;

	//bi-directional many-to-one association to PolicyInfo
    @ManyToOne
	@JoinColumn(name="POLICY_CONTRACT_NUMBER")
	private PolicyInfo policyInfo;

	//bi-directional many-to-one association to AgentContact
	@OneToMany(mappedBy="agent", fetch=FetchType.EAGER)
	private Set<AgentContact> agentContacts;

    public Agent() {
    }

	public int getAgentId() {
		return this.agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
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

	public String getUpn() {
		return this.upn;
	}

	public void setUpn(String upn) {
		this.upn = upn;
	}

	public PolicyInfo getPolicyInfo() {
		return this.policyInfo;
	}

	public void setPolicyInfo(PolicyInfo policyInfo) {
		this.policyInfo = policyInfo;
	}
	
	public Set<AgentContact> getAgentContacts() {
		return this.agentContacts;
	}

	public void setAgentContacts(Set<AgentContact> agentContacts) {
		this.agentContacts = agentContacts;
	}
	
}