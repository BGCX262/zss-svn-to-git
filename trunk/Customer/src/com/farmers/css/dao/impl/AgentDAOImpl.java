package com.farmers.css.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.farmers.css.dao.AgentDAO;

@Repository("agentrepository")
public class AgentDAOImpl implements AgentDAO {
	
	private EntityManager entityManager = null;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {

		this.entityManager = entityManager;
	}

	
	
	

}
