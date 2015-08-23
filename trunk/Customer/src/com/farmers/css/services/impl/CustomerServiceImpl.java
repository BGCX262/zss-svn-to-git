/**
 * 
 */
package com.farmers.css.services.impl;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.farmers.css.dao.CustomerDAO;
import com.farmers.css.entity.CustAddress;
import com.farmers.css.entity.CustContact;
import com.farmers.css.entity.Customer;
import com.farmers.css.services.CustomerService;

/**
 * @author Vasu
 * 
 */
@Service("customerService")
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
public class CustomerServiceImpl implements CustomerService, Serializable {

	@Resource(name = "CustomerRepository")
	private CustomerDAO customerRepository;

	public void setCustomerRepository(CustomerDAO customerRepository) {
		customerRepository = customerRepository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean saveCustomer(Customer customer) {

		return customerRepository.saveCustomer(customer);

	}

	public Customer getCustomer(Integer customerId)

	{
		return customerRepository.loadCustomerByID(customerId);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Customer updateCustomer(Customer customerToUpdate) {
		// TODO Auto-generated method stub
		return customerRepository.updateCustomer(customerToUpdate);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CustContact updateCustomerContact(CustContact customerContact) {

		return customerRepository.updateCustomerContact(customerContact);
	}

	@Override
	public Set<CustAddress> updateCustomerAddress(
			Set<CustAddress> CustomerAddressSet) {
		// TODO Auto-generated method stub
		return null;
	}

}