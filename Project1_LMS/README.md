Library Management System
This project is a simple Library Management System developed using Java Swing for the graphical user interface, JDBC for database connectivity, and MySQL as the backend database. It allows librarians or administrators to manage book inventory, register students, and handle book issuance and returns.

Features
Book Management:

Add new books with details like title, author, ISBN, publisher, publication year, and quantity.

View a list of all books in the library.

Update existing book details (title, author, etc., and total quantity).

Delete books (if not currently issued).

Student Management:

Add new students with details like name, student ID card, contact number, and email.

View a list of all registered students.

Delete students (if they have no outstanding issued books).

Transaction Management:

Issue books to registered students.

Return issued books.

View all currently issued books.

Data Persistence: All data is stored and managed in a MySQL database.

User-Friendly Interface: A basic Swing GUI for easy interaction.

Technologies Used
Backend Language: Java

Database Connectivity: JDBC (Java Database Connectivity)

Database: MySQL

Graphical User Interface: Java Swing

Database Setup
Before running the application, you need to set up the MySQL database.

Install MySQL Server: If you don't have MySQL installed, download and install it from the official MySQL website.

Create Database: Open your MySQL client (e.g., MySQL Workbench, command-line client) and execute the following SQL commands to create the database and tables:

-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS library_db;

-- Use the database
USE library_db;

-- Table for Books
CREATE TABLE IF NOT EXISTS books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    publisher VARCHAR(255),
    publication_year INT,
    quantity INT NOT NULL DEFAULT 0,
    available_quantity INT NOT NULL DEFAULT 0
);

-- Table for Students (Users)
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    student_id_card VARCHAR(20) UNIQUE NOT NULL,
    contact_number VARCHAR(15),
    email VARCHAR(255)
);

-- Table for Transactions (Issue/Return)
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    student_id INT NOT NULL,
    issue_date DATE NOT NULL,
    return_date DATE,
    status ENUM('issued', 'returned') NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- Optional: Add some sample data
INSERT INTO books (title, author, isbn, publisher, publication_year, quantity, available_quantity) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Scribner', 1925, 5, 5),
('1984', 'George Orwell', '9780451524935', 'Signet Classic', 1949, 3, 3),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Grand Central Publishing', 1960, 7, 7);

INSERT INTO students (name, student_id_card, contact_number, email) VALUES
('Alice Smith', 'S001', '123-456-7890', 'alice.s@example.com'),
('Bob Johnson', 'S002', '987-654-3210', 'bob.j@example.com');

Project Setup (Java)
Create a New Java Project:

Open your preferred Java IDE (IntelliJ IDEA, Eclipse, NetBeans).

Create a new Java project (e.g., a simple Java project or a Maven/Gradle project).

Create Package Structure:

Inside your project's src (or src/main/java) folder, create the following package structure:

com.library.jdbc

com.library.model

com.library.service

com.library.gui

You can usually do this by right-clicking on src -> New -> Package and typing the full package name (e.g., com.library.jdbc).

Add Java Files:

Place the provided Java source files into their respective packages:

DBConnection.java -> com.library.jdbc

Book.java -> com.library.model

Student.java -> com.library.model

Transaction.java -> com.library.model

LibraryManager.java -> com.library.service

LibraryApp.java -> com.library.gui

Add MySQL JDBC Driver:

Download: Download the MySQL Connector/J (JDBC driver) JAR file from the official MySQL website (https://dev.mysql.com/downloads/connector/j/). Choose the "Platform Independent" ZIP or TAR.GZ. Extract it and find the .jar file (e.g., mysql-connector-j-8.x.x.jar).

Add to Project:

IntelliJ IDEA: Go to File -> Project Structure -> Modules -> Dependencies tab. Click the + button, select JARs or Directories..., and navigate to the downloaded MySQL Connector/J .jar file. Click OK and Apply.

Eclipse: Right-click on your project in the Package Explorer -> Properties -> Java Build Path -> Libraries tab. Click Add External JARs... and select the downloaded MySQL Connector/J .jar file. Click Apply and Close.

Maven/Gradle (if applicable): If you are using Maven or Gradle, add the following dependency to your pom.xml (for Maven) or build.gradle (for Gradle):

<!-- Maven (pom.xml) -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.28</version> <!-- Use the version you downloaded -->
</dependency>
```gradle
// Gradle (build.gradle)
implementation 'mysql:mysql-connector-java:8.0.28' // Use the version you downloaded

Update Database Credentials:

Open com/library/jdbc/DBConnection.java.

Modify the URL, USER, and PASSWORD constants to match your MySQL server configuration:

private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "your_mysql_username"; // e.g., "root"
private static final String PASSWORD = "your_mysql_password"; // e.g., "password"

How to Run
Ensure your MySQL server is running.

In your IDE, navigate to com/library/gui/LibraryApp.java.

Right-click on LibraryApp.java and select Run 'LibraryApp.main()'.

The Library Management System GUI application should now launch.

Usage
The application has three main tabs:

Book Management: Use this tab to add new books, view the entire book inventory, update book details (select a row and modify fields), and delete books.

Student Management: Use this tab to register new students, view all students, and delete student records.

Transactions: Use this tab to issue books to students (provide Book ID and Student ID) and return books (provide Transaction ID and Book ID). You can also view all currently issued books.

Future Enhancements
User Authentication: Implement login for different roles (e.g., librarian, student).

Search Functionality: Add robust search capabilities for books and students.

Reporting: Generate reports on issued books, overdue books, popular books, etc.

Error Handling: More sophisticated error handling and user feedback.

Advanced GUI: Improve the user interface with better design, validation, and more interactive elements.

Borrowing Limits: Implement rules for the maximum number of books a student can borrow.

Fines: Add a system for calculating and managing fines for overdue books.

Book Reservation: Allow students to reserve books that are currently unavailable.
