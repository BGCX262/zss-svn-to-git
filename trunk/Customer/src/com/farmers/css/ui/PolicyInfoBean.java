/**
 * 
 */
package com.farmers.css.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.farmers.css.beans.CustomerBean;
import com.farmers.css.entity.PolicyInfo;

/**
 * @author USWVT9E
 * 
 */
@ManagedBean(name = "policyInfoBean")
@RequestScoped
public class PolicyInfoBean implements Serializable {

	private List<PolicyInfo> policyInfos;

	private PolicyInfo selectedPolicy;

	/**
	 * @return the selectedPolicy
	 */
	public PolicyInfo getSelectedPolicy() {
		return selectedPolicy;
	}

	/**
	 * @param selectedPolicy
	 *            the selectedPolicy to set
	 */
	public void setSelectedPolicy(PolicyInfo selectedPolicy) {
		this.selectedPolicy = selectedPolicy;
	}

	public List<PolicyInfo> getPolicyInfos() {
		return policyInfos;
	}

	public void setPolicyInfos(List<PolicyInfo> policyInfos) {
		this.policyInfos = policyInfos;
	}

	public PolicyInfoBean() {

		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();

		HttpSession session = (HttpSession) ectx.getSession(false);
		if (session != null) {
			CustomerBean customerBean = (CustomerBean) session
					.getAttribute("customerBean");

			if (customerBean.getPolicyInfos() != null) {
				policyInfos = new ArrayList<PolicyInfo>(customerBean
						.getPolicyInfos().size());

				for (PolicyInfo policyInfo : customerBean.getPolicyInfos()) {
					this.policyInfos.add(policyInfo);

				}

			}

		}

	}

	public String managePolicies() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();

		HttpSession session = (HttpSession) ectx.getSession(false);
		if (session != null) {
			CustomerBean customerBean = (CustomerBean) session
					.getAttribute("customerBean");

			if (customerBean.getPolicyInfos() != null) {
				policyInfos = new ArrayList<PolicyInfo>(customerBean
						.getPolicyInfos().size());

				for (PolicyInfo policyInfo : customerBean.getPolicyInfos()) {
					this.policyInfos.add(policyInfo);

				}
				return "/pages/policyInfo/policyInfo";
			}

		}
		return "/login";

	}
	
	
	public String  viewOrPayBill() {
		
		
		return "/index";
		
	}

}
