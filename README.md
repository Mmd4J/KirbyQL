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
//the third arguement is being used for if its primary key or not and the 4th one is for not updating the object on update function
        InsertDataFunction fun =
                DBObject < String > name = DBObject.of("name", "TEXT", false);

        DBObject<String> id = DBObject.of("id", "VARCHAR(36)", true, true);
        SQLTable table = database.createNewTable("workers", name, id);

//if you're using an object which is not primitive don't forget to add toString() at the end of the object
        InsertDataFunction insert = (InsertDataFunction) table.requestFunction(InsertDataFunction.class);
        UUID uid = UUID.randomUUID();
        insert.values(name.clone("Mmd"), uid.clone(id.toString())).execute();


//It will find out the objects that are primary key or not to be updated if you don't want to update your object do not
// enter your object in updateData method but if you want to find the object via primary keys enter those and check
//doNotUpdate on constructor creation.

        UpdateDataFunction update = (UpdateDataFunction) table.requestFunction(UpdateDataFunction.class);
        update.values(name.clone("Mmd"), uid.clone(UUID.randomUUID().toString())).execute();

        SelectFunction select = (SelectFunction) table.requestFunction(SelectFunction.class);
        SQLResult sql = select.where("worker", id.clone(myId)).execute();

//SQLResult class does contain the result set but make sure to use SQLResult.close(); if your job was done.
    }
}
```
