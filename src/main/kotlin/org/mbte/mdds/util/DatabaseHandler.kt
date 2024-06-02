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
            val sql = "CREATE TABLE Contacts (customer_id TEXT PRIMARY KEY, company_name TEXT NOT NULL, name TEXT NOT NULL" +
                    ", title TEXT NOT NULL, address TEXT NOT NULL, city TEXT NOT NULL, email TEXT NOT NULL, region TEXT, zip TEXT" +
                    ", country TEXT NOT NULL, phone TEXT NOT NULL, fax TEXT NOT NULL);"
            statement.executeUpdate(sql)
        }
    }

    fun insertContact(contact: Contact) {
        getConnection()?.use { connection ->
            // Creates values for contact inputed
            val contactId = contact.id
            val contactCompanyName = contact.companyName
            val contactName = contact.name
            val contactTitle = contact.title
            val contactAddress = contact.address
            val contactCity = contact.city
            val contactEmail = contact.email
            val contactRegion = contact.region
            val contactZip = contact.zip
            val contactCountry = contact.country
            val contactPhone = contact.phone
            val contactFax = contact.fax

            val statement = connection.createStatement()
            // Inserts new contacts with prepared statement using values
            val sql = "INSERT INTO Contacts (customer_id, company_name, name, title, address, city, email, region, country, phone, fax)" +
                    "VALUES (customer_id=$contactId, company_name=$contactCompanyName, name=$contactName, title=$contactTitle" +
                    ", address=$contactAddress, city=$contactCity, email=$contactEmail, region=$contactRegion, zip=$contactZip" +
                    ", country=$contactCountry, phone=$contactPhone, fax=$contactFax);"
			kotlin.runCatching { statement.executeUpdate(sql) }
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