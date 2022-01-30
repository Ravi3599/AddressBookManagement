package com.bridgelabz.addressbooksystemjdbc;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByName;

public class ContactPerson {
	
	@CsvBindByName(column = "First Name")
	private String firstName;
	
	@CsvBindByName(column = "Last Name")
	private String lastName;
	
	@CsvBindByName(column = "Email")
	private String email;
	
	@CsvBindByName(column = "Phone Number")
	private long phoneNumber;
	
	private LocalDate dateAdded;
	
	public Address address = new Address();

	public ContactPerson(String firstName, String lastName, String email, long phoneNumber, String city, String state, long zip) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address.setCity(city);
		this.address.setState(state);
		this.address.setZip(zip);
	}
	
	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}

	public ContactPerson() {
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public long getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	
	@Override
	public String toString() {
		
		return "First Name - "+firstName+", Last Name - "+lastName+", Phone Number - "+phoneNumber+", Email - "+email+", City - "+address.getCity()+", State - "+address.getState()+", Zip Code - "+address.getZip();
	}
	
	@Override
	public boolean equals(Object object) {
		
		if(this == object)
			return true;
		
		if(object == null || getClass() != object.getClass())
			return false;
		
		ContactPerson that = (ContactPerson) object;
		return firstName.equals(that.firstName) 
				&& lastName.equals(that.lastName) 
				&& Long.compare(that.phoneNumber,  phoneNumber) == 0 
				&& email.equals(that.email) 
				&& address.getCity().equals(that.address.getCity()) 
				&& address.getState().equals(that.address.getState()) 
				&& Long.compare(that.address.getZip(), address.getZip()) == 0;
	}
}