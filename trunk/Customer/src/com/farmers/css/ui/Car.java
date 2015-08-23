package com.farmers.css.ui;

import java.io.Serializable;

public class Car implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5865647146922574310L;
	private String color;
	private String manufacturer;
	private int year;
	private String model;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Car(String randomModel, int randomYear, String randomManufacturer,
			String randomColor) {
		this.model = randomModel;
		this.year = randomYear;
		this.manufacturer = randomManufacturer;
		this.color = randomColor;
	}

}
