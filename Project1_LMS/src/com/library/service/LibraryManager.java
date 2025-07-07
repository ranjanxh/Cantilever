// src/main/java/com/library/service/LibraryManager.java
package com.library.service;

import com.library.jdbc.DBConnection;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all library operations, interacting with the database.
 */
public class LibraryManager {

    /**
     * Adds a new book to the database.
     *
     * @param book The Book object to add.
     * @return true if the book was added successfully, false otherwise.
     */
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, publisher, publication_year, quantity, available_quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getPublicationYear());
            pstmt.setInt(6, book.getQuantity());
            pstmt.setInt(7, book.getAvailableQuantity());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBookId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Book added successfully: " + book.getTitle());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing book's details.
     *
     * @param book The Book object with updated details.
     * @return true if the book was updated successfully, false otherwise.
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, publisher = ?, publication_year = ?, quantity = ?, available_quantity = ? WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getPublicationYear());
            pstmt.setInt(6, book.getQuantity());
            pstmt.setInt(7, book.getAvailableQuantity());
            pstmt.setInt(8, book.getBookId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book updated successfully: " + book.getTitle());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a book from the database by its ID.
     *
     * @param bookId The ID of the book to delete.
     * @return true if the book was deleted successfully, false otherwise.
     */
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book deleted successfully with ID: " + bookId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId The ID of the book to retrieve.
     * @return The Book object if found, null otherwise.
     */
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getString("publisher"),
                            rs.getInt("publication_year"),
                            rs.getInt("quantity"),
                            rs.getInt("available_quantity")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving book by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return The Book object if found, null otherwise.
     */
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getString("publisher"),
                            rs.getInt("publication_year"),
                            rs.getInt("quantity"),
                            rs.getInt("available_quantity")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving book by ISBN: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all books from the database.
     *
     * @return A list of all Book objects.
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("publisher"),
                        rs.getInt("publication_year"),
                        rs.getInt("quantity"),
                        rs.getInt("available_quantity")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    /**
     * Adds a new student to the database.
     *
     * @param student The Student object to add.
     * @return true if the student was added successfully, false otherwise.
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (name, student_id_card, contact_number, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getStudentIdCard());
            pstmt.setString(3, student.getContactNumber());
            pstmt.setString(4, student.getEmail());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setStudentId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Student added successfully: " + student.getName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a student from the database by their ID.
     *
     * @param studentId The ID of the student to delete.
     * @return true if the student was deleted successfully, false otherwise.
     */
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student deleted successfully with ID: " + studentId);
                return true;
            }
        } catch (SQLException e) {
            // Consider checking for SQLState to differentiate between general error and foreign key constraint violation
            if (e.getSQLState().startsWith("23")) { // SQLState for integrity constraint violation
                System.err.println("Error deleting student: Cannot delete student with active issued books. Please return all books first.");
            } else {
                System.err.println("Error deleting student: " + e.getMessage());
            }
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param studentId The ID of the student to retrieve.
     * @return The Student object if found, null otherwise.
     */
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("student_id"),
                            rs.getString("name"),
                            rs.getString("student_id_card"),
                            rs.getString("contact_number"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a student by their ID Card number.
     *
     * @param studentIdCard The ID Card number of the student to retrieve.
     * @return The Student object if found, null otherwise.
     */
    public Student getStudentByIdCard(String studentIdCard) {
        String sql = "SELECT * FROM students WHERE student_id_card = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentIdCard);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("student_id"),
                            rs.getString("name"),
                            rs.getString("student_id_card"),
                            rs.getString("contact_number"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student by ID Card: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all students from the database.
     *
     * @return A list of all Student objects.
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("student_id_card"),
                        rs.getString("contact_number"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Issues a book to a student.
     * Decrements available_quantity of the book and creates a transaction record.
     *
     * @param bookId    The ID of the book to issue.
     * @param studentId The ID of the student issuing the book.
     * @return true if the book was issued successfully, false otherwise.
     */
    public boolean issueBook(int bookId, int studentId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check if book is available
            Book book = getBookById(bookId);
            if (book == null || book.getAvailableQuantity() <= 0) {
                System.out.println("Book is not available or does not exist.");
                return false;
            }

            // 2. Check if student exists
            Student student = getStudentById(studentId);
            if (student == null) {
                System.out.println("Student does not exist.");
                return false;
            }

            // 3. Decrement available quantity
            String updateBookSql = "UPDATE books SET available_quantity = ? WHERE book_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateBookSql)) {
                pstmt.setInt(1, book.getAvailableQuantity() - 1);
                pstmt.setInt(2, bookId);
                pstmt.executeUpdate();
            }

            // 4. Record the transaction
            String insertTransactionSql = "INSERT INTO transactions (book_id, student_id, issue_date, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertTransactionSql)) {
                pstmt.setInt(1, bookId);
                pstmt.setInt(2, studentId);
                pstmt.setDate(3, new Date(System.currentTimeMillis())); // Current date
                pstmt.setString(4, "issued");
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            System.out.println("Book ID " + bookId + " issued to Student ID " + studentId + " successfully.");
            return true;

        } catch (SQLException e) {
            System.err.println("Error issuing book: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                    System.err.println("Transaction rolled back.");
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    DBConnection.closeConnection(conn);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit or closing connection: " + e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Returns a book from a student.
     * Increments available_quantity of the book and updates the transaction record.
     *
     * @param transactionId The ID of the transaction to update.
     * @param bookId        The ID of the book being returned.
     * @return true if the book was returned successfully, false otherwise.
     */
    public boolean returnBook(int transactionId, int bookId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Update the transaction record
            String updateTransactionSql = "UPDATE transactions SET return_date = ?, status = ? WHERE transaction_id = ? AND book_id = ? AND status = 'issued'";
            try (PreparedStatement pstmt = conn.prepareStatement(updateTransactionSql)) {
                pstmt.setDate(1, new Date(System.currentTimeMillis())); // Current date
                pstmt.setString(2, "returned");
                pstmt.setInt(3, transactionId);
                pstmt.setInt(4, bookId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("Transaction not found or already returned.");
                    conn.rollback();
                    return false;
                }
            }

            // 2. Increment available quantity of the book
            String updateBookSql = "UPDATE books SET available_quantity = available_quantity + 1 WHERE book_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateBookSql)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            System.out.println("Book ID " + bookId + " returned successfully for transaction ID " + transactionId + ".");
            return true;

        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                    System.err.println("Transaction rolled back.");
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    DBConnection.closeConnection(conn);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit or closing connection: " + e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Retrieves all issued transactions that have not yet been returned.
     *
     * @return A list of Transaction objects with status 'issued'.
     */
    public List<Transaction> getIssuedTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE status = 'issued'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("book_id"),
                        rs.getInt("student_id"),
                        rs.getDate("issue_date"),
                        rs.getDate("return_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving issued transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Retrieves all transactions for a specific student.
     *
     * @param studentId The ID of the student.
     * @return A list of Transaction objects for the given student.
     */
    public List<Transaction> getTransactionsByStudent(int studentId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getInt("book_id"),
                            rs.getInt("student_id"),
                            rs.getDate("issue_date"),
                            rs.getDate("return_date"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions for student: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return The Transaction object if found, null otherwise.
     */
    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transactionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getInt("book_id"),
                            rs.getInt("student_id"),
                            rs.getDate("issue_date"),
                            rs.getDate("return_date"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
