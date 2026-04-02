╔══════════════════════════════════════════════════════════════════════════════╗
║    VEHICLE SERVICE MANAGEMENT SYSTEM — Complete Setup & Interview Guide      ║
║    Hyundai Motor Company | Internship Project Documentation                  ║
╚══════════════════════════════════════════════════════════════════════════════╝

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  PROJECT FOLDER STRUCTURE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

VehicleServiceMS/
│
├── sql/
│   └── schema.sql                  ← Database schema + sample data
│
├── lib/
│   └── mysql-connector-j-8.x.jar   ← Download this (see Step 2)
│
└── src/
    └── com/hyundai/vsms/
        │
        ├── model/                   ← POJOs (mirror of DB tables)
        │   ├── Customer.java
        │   ├── Vehicle.java
        │   └── ServiceRecord.java
        │
        ├── util/                    ← Shared utilities
        │   ├── DBConnection.java
        │   └── ConsoleHelper.java
        │
        ├── dao/                     ← Database Access Layer
        │   ├── CustomerDAO.java
        │   ├── VehicleDAO.java
        │   └── ServiceDAO.java
        │
        ├── service/                 ← Business Logic Layer
        │   ├── CustomerService.java
        │   ├── VehicleService.java
        │   └── ServiceRecordService.java
        │
        └── app/
            └── Main.java            ← Entry point / Controller

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  STEP-BY-STEP SETUP GUIDE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

STEP 1: Install Prerequisites
──────────────────────────────
  • Java JDK 17+   → https://adoptium.net
  • MySQL 8.0+     → https://dev.mysql.com/downloads/
  • IDE (optional) → IntelliJ IDEA Community (recommended) or Eclipse

  Verify installation:
    java -version
    mysql --version

STEP 2: Download MySQL JDBC Driver
────────────────────────────────────
  1. Go to: https://dev.mysql.com/downloads/connector/j/
  2. Download "Platform Independent" ZIP
  3. Extract the .jar file (mysql-connector-j-X.X.X.jar)
  4. Place it in VehicleServiceMS/lib/ folder

STEP 3: Set Up MySQL Database
───────────────────────────────
  1. Open MySQL command line or MySQL Workbench
  2. Login: mysql -u root -p
  3. Run the schema file:
       source /path/to/VehicleServiceMS/sql/schema.sql
     OR paste contents of schema.sql into MySQL Workbench and execute

  4. Verify tables were created:
       USE hyundai_vsms;
       SHOW TABLES;
       SELECT * FROM customers;

STEP 4: Configure Database Credentials
────────────────────────────────────────
  Open: src/com/hyundai/vsms/util/DBConnection.java

  Change these two lines to match your MySQL setup:
    private static final String USER     = "root";   ← your MySQL username
    private static final String PASSWORD = "root";   ← your MySQL password

STEP 5: Compile the Project
─────────────────────────────
  Open terminal in the VehicleServiceMS/ directory.

  On Windows:
    javac -cp "lib\mysql-connector-j-8.x.jar" -d out -sourcepath src src\com\hyundai\vsms\app\Main.java

  On Mac/Linux:
    javac -cp "lib/mysql-connector-j-8.x.jar" -d out -sourcepath src src/com/hyundai/vsms/app/Main.java

  (Replace 8.x with your actual jar version)

STEP 6: Run the Application
──────────────────────────────
  On Windows:
    java -cp "out;lib\mysql-connector-j-8.x.jar" com.hyundai.vsms.app.Main

  On Mac/Linux:
    java -cp "out:lib/mysql-connector-j-8.x.jar" com.hyundai.vsms.app.Main

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  USING IntelliJ IDEA (Easier Method)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  1. Open IntelliJ → File → New → Project from Existing Sources
  2. Select the VehicleServiceMS folder
  3. File → Project Structure → Libraries → Add JAR
     → Select mysql-connector-j-X.X.X.jar
  4. Right-click Main.java → Run 'Main.main()'

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  EXAMPLE APPLICATION OUTPUTS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

MAIN MENU:
  ┌─────────────────────────────────────────────┐
  │                  MAIN MENU                  │
  ├─────────────────────────────────────────────┤
  │  CUSTOMERS                                  │
  │   [1] Add New Customer                      │
  │   [2] View All Customers                    │
  │   [3] Search Customer by Name               │
  ├─────────────────────────────────────────────┤
  │  VEHICLES                                   │
  │   [4] Add Vehicle to Customer               │
  │   [5] View Vehicles by Customer             │
  ├─────────────────────────────────────────────┤
  │  SERVICE RECORDS                            │
  │   [6] Add Service Record                    │
  │   [7] View Service History (by Vehicle)     │
  │   [8] View Service History (by Customer)    │
  ├─────────────────────────────────────────────┤
  │  REPORTS                                    │
  │   [9]  Total Revenue Report                 │
  │   [10] Most Frequent Service Types          │
  │   [11] Customer Satisfaction Report         │
  │   [12] Monthly Revenue Breakdown            │
  ├─────────────────────────────────────────────┤
  │   [0] Exit                                  │
  └─────────────────────────────────────────────┘

OPTION 2 — ALL CUSTOMERS:
  ID    Name                   Phone          Email                     City
  ──────────────────────────────────────────────────────────────────────
  1     Arjun Mehta            9876543210     arjun@email.com           Nashik
  2     Priya Sharma           9812345678     priya@email.com           Pune
  3     Ravi Kumar             9823456789     ravi@email.com            Mumbai
  ℹ  Total customers: 5

OPTION 7 — SERVICE HISTORY FOR VEHICLE 1:
  Service [ID: 1  | Vehicle: 1 | Date: 2024-01-15 | Type: Oil Change            | Cost: ₹1500.00  | Hours: 1.5 | Rating: ★★★★★]
  Service [ID: 2  | Vehicle: 1 | Date: 2024-03-20 | Type: Tyre Rotation         | Cost: ₹800.00   | Hours: 1.0 | Rating: ★★★★☆]
  Service [ID: 3  | Vehicle: 1 | Date: 2024-06-10 | Type: Full Service          | Cost: ₹5500.00  | Hours: 5.0 | Rating: ★★★★★]

OPTION 9 — TOTAL REVENUE:
  ┌────────────────────────────────────────┐
  │  Total Revenue Generated : ₹36300.00  │
  └────────────────────────────────────────┘

OPTION 10 — MOST FREQUENT SERVICE TYPES:
  ┌──────────────────────────────┬──────────────┐
  │ Service Type                 │ Times Done   │
  ├──────────────────────────────┼──────────────┤
  │ Oil Change                   │   3  ███     │
  │ Full Service                 │   3  ███     │
  │ Tyre Rotation                │   1  █       │
  │ Brake Inspection             │   1  █       │
  │ AC Service                   │   1  █       │
  └──────────────────────────────┴──────────────┘

OPTION 11 — CUSTOMER SATISFACTION:
  ┌──────────────────────┬────────────┬─────────────┬───────────────────┐
  │ Customer Name        │ Avg Rating │ # Services  │ Total Spent       │
  ├──────────────────────┼────────────┼─────────────┼───────────────────┤
  │ Priya Sharma         │ 5.00  ████ │           2 │ ₹8500.00          │
  │ Arjun Mehta          │ 4.40  ████ │           5 │ ₹10800.00         │
  └──────────────────────┴────────────┴─────────────┴───────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  HOW TO PRESENT THIS PROJECT IN AN INTERVIEW
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

30-SECOND ELEVATOR PITCH:
  "I built a Vehicle Service Management System simulating an
  automobile service center at Hyundai. The project uses Core Java
  with JDBC for database connectivity, MySQL as the backend, and
  follows a layered MVC-style architecture with Model, DAO, Service,
  and Controller layers. I implemented CRUD operations for customers,
  vehicles, and service records — plus reporting features using
  advanced SQL with JOINs, GROUP BY, and aggregate functions like
  SUM, AVG, and COUNT."

KEY POINTS TO HIGHLIGHT:
  ✔ Layered Architecture (Model → DAO → Service → Controller)
  ✔ PreparedStatements for SQL injection prevention
  ✔ Business rule validation in the Service layer
  ✔ Multi-table JOINs and aggregate queries
  ✔ BigDecimal for currency (shows precision awareness)
  ✔ Singleton pattern for DB connection management
  ✔ LocalDate for modern Java date handling

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  COMMON INTERVIEW QUESTIONS & ANSWERS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Q1: Why did you use PreparedStatement instead of Statement?
    Answer: PreparedStatement prevents SQL injection — a common
    security vulnerability where malicious input can break or
    manipulate SQL queries. It also pre-compiles the SQL, which
    can improve performance for repeated queries.

Q2: Why use BigDecimal for cost instead of double or float?
    Answer: float and double use binary floating point, which
    causes rounding errors with decimal values. For example,
    0.1 + 0.2 ≠ 0.3 in double arithmetic. BigDecimal provides
    exact decimal arithmetic, which is essential for financial data.

Q3: What is the DAO pattern and why did you use it?
    Answer: DAO (Data Access Object) separates database operations
    from business logic. This means if we switch from MySQL to
    PostgreSQL or MongoDB, only the DAO layer changes — the
    Service and Controller layers remain untouched.

Q4: What is the difference between Statement and PreparedStatement?
    Answer: Statement compiles SQL every time it runs. PreparedStatement
    compiles once and reuses the plan. PreparedStatement also safely
    handles parameters via ? placeholders, preventing injection.

Q5: Explain the SQL JOIN query you used.
    Answer: I used an INNER JOIN to fetch all service records for
    a customer by joining three tables — customers joins to vehicles
    on customer_id, and vehicles joins to services on vehicle_id.
    This lets me query data that's split across related tables
    without duplicating data in any single table.

Q6: What is a Foreign Key? Why is it important here?
    Answer: A Foreign Key enforces referential integrity. In our
    schema, vehicle.customer_id references customers.customer_id,
    which ensures you cannot add a vehicle for a non-existent
    customer, and if a customer is deleted, their vehicles
    (and service records) are automatically cleaned up via CASCADE.

Q7: What design pattern did you use for the DB connection?
    Answer: Singleton pattern — the DBConnection class has a private
    constructor and a static method that returns the same Connection
    object every time. This prevents creating multiple connections
    which wastes resources.

Q8: How would you improve this project?
    Answer: For production I would:
    1. Replace the Singleton connection with HikariCP connection pool
    2. Add Spring Boot for REST APIs instead of console UI
    3. Add JUnit unit tests for the Service layer
    4. Add Spring Security for authentication
    5. Use Spring Data JPA instead of raw JDBC
    6. Add logging with SLF4J/Logback instead of System.out

Q9: What is N+1 query problem and does your project have it?
    Answer: N+1 means fetching 1 parent record and then N separate
    queries for children. In my project, when viewing service history
    by customer, I used a single JOIN query instead of looping and
    querying per vehicle — which avoids the N+1 problem.

Q10: What are ACID properties in databases?
     Answer: Atomicity (all or nothing), Consistency (valid state
     before and after), Isolation (concurrent transactions don't
     interfere), Durability (committed data persists). MySQL's
     InnoDB engine (which we use) supports all four ACID properties.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  ADVANCED SQL QUERIES REFERENCE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

1. Service history across all customer vehicles (3-table JOIN):
   SELECT s.*
   FROM services s
   JOIN vehicles v ON s.vehicle_id = v.vehicle_id
   WHERE v.customer_id = 1
   ORDER BY s.service_date DESC;

2. Most frequent service types (GROUP BY + COUNT):
   SELECT service_type, COUNT(*) AS service_count
   FROM services
   GROUP BY service_type
   ORDER BY service_count DESC;

3. Revenue + rating per customer (3-table JOIN + multiple aggregates):
   SELECT c.name,
          ROUND(AVG(s.rating), 2) AS avg_rating,
          COUNT(s.service_id)     AS total_services,
          SUM(s.cost)             AS total_spent
   FROM customers c
   JOIN vehicles v ON c.customer_id = v.customer_id
   JOIN services s ON v.vehicle_id  = s.vehicle_id
   GROUP BY c.customer_id, c.name
   ORDER BY avg_rating DESC;

4. Monthly revenue breakdown (MONTH() + GROUP BY):
   SELECT MONTHNAME(service_date) AS month,
          COUNT(*)                 AS jobs,
          SUM(cost)               AS revenue
   FROM services
   WHERE YEAR(service_date) = YEAR(CURDATE())
   GROUP BY MONTH(service_date), MONTHNAME(service_date)
   ORDER BY MONTH(service_date);

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  TECHNOLOGIES USED (for Resume)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  • Java 17        — Core language
  • JDBC           — Database connectivity
  • MySQL 8.0      — Relational database
  • SQL            — JOINs, GROUP BY, Aggregations
  • MVC Pattern    — Layered architecture
  • Design Patterns — Singleton (DB connection)

  Resume bullet point example:
  "Developed a Vehicle Service Management System for an automobile
   service center domain using Java, JDBC, and MySQL, implementing
   layered MVC architecture with DAO pattern, PreparedStatements for
   security, and advanced SQL queries (JOINs, GROUP BY, aggregations)
   for business reporting."
