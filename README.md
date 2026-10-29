# Vehicle Showroom Management System

A console-based Java application using JDBC and MySQL to manage a vehicle showroom's stock with full CRUD operations.

## Features
- Add, view, search, update and delete vehicles
- View only available vehicles
- Mark a vehicle as sold

## Tech stack
Java, JDBC, MySQL

## Setup
1. Install JDK and MySQL.
2. Run `schema.sql` in MySQL Workbench.
3. Download MySQL Connector/J from https://dev.mysql.com/downloads/connector/j/ and put the `.jar` in a `lib` folder.
4. Set your MySQL password in the `PASS` line of `VehicleShowroom.java`.
5. Compile and run:

```
javac -cp ".;lib\*" VehicleShowroom.java
java -cp ".;lib\*" VehicleShowroom
```

On Linux/Mac, use `:` instead of `;`.