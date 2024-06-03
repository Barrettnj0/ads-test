# ADS Backend Assessment
Complete the project TODO's to demonstrate an understanding of SQL, XML, JSON, and the JVM.

## Project Overview
The project includes an XML source ab.xml containing an AddressBook populated with Contacts. 
1. Design Contact SQL Schema and define table and CRUD statements
1. Load the XML file into memory
1. Parse the contents of the AddressBook
1. Insert Contacts from parsed AddressBook into SQLite
1. Retrieve all Contacts from SQLite and write them to a file.

## Requirements
1. Must include quality tests that verify functionality
1. SQL statements must be protected from SQL Injection
1. Must support insertion of a Contact that already exists

## Extra Credit
For additional credits:
1. Document all methods and classes
1. Utilize SQLDelight framework
1. Further optimize DatabaseHandler to be more performant
1. Add support for column addition to the Contact table
1. Use coroutines where applicable to improve performance


## Constraints
1. No new libraries shall be added to complete the assessment


## How to Use
1. Open build.gradle.kts file in an IDE of your choosing (IntelliJ is my choice)
2. Once project has compiled, run the ADSBackendTest1.kt file in ads-test/src/main/kotlin/org.mbte.mdds/tests
3. The output sqlite database file will be in your user folder and the output json file will be in the ads-test/src/main/resources folder

### Optional:
If you wish to input your own contacts to the address book, you can do so in the ab.xml file in ads-test/src/main/resources
NOTE: Any additions must be in xml format as done so already in the xml file.

