
// src/main/java/com/library/model/Transaction.java
package com.library.model;

import java.sql.Date;

/**
 * Represents a Book Transaction (Issue or Return) in the library system.
 */
public class Transaction {
    private int transactionId;
    private int bookId;
    private int studentId;
    private Date issueDate;
    private Date returnDate; // Null if not yet returned
    private String status; // "issued" or "returned"

    // Constructors
    public Transaction() {
    }

    public Transaction(int bookId, int studentId, Date issueDate, String status) {
        this.bookId = bookId;
        this.studentId = studentId;
        this.issueDate = issueDate;
        this.status = status;
    }

    public Transaction(int transactionId, int bookId, int studentId, Date issueDate, Date returnDate, String status) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.studentId = studentId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", bookId=" + bookId +
                ", studentId=" + studentId +
                ", issueDate=" + issueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                '}';
    }
}
