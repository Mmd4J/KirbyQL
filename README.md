# SQL API
This project is made for people who are bored of using sql so they can now rest while using the library. Its lightweight and easy to use. 
Feel free to contribiute in the project and also feel free to modify the project but please do not reshare this project to somewhere without naming me(MmdGIO) as the project author.

## Installation

**Gradle**:
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
    
```

```gradle
dependencies {
        implementation 'com.github.GaMeIsNtOvEr:SQLAPI:Tag'
}

```
**Maven**
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

```xml
	<dependency>
	    <groupId>com.github.GaMeIsNtOvEr</groupId>
	    <artifactId>SQLApi</artifactId>
	    <version>Tag</version>
	</dependency>
```
## Getting Started
To get started you need to create an instance from the database.
the Database class requires DatabaseInfo to get to know what type of database do we have and also some more details about how to connect to the database.
```java
// Getting the mysql information to connect.
Database mySqlInfo = new DatabaseInfo("database name","localhost",3306,"root","password");
// Creates a instance of MySQL MariaDB is only supported for now.
Database mySQL = new Database(mySqlInfo);

// Getting the database file which we want it to be used for SQLite
File myDBFile = new File("myDatabase.db");
// Checking if file doesnt exists so we create a new file.
if (!myDBFile.exists()) {
    try {
        myDBFile.createNewFile();
    } catch (IOException e){
        e.printStackTrace();
    }
}
DatabaseInfo sqlLiteInfo = new DatabaseInfo(myDBFile);
// Creates an instance of SQLite.
Database sqLite = new Database(new DatabaseInfo())
```
With these above you've learned how to create a MySQL/SQLite database. but now lets see how to basically create new table/insert/update/select some data.
```java
Database database = new Database(new DatabaseInfo("database name","localhost",3306,"root","password"));
//the third arguement is being used for if its primary key or not and the 4th one is for not updating the object on update function.

DBObject<String> name = DBObject.of("name","TEXT",false);

DBObject<String> id = DBObject.of("id","VARCHAR(36)",true,true);
database.createNewTable("workers",name,id);

//if you're using an object which is not primitive don't forget to add toString() at the end of the object

database.insertData("workers",name.clone("Mmd"),id.clone(UUID.randomUUID().toString()));

//It will find out the objects that are primary key or not to be updated if you don't want to update your object do not enter your object in updateData method but if you want to find the object via primary keys enter those and check doNotUpdate on constructor creation.

database.updateData("workers",name.clone("Mmd"),id.clone(UUID.randomUUID().toString())));

SQLResult sql = database.selectData("worker",id.clone(myId));
//SQLResult class does contain the result set but make sure to use SQLResult.close(); if your job was done.
```
