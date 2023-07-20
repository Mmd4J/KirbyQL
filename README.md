# KirbyQL

KirbyQL is another light-weight orm which is really easier to use than other ones.
The basic idea comes from those days that I had to face sql everytime I had a new project.
So I decided to make a lightweight and also simple library to use on my projects.

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
the Database class requires DatabaseInfo to get to know what type of database do we have and also some more details
about how to connect to the database.

```java
import me.gameisntover.kirbyql.database.DatabaseInfo;

// Getting the mysql information to connect.
public class KirbyTest {
    public static void main(String[] args) {
        DatabaseInfo mySqlInfo = new DatabaseInfo("database name", "localhost", 3306, "root", "password");
        // Creates an instance of MySQL MariaDB is only supported for now.
        Database mySQL = new Database(mySqlInfo);

        // Getting the database file which we want it to be used for SQLite
        File myDBFile = new File("myDatabase.db");
        // Checking if file doesn't exist, so we create a new file.
        if (!myDBFile.exists()) {
            try {
                myDBFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DatabaseInfo sqlLiteInfo = new DatabaseInfo(myDBFile);
        // Creates an instance of SQLite.
        Database sqLite = new Database(new DatabaseInfo());

    }
}
```

With these above you've learned how to create a MySQL/SQLite database. but now lets see how to basically create new
table/insert/update/select/delete some data.

```java

import me.gameisntover.kirbyql.database.Database;
import me.gameisntover.kirbyql.database.function.InsertDataFunction;
import me.gameisntover.kirbyql.database.function.SelectFunction;
import me.gameisntover.kirbyql.database.function.UpdateDataFunction;
import me.gameisntover.kirbyql.database.table.SQLTable;

import java.util.UUID;

public class KirbyTest {
    public static void main(String[] args) {
        Database database = new Database(new DatabaseInfo("database name", "localhost", 3306, "root", "password"));
// Lets start by creating our table parameters the third parameter on the second DBObject constructor should be true if your object is primary key.
	DBObject < String > name = DBObject.of("name", "TEXT", false);
        DBObject<String> id = DBObject.of("id", "VARCHAR(36)", true, true);
// It is now time to create a new table and register the table parameters as you see the "worker" should be replaced with your table name.
        SQLTable table = database.createNewTable("workers", name, id);

//To insert data you gotta do something like this but also if you're using an object which is not primitive don't forget to add toString() at the end of the object
        InsertDataFunction insert = (InsertDataFunction) table.requestFunction(InsertDataFunction.class);
        UUID uid = UUID.randomUUID();
        insert.values(name.clone("Mmd"), uid.clone(id.toString())).execute();


// UpdateDataFunction allows you to update your datas. 
//It will find out the objects that are primary key or not to be updated if you don't want to update your object just dont add it in values. if you want to use your object as a key to find the value you need just drop your object on where() method

        UpdateDataFunction update = (UpdateDataFunction) table.requestFunction(UpdateDataFunction.class);
        update.values(name.clone("Mmd")).where(uid.clone(UUID.randomUUID().toString())).execute();

// SelectFunction will allows you to get your data.

        SelectFunction select = (SelectFunction) table.requestFunction(SelectFunction.class);
        SQLResult sql = select.where(id.clone(myId)).execute();

//SQLResult class does contain the result set but make sure to use SQLResult.close(); if your job was done.
    }
}
```
