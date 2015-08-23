/**
 * 
 */
package com.farmers.css.dao;

import java.io.Serializable;
import java.util.List;

import com.farmers.css.entity.CustContact;
import com.farmers.css.entity.Customer;

/**
 * @author Vasu
 * 
 */
public interface CustomerDAO extends Serializable {

	public Customer loadCustomerByID(Integer customerId);

	public boolean saveCustomer(Customer newCustomer);

	public List<Customer> loadAllCustomers();

	public Customer updateCustomer(Customer customerToUpdate);

	public CustContact updateCustomerContact(CustContact custContact);

}
