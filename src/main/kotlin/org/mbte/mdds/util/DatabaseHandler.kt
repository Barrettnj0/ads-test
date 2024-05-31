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
            val sql = "TODO" //TODO
            statement.executeUpdate(sql)
        }
    }

    fun insertContact(contact: Contact) {
        getConnection()?.use { connection ->
            val statement = connection.createStatement()
            val sql = "TODO" //TODO
			kotlin.runCatching { statement.executeUpdate(sql) }
				.onFailure { 
					System.err.println("Failed to execute SQL: $sql")
					it.printStackTrace()
				}
            
        }
    }
    
    fun getAllContacts(): List<Contact> {
		getConnection()?.use { connection ->
            val statement = connection.createStatement()
            val sql = "TODO"
			val result = kotlin.runCatching { statement.executeQuery(sql) }
			return if (result.isFailure) {
				System.err.println("Failed to execute SQL: $sql")
				result.exceptionOrNull()?.printStackTrace()
				emptyList()
			} else {
				//TODO
				emptyList()
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