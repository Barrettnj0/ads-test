import org.junit.jupiter.api.Test
import org.mbte.mdds.tests.Contact
import org.mbte.mdds.util.DatabaseHandler
import java.io.File
import kotlin.test.assertEquals

class BackendTestMethods {

    @Test
    fun testEmptyTable() {
        val dbFile = File("src/test/resources/addressbook.sqlite")
        val dbHandler = DatabaseHandler("jdbc:sqlite:${dbFile.absolutePath}")
        dbHandler.initContactsTable()

        val actualContactList = dbHandler.getAllContacts()

        assertEquals(ArrayList<Contact>(), actualContactList)
        dbFile.delete()
    }

    @Test
    fun testDuplicates() {
        val dbFile = File("src/test/resources/addressbook.sqlite")
        val dbHandler = DatabaseHandler("jdbc:sqlite:${dbFile.absolutePath}")
        dbHandler.initContactsTable()

        dbHandler.insertContact(testContact1)
        dbHandler.insertContact(testContact1)

        val actualContactList = dbHandler.getAllContacts()

        assertEquals(testContactList1,actualContactList)
        dbFile.delete()
    }

    @Test
    fun testNullEntries() {
        val dbFile = File("src/test/resources/addressbook.sqlite")
        val dbHandler = DatabaseHandler("jdbc:sqlite:${dbFile.absolutePath}")
        dbHandler.initContactsTable()

        dbHandler.insertContact(testContact2)

        val actualContactList = dbHandler.getAllContacts()

        assertEquals(testContactList2,actualContactList)
        dbFile.delete()
    }

    val testContact1 = Contact(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "1",
        "I",
        "2",
        "3"
    )

    val testContact2 = Contact(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        null,
        null,
        "I",
        "2",
        null
    )

    val testContactList1 = listOf(testContact1, testContact1)
    val testContactList2 = listOf(testContact2)
}