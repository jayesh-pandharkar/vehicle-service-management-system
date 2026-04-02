# 🚗 Vehicle Service Management System (VSMS)

A **console-based backend system** simulating a real-world automobile service center, developed during my internship at Hyundai.

---

## 📌 Overview

The Vehicle Service Management System (VSMS) is designed to manage:

* Customers
* Vehicles
* Service Records
* Business Reports

It follows a **layered MVC-inspired architecture**:

```
Model → DAO → Service → Controller
```

---

## 🛠️ Tech Stack

* **Java 17**
* **JDBC**
* **MySQL 8**
* **SQL (JOINs, GROUP BY, Aggregations)**
* **MVC Architecture**
* **DAO Design Pattern**

---

## 📂 Project Structure

```
VehicleServiceMS/
│
├── sql/                 # Database schema
├── lib/                 # MySQL connector
└── src/com/hyundai/vsms/
    ├── model/           # POJOs
    ├── dao/             # Data Access Layer
    ├── service/         # Business Logic
    ├── util/            # Utilities
    └── app/Main.java    # Entry point
```

---

## ⚙️ Features

### 👤 Customer Management

* Add new customers
* View all customers
* Search customers by name

### 🚘 Vehicle Management

* Add vehicles to customers
* View vehicles by customer

### 🔧 Service Records

* Add service records
* View service history (by vehicle & customer)

### 📊 Reports

* Total revenue calculation
* Most frequent service types
* Customer satisfaction report
* Monthly revenue breakdown

---

## 🧠 Key Concepts Implemented

* ✔ Layered Architecture (MVC-inspired)
* ✔ DAO Pattern (Separation of concerns)
* ✔ PreparedStatement (SQL Injection prevention)
* ✔ Singleton Pattern (DB connection handling)
* ✔ Multi-table JOIN queries
* ✔ Aggregate SQL functions (SUM, AVG, COUNT)
* ✔ BigDecimal for financial accuracy
* ✔ Use of LocalDate for date handling

---

## 🗄️ Database Design

Relational schema with foreign keys:

* **Customers**
* **Vehicles** → linked via `customer_id`
* **Services** → linked via `vehicle_id`

Ensures **referential integrity and data consistency**

---

## 🚀 Setup Instructions

### 1. Prerequisites

* Java 17+
* MySQL 8+

### 2. Setup Database

Run:

```sql
source sql/schema.sql;
```

### 3. Configure DB Credentials

Update in:

```
DBConnection.java
```

### 4. Compile & Run

```bash
javac -cp "lib/mysql-connector-j-8.x.jar" -d out -sourcepath src src/com/hyundai/vsms/app/Main.java
java -cp "out:lib/mysql-connector-j-8.x.jar" com.hyundai.vsms.app.Main
```

---

## 📸 Sample Output

```
MAIN MENU
[1] Add Customer
[2] View Customers
...
```

---

## 🎯 What I Learned

* Designing real-world backend systems
* Writing optimized SQL queries with JOINs
* Applying clean architecture principles
* Secure database interaction using JDBC
* Handling relational data efficiently

---

## 💡 Future Improvements

* Convert to **Spring Boot REST API**
* Add **frontend (React)**
* Implement **authentication (Spring Security)**
* Add **unit testing (JUnit)**
* Use **connection pooling (HikariCP)**

---

## 🧑‍💻 Author

**Jayesh Pandharkar**

---


