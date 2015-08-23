package com.farmers.css.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the agent_contact database table.
 * 
 */
@Entity
@Table(name="agent_contact")
public class AgentContact implements Serializable {
	
	@Id
	@Column(name="CONTACT_ID", unique=true, nullable=false)
	private int contactId;

	@Column(name="BUSINESS_PHONE", length=12)
	private String businessPhone;

	@Column(name="DATE_CREATED")
	private Timestamp dateCreated;

	@Column(name="DATE_UPDATED")
	private Timestamp dateUpdated;

	@Column(name="EMAIL_ADDRESS", length=45)
	private String emailAddress;

	@Column(name="PERSONAL_PHONE", length=12)
	private String personalPhone;

	@Column(length=1)
	private String type;

	//bi-directional many-to-one association to AgentAddress
	@OneToMany(mappedBy="agentContact", fetch=FetchType.EAGER)
	private Set<AgentAddress> agentAddresses;

	//bi-directional many-to-one association to Agent
    @ManyToOne
	@JoinColumn(name="AGENT_ID")
	private Agent agent;

    public AgentContact() {
    }

	public int getContactId() {
		return this.contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getBusinessPhone() {
		return this.businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
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

	public String getPersonalPhone() {
		return this.personalPhone;
	}

	public void setPersonalPhone(String personalPhone) {
		this.personalPhone = personalPhone;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<AgentAddress> getAgentAddresses() {
		return this.agentAddresses;
	}

	public void setAgentAddresses(Set<AgentAddress> agentAddresses) {
		this.agentAddresses = agentAddresses;
	}
	
	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
}