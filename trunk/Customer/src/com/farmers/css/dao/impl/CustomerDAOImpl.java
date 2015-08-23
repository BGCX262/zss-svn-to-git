/**
 * 
 */
package com.farmers.css.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.farmers.css.dao.CustomerDAO;
import com.farmers.css.entity.CustContact;
import com.farmers.css.entity.Customer;

/**
 * @author Vasu
 * 
 */
@Repository("CustomerRepository")
public class CustomerDAOImpl implements CustomerDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3422616284119896049L;
	private EntityManager entityManager = null;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {

		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.farmers.css.dao.CustomerDAO#loadCustomerByID(java.lang.Integer)
	 */
	@Override
	public Customer loadCustomerByID(Integer customerId) {

		return entityManager.find(Customer.class, customerId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.farmers.css.dao.CustomerDAO#loadAllCusomers(java.lang.Integer,
	 * boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> loadAllCustomers() {

		String sqlQuery = "select c from Customer c";
		return (List<Customer>) entityManager.createQuery(sqlQuery)
				.getResultList();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.farmers.css.dao.CustomerDAO#saveCustomer(com.farmers.css.entity.Customer
	 * )
	 */
	@Override
	public boolean saveCustomer(Customer newCustomer) {

		try {
			entityManager.persist(newCustomer);
			return true;

		} catch (Exception e) {

		}

		return true;
	}

	@Override
	public Customer updateCustomer(Customer customerToUpdate) {

		return entityManager.merge(customerToUpdate);

	}

	@Override
	public CustContact updateCustomerContact(CustContact custContact) {

		return entityManager.merge(custContact);

	}

}