package com.bridgelabz.addressbookmanagementsystem;

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
	
	
}