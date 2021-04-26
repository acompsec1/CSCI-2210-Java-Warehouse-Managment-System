# CSCI-2210-Java-Warehouse-Managment-System
This project contains code for our final project for CSCI-2210 (Java)

Team members:
Andrew Mahr
Andrew Manfredi
Jake Intravaia
Spencer Wondolowski
Antonio Lopez


Team members and their roles:
Andrew Mahr - Team Head, Programmer
Andrew Manfredi - Teammate, Programmer
Jake Intravaia - Teammate, Programmer
Spencer Wondolowski - Teammate, Programmer
Antonio Lopez - Teammate, Programmer


## Needed Modules/Libraries
In order to run our code, the following libraries, files, and programs are needed:

JavaFX SDK: https://gluonhq.com/products/javafx/

Java MySQL Connector: https://dev.mysql.com/downloads/connector/j/

MySQL - https://www.mysql.com/downloads/

## IntelliJ Setup 
Our group used the IntelliJ IDE (https://www.jetbrains.com/idea/download/) to build and run our code. 

To setup and install JavaFX in IntelliJ, please use this link: https://www.jetbrains.com/help/idea/javafx.html

## Database Setup
To use our database that is currently in our repository, you can import the file into your MySQL server. Once the database is imported, you will need to edit the username, password, and, if neccessary, the database url in the [DatabaseConnector Class](DatabaseConnector.java) in order to connect properly to your server:

```bash
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/wmdb";

    private static final String DATABASE_USERNAME = "";
    private static final String DATABASE_PASSWORD = "";
```


## Running our Code
Finally, in order to run our code, you will need to run the [WarehouseManagement.java](WarehouseManagement.java) file.
