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
	var addressBook = AddressBook(ArrayList())

	if (addressBookDocument != null) {
		addressBook = ADSBackendTest1().loadAddressBook(addressBookDocument)
	}

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