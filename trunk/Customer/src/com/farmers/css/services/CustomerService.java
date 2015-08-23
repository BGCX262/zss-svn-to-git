package com.farmers.css.services;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.farmers.css.entity.CustAddress;
import com.farmers.css.entity.CustContact;
import com.farmers.css.entity.Customer;

public interface CustomerService extends Serializable {

	public boolean saveCustomer(Customer customerToSave);

	public Customer updateCustomer(Customer customerToUpdate);

	public Customer getCustomer(Integer customerId);

	public CustContact updateCustomerContact(CustContact customerContact);

	public Set<CustAddress> updateCustomerAddress(
			Set<CustAddress> CustomerAddressSet);
}