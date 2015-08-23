package com.farmers.css.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.farmers.css.entity.CustAddress;
import com.farmers.css.entity.CustContact;
import com.farmers.css.entity.Customer;
import com.farmers.css.entity.PolicyInfo;
import com.farmers.css.services.CustomerService;

@ManagedBean(name = "customerBean")
@SessionScoped
public class CustomerBean implements Serializable {

	private static final long serialVersionUID = 5927862954534658299L;

	@ManagedProperty("#{customerService}")
	private CustomerService customerService;

	private String lastLoginTime;
	private List<String> supportedLanguages;
	private String customerLanguage = "en";

	/**
	 * @return the supportedLanguages
	 */
	public List<String> getSupportedLanguages() {
		return supportedLanguages;
	}

	/**
	 * @param supportedLanguages
	 *            the supportedLanguages to set
	 */
	public void setSupportedLanguages(List<String> supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}

	/**
	 * @return the customerLanguage
	 */
	public String getCustomerLanguage() {
		return customerLanguage;
	}

	/**
	 * @param customerLanguage
	 *            the customerLanguage to set
	 */
	public void setCustomerLanguage(String customerLanguage) {
		this.customerLanguage = customerLanguage;
	}

	/**
	 * @return the lastLoginTime
	 */
	public String getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime
	 *            the lastLoginTime to set
	 */
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the custAddressSet
	 */
	public Set<CustAddress> getCustAddressSet() {
		return custAddressSet;
	}

	/**
	 * @param custAddressSet
	 *            the custAddressSet to set
	 */
	public void setCustAddressSet(Set<CustAddress> custAddressSet) {
		this.custAddressSet = custAddressSet;
	}

	private boolean loadFromDB = true;
	private String customerId;
	private Customer customer;
	private Set<PolicyInfo> policyInfos = null;
	private CustContact custContact = null;
	private Set<CustAddress> custAddressSet = null;

	/**
	 * @return the loadFromDB
	 */
	public boolean isLoadFromDB() {
		return loadFromDB;
	}

	/**
	 * @param loadFromDB
	 *            the loadFromDB to set
	 */
	public void setLoadFromDB(boolean loadFromDB) {
		this.loadFromDB = loadFromDB;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the custContact
	 */
	public CustContact getCustContact() {
		return custContact;
	}

	/**
	 * @param custContact
	 *            the custContact to set
	 */
	public void setCustContact(CustContact custContact) {
		this.custContact = custContact;
	}

	/**
	 * @return the policyInfos
	 */
	public Set<PolicyInfo> getPolicyInfos() {
		return policyInfos;
	}

	/**
	 * @param policyInfos
	 *            the policyInfos to set
	 */
	public void setPolicyInfos(Set<PolicyInfo> policyInfos) {
		this.policyInfos = policyInfos;
	}

	/**
	 * @return the custAddressList
	 */
	public Set<CustAddress> getCustAddressList() {
		return custAddressSet;
	}

	/**
	 * @param custAddressList
	 *            the custAddressList to set
	 */
	public void setCustAddressList(Set<CustAddress> custAddressSet) {
		this.custAddressSet = custAddressSet;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the customerService
	 */
	public CustomerService getCustomerService() {
		return customerService;
	}

	/**
	 * @param customerService
	 *            the customerService to set
	 */
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String saveCustomer() {

		return null;

	}

	public String loadCustomer() {
		if (customer == null) {
			this.customer = customerService.getCustomer(Integer
					.valueOf(customerId));
			loadFromDB = false;

			if (customer.getDateUpdated() != null) {
				lastLoginTime = DateFormat.getDateTimeInstance(DateFormat.LONG,
						DateFormat.LONG).format(customer.getDateUpdated());
			}
			// Populate Polciy infos
			if (customer.getPolicyInfos() != null) {
				this.policyInfos = new HashSet<PolicyInfo>(customer
						.getPolicyInfos().size());

				for (PolicyInfo policyInfo : customer.getPolicyInfos()) {

					policyInfos.add(policyInfo);
				}
			}

			// populate Cust contact

			custContact = customer.getCustomerContact();

			if (custContact != null && custContact.getCustAddresses() != null) {
				this.custAddressSet = new HashSet<CustAddress>(custContact
						.getCustAddresses().size());

				for (CustAddress customerAddress : custContact
						.getCustAddresses()) {

					this.custAddressSet.add(customerAddress);

				}

			}

			//custContact.setBusinessPhone("1234566789");
			customer.setCustomerContact(custContact);

			Customer customerReturn = customerService
					.updateCustomer(this.customer);

			System.out.println("New Values are ****************"
					+ customerReturn.getCustomerContact().getBusinessPhone());

		}

		return "/index";
	}

	public String updateCustomer() {

		customer = customerService.updateCustomer(customer);

		return "/pages/selfService/selfService";
	}

	public String updateCustomerContact() {

		custContact = customerService.updateCustomerContact(custContact);

		return "/pages/selfService/selfService";
	}

	public String signOut() {
		customer.setDateUpdated(new Timestamp(new Date().getTime()));
		customerService.updateCustomer(customer);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();

		HttpSession session = (HttpSession) ectx.getSession(false);
		session.invalidate();

		return "/login";
	}

	public String changelanguage() {

		Locale locale = new Locale(customerLanguage.toLowerCase());
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);

		return null;
	}

}