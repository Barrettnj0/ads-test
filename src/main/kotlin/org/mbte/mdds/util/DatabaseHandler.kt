package org.mbte.mdds.util

import org.mbte.mdds.tests.Contact
import java.sql.Connection
import java.sql.DriverManager


class DatabaseHandler(private val url: String) {
	init {
        // Register the SQLite JDBC driver
        Class.forName("org.sqlite.JDBC")
    }

    fun initContactsTable() {
        getConnection()?.use { connection ->
            val statement = connection.createStatement()
            // Create Contacts table
            // NOTE: We don't use primary key for customer_id since we want to allow duplicate contacts!
            val sql = "CREATE TABLE Contacts (customer_id TEXT NOT NULL, company_name TEXT NOT NULL, name TEXT NOT NULL" +
                    ", title TEXT NOT NULL, address TEXT NOT NULL, city TEXT NOT NULL, email TEXT NOT NULL, region TEXT, zip TEXT" +
                    ", country TEXT NOT NULL, phone TEXT NOT NULL, fax TEXT);"
            statement.executeUpdate(sql)
        }
    }

    fun insertContact(contact: Contact) {
        getConnection()?.use { connection ->
            // Inserts new contacts with prepared statement using values
            val sql = "INSERT INTO Contacts (customer_id, company_name, name, title, address, city, email, region, zip, country, phone, fax)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            val statement = connection.prepareStatement(sql)
            statement.setString(1, contact.id)
            statement.setString(2, contact.companyName)
            statement.setString(3, contact.name)
            statement.setString(4, contact.title)
            statement.setString(5, contact.address)
            statement.setString(6, contact.city)
            statement.setString(7, contact.email)
            statement.setString(8, contact.region)
            statement.setString(9, contact.zip)
            statement.setString(10, contact.country)
            statement.setString(11, contact.phone)
            statement.setString(12, contact.fax)
			kotlin.runCatching { statement.executeUpdate() }
				.onFailure { 
					System.err.println("Failed to execute SQL: $sql")
					it.printStackTrace()
				}
            
        }
    }
    
    fun getAllContacts(): List<Contact> {
		getConnection()?.use { connection ->
            val contacts = ArrayList<Contact>()
            val statement = connection.createStatement()
            val sql = "SELECT * FROM Contacts;"
			val result = kotlin.runCatching { statement.executeQuery(sql) }
			return if (result.isFailure) {
				System.err.println("Failed to execute SQL: $sql")
				result.exceptionOrNull()?.printStackTrace()
				emptyList()
			} else {
                val resultSet = result.getOrThrow()
				while (resultSet.next()) {
                    val contact = Contact(resultSet.getString("customer_id"), resultSet.getString("company_name")
                        , resultSet.getString("name"), resultSet.getString("title")
                        , resultSet.getString("address"), resultSet.getString("city")
                        , resultSet.getString("email"), resultSet.getString("region")
                        , resultSet.getString("zip"), resultSet.getString("country")
                        , resultSet.getString("phone"), resultSet.getString("fax"))
                    contacts.add(contact)
                }
                return contacts
			}
        }
        return emptyList()
	}

    private fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(url)
        } catch (e: Exception) {
            System.err.println("Failed to get connection to SQLite!!")
            e.printStackTrace()
            null
        }
    }
}