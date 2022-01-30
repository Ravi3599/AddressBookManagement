package com.bridgelabz.addressbooksystemjdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddressBookTest {
	
	static AddressBook addressBook = new AddressBook();
	
	@BeforeClass
	public static void createAddressBookObject() {
		addressBook.setAddressBookName("book1");
	}
	
	@Test
	public void givenDetails_ShouldAddToContactList() {
		
		ContactPerson person = new ContactPerson();
		addressBook.addContact("Ram", new ContactPerson("Ram","Shram","ram@gmail.com",762069,"Pune","Maharshtra",012345));
		addressBook.addContact("Aakash", new ContactPerson("Aakash","Murtyi","aakash@gmail.com",654466,"Nagpur","Maharshtra",545449));
		int listSize = addressBook.contactList.size();
		Assert.assertEquals(2, listSize);
		
	}
	
	@Test
	public void given2Contacts_WhenWrittenToFile_ShouldMatchEntries()
	{
		AddressBookFileIO addressFileIO = new AddressBookFileIO();
		addressFileIO.writeToAddressBookFile("book1.txt", addressBook.contactList);
		addressFileIO.printData("book1.txt");
		long entries = addressFileIO.countEntries("book1.txt");
		Assert.assertEquals(2, entries);
		
	}
	
	@Test
	public void givenFile_WhenRead_ShouldReturnNumberOfEntries() {
		
		AddressBookFileIO addressFileIO = new AddressBookFileIO();
		List<String> entries = addressFileIO.readDataFromFile("book1.txt");
		long countEntries = entries.size();
		Assert.assertEquals(2, countEntries);
	}
	@Test
	public void givenAddressBookInDB_ShouldReturnCountOfBasedOnCity() {
		
		AddressBookDirectory employeePayrollService = new AddressBookDirectory();
		List<Integer> expectedCountBasedOnGender = new ArrayList();
		expectedCountBasedOnGender.add(1);
		expectedCountBasedOnGender.add(2);
		expectedCountBasedOnGender.add(1);
		expectedCountBasedOnGender.add(1);
		expectedCountBasedOnGender.add(1);
		expectedCountBasedOnGender.add(1);
		List<Integer> maximumSalaryBasedOnGender = employeePayrollService.getCountOfEmployeesBasedOnCity(IOService.DB_IO);
		if(maximumSalaryBasedOnGender.size() == 6) {
			Assert.assertEquals(expectedCountBasedOnGender, maximumSalaryBasedOnGender);
		}
	}
	
	@Test
	public void givenAddressBookInDB_ShouldReturnCountOfBasedOnState() {
		
		AddressBookDirectory employeePayrollService = new AddressBookDirectory();
		List<Integer> expectedCountBasedOnGender = new ArrayList();
		expectedCountBasedOnGender.add(1);
		expectedCountBasedOnGender.add(4);
		expectedCountBasedOnGender.add(2);
		List<Integer> maximumSalaryBasedOnGender = employeePayrollService.getCountOfEmployeesBasedOnState(IOService.DB_IO);
		if(maximumSalaryBasedOnGender.size() == 3) {
			Assert.assertEquals(expectedCountBasedOnGender, maximumSalaryBasedOnGender);
		}
	}
	@Test 
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
		
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		List<ContactPerson> employeePayrollData = addressBookDirectory.readContactDetails(IOService.DB_IO);
		addressBookDirectory.updateContactLastName("Rosa", "Ramirez");
		
		boolean result = addressBookDirectory.checkContactDetailsInSyncWithDB("Rosa");
		Assert.assertTrue(result);
		
	}
	@Test
	public void givenStartDateRange_WhenMatchesUsingPreparedStatement_ShouldReturnEmployeeDetails() {
		
		String startDate = "2013-01-01";
		String endDate = "2021-01-01";
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		List<ContactPerson> contactData = addressBookDirectory.getContactsBasedOnStartDateUsingPreparedStatement(IOService.DB_IO, startDate, endDate);
		Assert.assertEquals(5, contactData.size());
	}
	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWithUpdatedDB() {
		
		String dateAdded = "2017-02-12";
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		addressBookDirectory.readContactDetails(IOService.DB_IO);
		addressBookDirectory.addContactToUpdatedDatabse(8, "Amy", "Gonzales", 123456789, "amy@gmail.com", 6, "Kodagu", "Karnataka", 345567, dateAdded);

		boolean result = addressBookDirectory.checkContactDetailsInSyncWithDB("Amy");
		Assert.assertTrue(result);
		
	}
	
}