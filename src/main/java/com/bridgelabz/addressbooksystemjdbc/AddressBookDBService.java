package com.bridgelabz.addressbooksystemjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {
	
	private Connection getConnection() throws SQLException {
		
		String jdbcURL = "jdbc:mysql://localhost:3306/addressbook_service?useSSL=false";
		String userName = "root";
		String password = "Bridgelabz@1234";
		Connection connection;
		
		System.out.println("Connecting to the database : "+jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is Succcessfully Established!! "+connection);
		
		return connection;
	}
	
	private List<ContactPerson> getContactDetails(ResultSet resultSet) {
		
		List<ContactPerson> contactList = new ArrayList<>();
		
		try {
			while(resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				long phoneNumber = resultSet.getLong("phone_number");
				String email = resultSet.getString("email");
				String city = resultSet.getString("city"); 
				String state = resultSet.getString("state"); 
				long zipCode = resultSet.getLong("zip"); 
				contactList.add(new ContactPerson(firstName, lastName, email, phoneNumber, city, state, zipCode));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return contactList;
		
	}
	
	private Map<Integer,String> getAddressDetails(ResultSet resultSet) {
		
		Map<Integer,String> addressBookList  = new HashMap<>();
		
		try {
			while(resultSet.next()) {
				int addressBookId = resultSet.getInt("addressbook_id");
				String addressBookName = resultSet.getString("addressbook_name");
				addressBookList.put(addressBookId, addressBookName);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return addressBookList;
		
	}

	public List<ContactPerson> readContactDetails() {
		
		String sqlStatement = "SELECT * FROM contact JOIN address ON contact.address_id = address.address_id;";
		List<ContactPerson> contactsList = new ArrayList<>();
				
		try (Connection connection = getConnection();){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			contactsList = getContactDetails(resultSet);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return contactsList;
	}
	
	public Map<Integer, String> readAddressDetails() {
		
		String sqlStatement = "SELECT * FROM address_book;";
		Map<Integer,String> addressBookList  = new HashMap<>();
				
		try (Connection connection = getConnection();){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			addressBookList = getAddressDetails(resultSet);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return addressBookList;
	}

	public List<ContactPerson> getContactDetailsBasedOnCityUsingStatement(String city) {
		
		String sqlStatement = String.format("SELECT * FROM contact JOIN address ON contact.address_id = address.address_id WHERE city = '%s';",city);
		List<ContactPerson> contactList = new ArrayList<>();
				
		try (Connection connection = getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			contactList = getContactDetails(resultSet);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return contactList;
	}

	public List<ContactPerson> getContactDetailsBasedOnStateUsingStatement(String state) {
		
		String sqlStatement = String.format("SELECT * FROM contact JOIN address ON contact.address_id = address.address_id WHERE state = '%s';",state);
		List<ContactPerson> contactList = new ArrayList<>();
				
		try (Connection connection = getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			contactList = getContactDetails(resultSet);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return contactList;
	}

	public List<Integer> getCountOfEmployeesBasedOnCityUsingStatement() {
		
		String sqlStatement = "SELECT city, COUNT(contact_id) AS COUNT_BY_CITY FROM contact JOIN address ON contact.address_id = address.address_id GROUP BY city;";
		List<Integer> countBasedOnCity = new ArrayList<>();
				
		try (Connection connection = getConnection();){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			while(resultSet.next()) {
				int count = resultSet.getInt("COUNT_BY_CITY");
				countBasedOnCity.add(count);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return countBasedOnCity;
	}
	
	public List<Integer> getCountOfEmployeesBasedOnStateUsingStatement() {
		
		String sqlStatement = "SELECT state, COUNT(contact_id) As COUNT_BY_STATE FROM contact JOIN address ON contact.address_id = address.address_id GROUP BY state;";
		List<Integer> countBasedOnState = new ArrayList<>();
				
		try (Connection connection = getConnection();){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			while(resultSet.next()) {
				int count = resultSet.getInt("COUNT_BY_STATE");
				countBasedOnState.add(count);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return countBasedOnState;
	}
}