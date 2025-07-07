// src/main/java/com/library/gui/LibraryApp.java
package com.library.gui;

import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Transaction;
import com.library.service.LibraryManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Main GUI class for the Library Management System using Swing.
 */
public class LibraryApp extends JFrame {

    private LibraryManager libraryManager;

    // GUI Components
    private JTabbedPane tabbedPane;

    // Book Management Panel Components
    private JTextField bookTitleField, bookAuthorField, bookIsbnField, bookPublisherField, bookYearField, bookQuantityField;
    private JButton addBookButton, viewBooksButton, updateBookButton, deleteBookButton;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;

    // Student Management Panel Components
    private JTextField studentNameField, studentIdCardField, studentContactField, studentEmailField;
    private JButton addStudentButton, viewStudentsButton, deleteStudentButton; // Added deleteStudentButton
    private JTable studentTable;
    private DefaultTableModel studentTableModel;

    // Transaction Panel Components
    private JTextField issueBookIdField, issueStudentIdField;
    private JButton issueBookButton, returnBookButton, viewIssuedBooksButton;
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private JTextField returnTransactionIdField, returnBookIdField;


    public LibraryApp() {
        libraryManager = new LibraryManager();
        setTitle("Library Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        initComponents();
        addListeners();

        // Initial load of data
        loadBooksIntoTable();
        loadStudentsIntoTable();
        loadIssuedTransactionsIntoTable();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // Book Management Panel
        JPanel bookPanel = new JPanel(new BorderLayout());
        bookPanel.add(createBookInputPanel(), BorderLayout.NORTH);
        bookPanel.add(createBookTablePanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Book Management", bookPanel);

        // Student Management Panel
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.add(createStudentInputPanel(), BorderLayout.NORTH);
        studentPanel.add(createStudentTablePanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Student Management", studentPanel);

        // Transaction Panel
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.add(createTransactionInputPanel(), BorderLayout.NORTH);
        transactionPanel.add(createTransactionTablePanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Transactions", transactionPanel);

        add(tabbedPane);
    }

    private JPanel createBookInputPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Book Details"));

        panel.add(new JLabel("Title:"));
        bookTitleField = new JTextField();
        panel.add(bookTitleField);

        panel.add(new JLabel("Author:"));
        bookAuthorField = new JTextField();
        panel.add(bookAuthorField);

        panel.add(new JLabel("ISBN:"));
        bookIsbnField = new JTextField();
        panel.add(bookIsbnField);

        panel.add(new JLabel("Publisher:"));
        bookPublisherField = new JTextField();
        panel.add(bookPublisherField);

        panel.add(new JLabel("Publication Year:"));
        bookYearField = new JTextField();
        panel.add(bookYearField);

        panel.add(new JLabel("Quantity:"));
        bookQuantityField = new JTextField();
        panel.add(bookQuantityField);

        addBookButton = new JButton("Add Book");
        viewBooksButton = new JButton("View All Books");
        updateBookButton = new JButton("Update Book (by ID)");
        deleteBookButton = new JButton("Delete Book (by ID)");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addBookButton);
        buttonPanel.add(updateBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(viewBooksButton);

        JPanel inputAndButtonPanel = new JPanel(new BorderLayout());
        inputAndButtonPanel.add(panel, BorderLayout.CENTER);
        inputAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputAndButtonPanel;
    }

    private JPanel createBookTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("All Books"));

        String[] columnNames = {"ID", "Title", "Author", "ISBN", "Publisher", "Year", "Total Qty", "Available Qty"};
        bookTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        bookTable = new JTable(bookTableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add a listener for row selection to populate fields for update/delete
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                int selectedRow = bookTable.getSelectedRow();
                bookTitleField.setText(bookTableModel.getValueAt(selectedRow, 1).toString());
                bookAuthorField.setText(bookTableModel.getValueAt(selectedRow, 2).toString());
                bookIsbnField.setText(bookTableModel.getValueAt(selectedRow, 3).toString());
                bookPublisherField.setText(bookTableModel.getValueAt(selectedRow, 4).toString());
                bookYearField.setText(bookTableModel.getValueAt(selectedRow, 5).toString());
                bookQuantityField.setText(bookTableModel.getValueAt(selectedRow, 6).toString());
                // Available quantity is not directly editable via this panel, it's derived.
            }
        });

        return panel;
    }

    private JPanel createStudentInputPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        panel.add(new JLabel("Name:"));
        studentNameField = new JTextField();
        panel.add(studentNameField);

        panel.add(new JLabel("Student ID Card:"));
        studentIdCardField = new JTextField();
        panel.add(studentIdCardField);

        panel.add(new JLabel("Contact Number:"));
        studentContactField = new JTextField();
        panel.add(studentContactField);

        panel.add(new JLabel("Email:"));
        studentEmailField = new JTextField();
        panel.add(studentEmailField);

        addStudentButton = new JButton("Add Student");
        viewStudentsButton = new JButton("View All Students");
        deleteStudentButton = new JButton("Delete Student (by ID)"); // Initialize the new button

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addStudentButton);
        buttonPanel.add(deleteStudentButton); // Add the new button to the panel
        buttonPanel.add(viewStudentsButton);

        JPanel inputAndButtonPanel = new JPanel(new BorderLayout());
        inputAndButtonPanel.add(panel, BorderLayout.CENTER);
        inputAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputAndButtonPanel;
    }

    private JPanel createStudentTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("All Students"));

        String[] columnNames = {"ID", "Name", "ID Card", "Contact", "Email"};
        studentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        studentTable = new JTable(studentTableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add a listener for row selection to populate fields for delete
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                int selectedRow = studentTable.getSelectedRow();
                // Assuming you might want to populate fields for editing or just to show selected ID
                // For delete, we just need the ID from the table.
                studentNameField.setText(studentTableModel.getValueAt(selectedRow, 1).toString());
                studentIdCardField.setText(studentTableModel.getValueAt(selectedRow, 2).toString());
                studentContactField.setText(studentTableModel.getValueAt(selectedRow, 3).toString());
                studentEmailField.setText(studentTableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        return panel;
    }

    private JPanel createTransactionInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Transaction Details"));

        panel.add(new JLabel("Book ID (for Issue):"));
        issueBookIdField = new JTextField();
        panel.add(issueBookIdField);

        panel.add(new JLabel("Student ID (for Issue):"));
        issueStudentIdField = new JTextField();
        panel.add(issueStudentIdField);

        panel.add(new JLabel("Transaction ID (for Return):"));
        returnTransactionIdField = new JTextField();
        panel.add(returnTransactionIdField);

        panel.add(new JLabel("Book ID (for Return):"));
        returnBookIdField = new JTextField();
        panel.add(returnBookIdField);

        issueBookButton = new JButton("Issue Book");
        returnBookButton = new JButton("Return Book");
        viewIssuedBooksButton = new JButton("View Issued Books");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(issueBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(viewIssuedBooksButton);

        JPanel inputAndButtonPanel = new JPanel(new BorderLayout());
        inputAndButtonPanel.add(panel, BorderLayout.CENTER);
        inputAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputAndButtonPanel;
    }

    private JPanel createTransactionTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Issued Transactions"));

        String[] columnNames = {"Trans ID", "Book ID", "Student ID", "Issue Date", "Return Date", "Status"};
        transactionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        transactionTable = new JTable(transactionTableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add a listener for row selection to populate fields for return
        transactionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && transactionTable.getSelectedRow() != -1) {
                int selectedRow = transactionTable.getSelectedRow();
                returnTransactionIdField.setText(transactionTableModel.getValueAt(selectedRow, 0).toString());
                returnBookIdField.setText(transactionTableModel.getValueAt(selectedRow, 1).toString());
            }
        });

        return panel;
    }

    private void addListeners() {
        // Book Management Listeners
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        viewBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooksIntoTable();
            }
        });

        updateBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });

        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        // Student Management Listeners
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudentsIntoTable();
            }
        });

        // Listener for the new delete student button
        deleteStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });


        // Transaction Listeners
        issueBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                issueBook();
            }
        });

        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });

        viewIssuedBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadIssuedTransactionsIntoTable();
            }
        });
    }

    // --- Book Management Methods ---
    private void addBook() {
        try {
            String title = bookTitleField.getText();
            String author = bookAuthorField.getText();
            String isbn = bookIsbnField.getText();
            String publisher = bookPublisherField.getText();
            int year = Integer.parseInt(bookYearField.getText());
            int quantity = Integer.parseInt(bookQuantityField.getText());

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                showMessage("Error", "Title, Author, and ISBN cannot be empty.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = new Book(title, author, isbn, publisher, year, quantity, quantity); // available_quantity = quantity initially
            if (libraryManager.addBook(book)) {
                showMessage("Success", "Book added successfully!", JOptionPane.INFORMATION_MESSAGE);
                clearBookFields();
                loadBooksIntoTable();
            } else {
                showMessage("Error", "Failed to add book. ISBN might be duplicate.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            showMessage("Input Error", "Please enter valid numbers for Publication Year and Quantity.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("No Selection", "Please select a book from the table to update.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
            String title = bookTitleField.getText();
            String author = bookAuthorField.getText();
            String isbn = bookIsbnField.getText();
            String publisher = bookPublisherField.getText();
            int year = Integer.parseInt(bookYearField.getText());
            int quantity = Integer.parseInt(bookQuantityField.getText());

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                showMessage("Error", "Title, Author, and ISBN cannot be empty.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book existingBook = libraryManager.getBookById(bookId);
            if (existingBook == null) {
                showMessage("Error", "Book not found for update.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calculate new available quantity based on change in total quantity
            int oldQuantity = existingBook.getQuantity();
            int oldAvailableQuantity = existingBook.getAvailableQuantity();
            int newAvailableQuantity = oldAvailableQuantity + (quantity - oldQuantity);

            if (newAvailableQuantity < 0) {
                showMessage("Error", "Cannot reduce total quantity below currently issued books.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book updatedBook = new Book(bookId, title, author, isbn, publisher, year, quantity, newAvailableQuantity);
            if (libraryManager.updateBook(updatedBook)) {
                showMessage("Success", "Book updated successfully!", JOptionPane.INFORMATION_MESSAGE);
                clearBookFields();
                loadBooksIntoTable();
            } else {
                showMessage("Error", "Failed to update book.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            showMessage("Input Error", "Please enter valid numbers for Publication Year and Quantity.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("No Selection", "Please select a book from the table to delete.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (libraryManager.deleteBook(bookId)) {
                showMessage("Success", "Book deleted successfully!", JOptionPane.INFORMATION_MESSAGE);
                clearBookFields();
                loadBooksIntoTable();
            } else {
                showMessage("Error", "Failed to delete book. It might be currently issued.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadBooksIntoTable() {
        bookTableModel.setRowCount(0); // Clear existing data
        List<Book> books = libraryManager.getAllBooks();
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getPublisher(),
                    book.getPublicationYear(),
                    book.getQuantity(),
                    book.getAvailableQuantity()
            });
        }
    }

    private void clearBookFields() {
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookIsbnField.setText("");
        bookPublisherField.setText("");
        bookYearField.setText("");
        bookQuantityField.setText("");
    }

    // --- Student Management Methods ---
    private void addStudent() {
        try {
            String name = studentNameField.getText();
            String idCard = studentIdCardField.getText();
            String contact = studentContactField.getText();
            String email = studentEmailField.getText();

            if (name.isEmpty() || idCard.isEmpty()) {
                showMessage("Error", "Name and Student ID Card cannot be empty.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(name, idCard, contact, email);
            if (libraryManager.addStudent(student)) {
                showMessage("Success", "Student added successfully!", JOptionPane.INFORMATION_MESSAGE);
                clearStudentFields();
                loadStudentsIntoTable();
            } else {
                showMessage("Error", "Failed to add student. Student ID Card might be duplicate.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            showMessage("Error", "An unexpected error occurred while adding student.", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("No Selection", "Please select a student from the table to delete.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) studentTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student? This will also delete associated transactions.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (libraryManager.deleteStudent(studentId)) {
                showMessage("Success", "Student deleted successfully!", JOptionPane.INFORMATION_MESSAGE);
                clearStudentFields();
                loadStudentsIntoTable();
                loadIssuedTransactionsIntoTable(); // Refresh transactions as well
            } else {
                showMessage("Error", "Failed to delete student. They might have issued books that need to be returned first.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadStudentsIntoTable() {
        studentTableModel.setRowCount(0); // Clear existing data
        List<Student> students = libraryManager.getAllStudents();
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{
                    student.getStudentId(),
                    student.getName(),
                    student.getStudentIdCard(),
                    student.getContactNumber(),
                    student.getEmail()
            });
        }
    }

    private void clearStudentFields() {
        studentNameField.setText("");
        studentIdCardField.setText("");
        studentContactField.setText("");
        studentEmailField.setText("");
    }

    // --- Transaction Methods ---
    private void issueBook() {
        try {
            int bookId = Integer.parseInt(issueBookIdField.getText());
            int studentId = Integer.parseInt(issueStudentIdField.getText());

            if (libraryManager.issueBook(bookId, studentId)) {
                showMessage("Success", "Book issued successfully!", JOptionPane.INFORMATION_MESSAGE);
                clearIssueFields();
                loadIssuedTransactionsIntoTable();
                loadBooksIntoTable(); // Update book availability display
            } else {
                showMessage("Error", "Failed to issue book. Check Book ID, Student ID, or book availability.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            showMessage("Input Error", "Please enter valid numbers for Book ID and Student ID.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        try {
            int transactionId = Integer.parseInt(returnTransactionIdField.getText());
            int bookId = Integer.parseInt(returnBookIdField.getText());

            if (libraryManager.returnBook(transactionId, bookId)) {
                showMessage("Success", "Book returned successfully!", JOptionPane.INFORMATION_MESSAGE);
                clearReturnFields();
                loadIssuedTransactionsIntoTable(); // Refresh table to remove returned book
                loadBooksIntoTable(); // Update book availability display
            } else {
                showMessage("Error", "Failed to return book. Check Transaction ID and Book ID.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            showMessage("Input Error", "Please enter valid numbers for Transaction ID and Book ID.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadIssuedTransactionsIntoTable() {
        transactionTableModel.setRowCount(0); // Clear existing data
        List<Transaction> transactions = libraryManager.getIssuedTransactions();
        for (Transaction transaction : transactions) {
            transactionTableModel.addRow(new Object[]{
                    transaction.getTransactionId(),
                    transaction.getBookId(),
                    transaction.getStudentId(),
                    transaction.getIssueDate(),
                    transaction.getReturnDate() != null ? transaction.getReturnDate() : "N/A",
                    transaction.getStatus()
            });
        }
    }

    private void clearIssueFields() {
        issueBookIdField.setText("");
        issueStudentIdField.setText("");
    }

    private void clearReturnFields() {
        returnTransactionIdField.setText("");
        returnBookIdField.setText("");
    }

    // --- Utility Method ---
    private void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public static void main(String[] args) {
        // Ensure Swing GUI updates are done on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LibraryApp().setVisible(true);
            }
        });
    }
}
