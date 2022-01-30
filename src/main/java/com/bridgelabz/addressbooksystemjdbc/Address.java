package com.bridgelabz.addressbooksystemjdbc;

import com.opencsv.bean.CsvBindByName;

public class Address {
	
	@CsvBindByName(column = "City")
	private String city;
	
	@CsvBindByName(column = "State")
	private String state;
	
	@CsvBindByName(column = "Zip Code")
	private long zip;

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public long getZip() {
		return zip;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZip(long zip) {
		this.zip = zip;
	}
	
	
}