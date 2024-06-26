package org.mbte.mdds.tests

import org.json.JSONObject
import org.mbte.mdds.util.DatabaseHandler
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

fun main(args: Array<String>) {
	val test = ADSBackendTest1()
	val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	val userHome = File(System.getProperty("user.home"))
	val time = dateFormatter.format(LocalDateTime.now()).replace(" ", "_").replace(":", ";")
	// I changed this to a sqlite file extension here as I assume we want to use sqlite to query and view db
	val dbFile = File(userHome, "${test.javaClass.simpleName}-$time.sqlite")
	dbFile.createNewFile()
	val dbHandler = DatabaseHandler("jdbc:sqlite:${dbFile.absolutePath}")
	dbHandler.initContactsTable()

	// Create json path and file for each application start
	val jsonFile = File("src/main/resources/addressbook.json")
	jsonFile.createNewFile()

	// Create Document object based off of xml file from file path
	val addressBookDocument = ADSBackendTest1().loadXml("src/main/resources/ab.xml")

	// Instantiate an AddressBook variable to be replaced
	var addressBook = AddressBook(ArrayList())

	// Use null check then replace instantiated addressBook var
	if (addressBookDocument != null) {
		addressBook = ADSBackendTest1().loadAddressBook(addressBookDocument)
	}

	// Iterate through xml contacts to fill SQLite table
	for (contact in addressBook.contacts) {
		dbHandler.insertContact(contact)
	}

	// Get new address book from db
	val dbAddressBook = AddressBook(dbHandler.getAllContacts())

	// Convert address book from database into json and store it in addressBookJson val
	val addressBookJson = ADSBackendTest1().convertToJson(dbAddressBook)

	// Use printOutput to print addressBookJson to jsonFile filepath
	ADSBackendTest1().printOutput(addressBookJson, jsonFile)

	println("Assessment complete.")
	println("Database file located at ${dbFile.absolutePath}")
	println("JSON output located at ${jsonFile.absolutePath}")
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
		val contactList = ArrayList<Contact>()

		doc.documentElement?.normalize()
		val root = doc.documentElement
		val nodeList = root?.childNodes

		var i = 0
		while (i < nodeList?.length!!) {
			val contactAttributeMap = LinkedHashMap<Int, String>()

			val node = nodeList.item(i)
			if (node.nodeType == Document.ELEMENT_NODE) {
				val element = node as Element

				val attributes = element.childNodes
				var j = 1
				while (j < attributes.length) {
					val attribute = attributes.item(j)

					when (attribute.nodeName) {
						"CustomerID" -> contactAttributeMap[0] = attribute.textContent
						"CompanyName" -> contactAttributeMap[1] = attribute.textContent
						"ContactName" -> contactAttributeMap[2] = attribute.textContent
						"ContactTitle" -> contactAttributeMap[3] = attribute.textContent
						"Address" -> contactAttributeMap[4] = attribute.textContent
						"City" -> contactAttributeMap[5] = attribute.textContent
						"Email" -> contactAttributeMap[6] = attribute.textContent
						"Region" -> contactAttributeMap[7] = attribute.textContent
						"PostalCode" -> contactAttributeMap[8] = attribute.textContent
						"Country" -> contactAttributeMap[9] = attribute.textContent
						"Phone" -> contactAttributeMap[10] = attribute.textContent
						"Fax" -> contactAttributeMap[11] = attribute.textContent
					}
					j += 2
				}

				contactList.add(Contact(
					contactAttributeMap[0].toString(),
					contactAttributeMap[1].toString(),
					contactAttributeMap[2].toString(),
					contactAttributeMap[3].toString(),
					contactAttributeMap[4].toString(),
					contactAttributeMap[5].toString(),
					contactAttributeMap[6].toString(),
					contactAttributeMap[7],
					contactAttributeMap[8],
					contactAttributeMap[9].toString(),
					contactAttributeMap[10].toString(),
					contactAttributeMap[11]
				))
			}
			i += 1
		}

		return AddressBook(contactList)
	}

	override fun convertToJson(addressBook: AddressBook): JSONObject {
		val addressBookJson = JSONObject()
		addressBookJson.put("contactList", addressBook.contacts)
		return addressBookJson
	}

	override fun printOutput(json: JSONObject, output: File) { output.writeText(json.toString()) }

}