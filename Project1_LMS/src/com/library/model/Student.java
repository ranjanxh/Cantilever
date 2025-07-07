
// src/main/java/com/library/model/Student.java
package com.library.model;

/**
 * Represents a Student (User) in the library system.
 */
public class Student {
    private int studentId;
    private String name;
    private String studentIdCard; // Unique ID for the student
    private String contactNumber;
    private String email;

    // Constructors
    public Student() {
    }

    public Student(String name, String studentIdCard, String contactNumber, String email) {
        this.name = name;
        this.studentIdCard = studentIdCard;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public Student(int studentId, String name, String studentIdCard, String contactNumber, String email) {
        this.studentId = studentId;
        this.name = name;
        this.studentIdCard = studentIdCard;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentIdCard() {
        return studentIdCard;
    }

    public void setStudentIdCard(String studentIdCard) {
        this.studentIdCard = studentIdCard;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", studentIdCard='" + studentIdCard + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
