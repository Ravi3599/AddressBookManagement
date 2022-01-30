package com.bridgelabz.addressbooksystemjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {
	
	private static AddressBookDBService addressbookDBService;
	private PreparedStatement addressBookDataStatement;

	private AddressBookDBService() {
		
	}
	
	public static AddressBookDBService getInstance() {
		if(addressbookDBService == null)
			addressbookDBService = new AddressBookDBService();
		return addressbookDBService;
	}
	
	
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
				int contactId = resultSet.getInt("contact_id");
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				long phoneNumber = resultSet.getLong("phone_number");
				String email = resultSet.getString("email");
				String city = resultSet.getString("city"); 
				String state = resultSet.getString("state"); 
				long zipCode = resultSet.getLong("zip"); 
				contactList.add(new ContactPerson(contactId, firstName, lastName, email, phoneNumber, city, state, zipCode));
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
	
	private void preparedStatementForContactData() {
		
		try {
			Connection connection = this.getConnection();
			String sqlStatement = "SELECT * FROM contact JOIN address ON contact.address_id = address.address_id WHERE first_name = ?;";
			addressBookDataStatement = connection.prepareStatement(sqlStatement);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
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
	
	public int updateContactData(String firstName, String lastName) {
		
		return this.updateContactDataUsingStatement(firstName,lastName);
	}	

	public int updateContactDataUsingStatement(String firstName, String lastName) {
		
		String sqlStatement = String.format("UPDATE contact SET last_name = %s WHERE first_name = '%s';", lastName, firstName);
		
		try (Connection connection = getConnection()){
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sqlStatement);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<ContactPerson> getContactDataUsingName(String firstName) {
		
		List<ContactPerson> contactList = null;
		if(this.addressBookDataStatement == null)
			this.preparedStatementForContactData();
		try {
			addressBookDataStatement.setString(1,firstName);
			ResultSet resultSet = addressBookDataStatement.executeQuery();
			contactList = this.getContactDetails(resultSet);	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public List<ContactPerson> getContactsBasedOnStartDateUsingPreparedStatement(String startDate, String endDate) {
		
		String sqlStatement = String.format("SELECT * FROM contact JOIN address ON contact.address_id = address.address_id WHERE date_added BETWEEN '%s' AND '%s';",startDate, endDate);
		List<ContactPerson> contactList = new ArrayList<>();
				
		try (Connection connection = getConnection();){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			contactList = this.getContactDetails(resultSet);
		}
		catch(SQLException exception){
			exception.printStackTrace();
		}
		return contactList;
	}
	
	public ContactPerson addNewContactToContacts(int contactId, String firstName, String lastName, long phoneNumber, String email, int addressId, String city, String state, long zip, String dateAdded) {
		
		int id = -1;
		Connection connection = null;
		ContactPerson contactPerson = null;
		
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		}
		catch(SQLException exception) {
			exception.printStackTrace();
		}
		try (Statement statement = connection.createStatement()){
			
			String sql = String.format("INSERT INTO contact (contact_id, first_name, last_name, phone_number, email, address_id, date_added) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s');", contactId, firstName, lastName, phoneNumber, email, addressId, dateAdded);
			
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next())
					id = resultSet.getInt(1);
			}
			contactPerson = new ContactPerson(id, firstName, lastName, email, phoneNumber, city, state, zip);
			
		}
		catch(SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return contactPerson;
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
		
		try(Statement statement = connection.createStatement()){

			String sqlQuery = String.format("INSERT INTO address VALUES ('%s', '%s', '%s', '%s')",addressId, city, state, zip);
			int rowAffected = statement.executeUpdate(sqlQuery);
			if (rowAffected == 1) {
				contactPerson = new ContactPerson(contactId, firstName, lastName, email, phoneNumber, city, state, zip);
			}			
		}
		catch(SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
		
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(connection != null)
				try {
					connection.close();
				} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return contactPerson;
	}
}