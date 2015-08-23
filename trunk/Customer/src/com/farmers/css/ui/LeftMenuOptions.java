package com.farmers.css.ui;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "leftMenuBean")
@SessionScoped
public class LeftMenuOptions implements Serializable {

	private int activeTab = 0;

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public void save(ActionEvent actionEvent) {
		addMessage("Data saved");
	}

	public void update(ActionEvent actionEvent) {
		addMessage("Data updated");
	}

	public void delete(ActionEvent actionEvent) {
		addMessage("Data deleted");
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String managePolicyNickname() {
		return "/pages/policyInfo/policyNickname";
	}

	public String showPolicySummary() {
		return "/pages/policyInfo/policyInfo";
	}

	public String manageOnlineProfile() {
		return "/pages/selfService/selfService";
	}
}
