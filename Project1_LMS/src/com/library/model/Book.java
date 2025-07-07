package com.library.model;

/**
 * Represents a Book in the library system.
 */
public class Book {
    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private int publicationYear;
    private int quantity;
    private int availableQuantity;

    // Constructors
    public Book() {
    }

    public Book(String title, String author, String isbn, String publisher, int publicationYear, int quantity, int availableQuantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
    }

    public Book(int bookId, String title, String author, String isbn, String publisher, int publicationYear, int quantity, int availableQuantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationYear=" + publicationYear +
                ", quantity=" + quantity +
                ", availableQuantity=" + availableQuantity +
                '}';
    }
}
