package org.mbte.mdds.tests

import org.json.JSONObject
import org.mbte.mdds.util.DatabaseHandler
import org.w3c.dom.Document
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

fun main(args: Array<String>) {
	val test = ADSBackendTest1()
	val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	val userHome = File(System.getProperty("user.home"))
	val time = dateFormatter.format(LocalDateTime.now()).replace(" ", "_").replace(":", ";")
	val dbFile = File(userHome, "${test.javaClass.simpleName}-$time.db")
	dbFile.createNewFile()
	val dbHandler = DatabaseHandler("jdbc:sqlite:${dbFile.absolutePath}")
	dbHandler.initContactsTable()

	/**
	 * 1. Load the xml file 'ab.xml'
	 * 2. Load the address book contents from 'ab.xml'
	 * 3. Insert each contact into the Database
	 * 4. Retrieve all contacts from the Database
	 * 5. Convert the contacts from the Database into Json and write to file
	 */

	val addressBookDocument = ADSBackendTest1().loadXml("src/main/resources/ab.xml")

	
	println("Assessment complete.")
	println("Database file located at ${dbFile.absolutePath}")
//	println("JSON output located at ${output.absolutePath}")
}

data class AddressBook(val contacts: List<Contact>)
data class Contact(
	val id: String, 
	val companyName: String, 
	val name: String, 
	val title: String, 
	val address: String, 
	val city: String, 
	val email: String,
	val region: String?,
	val zip: String?, 
	val country: String, 
	val phone: String, 
	val fax: String?
)

interface AddressBookInterface {
	fun loadXml(fileName: String): Document?
	fun loadAddressBook(doc: Document): AddressBook
	fun convertToJson(addressBook: AddressBook): JSONObject
	fun printOutput(json: JSONObject, output: File)
}

class ADSBackendTest1(): AddressBookInterface {
	override fun loadXml(fileName: String): Document? {
		return DocumentBuilderFactory
			.newInstance()
			.newDocumentBuilder()
			.parse(File(fileName))
	}

	override fun loadAddressBook(doc: Document): AddressBook {
		TODO("Not yet implemented")
		
		// Take Document and convert each part to a Contact
		// Take each contact and load them into AddressBook
		// Return addressBook
		// NOTE: addressBook returned will be used to make iterative calls to insertContact() in db handler

	}

	override fun convertToJson(addressBook: AddressBook): JSONObject {
		TODO("Not yet implemented")
		// Take addressBook and convert to JSON
	}

	override fun printOutput(json: JSONObject, output: File) {
		TODO("Not yet implemented")
		// Take addressBook json and write to a file
	}

}