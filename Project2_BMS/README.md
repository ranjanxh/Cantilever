# OneStopBank - Java Desktop Banking System

This project is a simple desktop banking application developed using **Java Swing** for the Graphical User Interface (GUI) and **MySQL** as the relational database. It provides basic banking functionalities inspired by a modern dashboard design.

---

## ✨ Features

* **User Authentication**: Secure login with account number and a 4-digit PIN.
* **Account Creation**: Create new bank accounts with initial deposit, personal details, and a PIN.
* **Dashboard View**: Displays account holder's name, account number, current balance, account type, contact information, branch, and IFSC code.
* **Deposit Funds**: Add money to the logged-in account.
* **Withdraw Funds**: Withdraw money from the logged-in account (with balance check).
* **Fund Transfer**: Transfer money between accounts (with balance check and recipient account validation).
* **Change Account PIN**: Update the 4-digit PIN for the logged-in account.
* **Transaction History**: View a list of all past transactions (deposits, withdrawals, transfers) for the logged-in account.
* **Console Interface**: A separate console-based interface for interacting with the banking system.

---

## 🛠️ Technologies Used

* **Frontend**: Java Swing (for GUI)
* **Backend/Logic**: Java
* **Database**: MySQL
* **Database Connectivity**: MySQL JDBC Connector/J
* **Password/PIN Hashing**: jBCrypt library

---

## 📂 Project Structure

The project consists of the following key Java files:

* `DBConnection.java`: Handles the connection to the MySQL database.
* `Account.java`: A model class representing a bank account entity.
* `Transaction.java`: A model class representing a transaction record.
* `BankOperations.java`: Contains the core business logic for banking operations (login, create account, deposit, withdraw, transfer, change PIN, get details, get transactions). It uses jBCrypt for secure PIN hashing.
* `BankingSystemGUI.java`: The main class for the graphical user interface.
* `BankingSystemConsole.java`: A command-line interface for the banking system.

---

## 🚀 Setup Instructions

Follow these steps to set up and run the project:

### MySQL Database Setup

1.  **Ensure MySQL Server is Running**: Make sure your MySQL database server is installed and actively running on your machine (e.g., via MySQL Workbench or your system services).

2.  **Execute Database Schema**:
    * Open MySQL Workbench or your preferred MySQL client.
    * Connect to your local MySQL instance (e.g., `root` user with your password).
    * Open a new SQL query tab.
    * Copy the entire SQL script provided in the MySQL Database Schema for Java Banking System immersive document (you'll need to provide this SQL script alongside your project files, typically in a `database` folder or directly in the README).
    * Paste the script into the SQL tab.
    * **Crucial for Sample Data**: The `pin_hash` column requires a bcrypt hash. You'll need to generate this.
        * **Recommended**: Use the `PasswordHasher` Java class (provided as a comment in the SQL schema) with the `jBCrypt` library to generate a hash for a sample PIN like "1234".
        * **Alternatively**: Temporarily use an online bcrypt hash generator for "1234" (but never do this for production passwords).
    * Uncomment and modify the `INSERT` statements at the end of the SQL script to add a sample account. Replace `YOUR_PIN_HASH_FOR_1234` with the actual bcrypt hash you generated.
    * Execute the entire script (using the lightning bolt ⚡ icon in MySQL Workbench).
    * Verify that the `java_banking_db` database and its `accounts` and `transactions` tables are created by refreshing the Schemas navigator.

### Java Project Setup (in IntelliJ IDEA)

1.  **Create New Java Project**:
    * Open IntelliJ IDEA.
    * Go to `File` > `New` > `Project...`.
    * Select `Java` and click `Next`.
    * Choose your Java SDK (JDK 8 or higher is recommended) and click `Next`.
    * Give your project a name (e.g., `OneStopBankJava`) and click `Finish`.

2.  **Add Java Files**:
    * In your new project, navigate to the `src` folder.
    * Create new Java classes (`DBConnection.java`, `Account.java`, `Transaction.java`, `BankOperations.java`, `BankingSystemGUI.java`, `BankingSystemConsole.java`).
    * Copy the content from each respective immersive document and paste it into the correct Java file.

3.  **Add External Libraries (JARs)**:
    * **MySQL JDBC Connector/J**:
        * Download `mysql-connector-j-X.X.X.jar` (the JDBC driver) from the official MySQL website ([https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/)).
        * In IntelliJ, go to `File` > `Project Structure...` (`Ctrl + Alt + Shift + S` or `Cmd + ;`).
        * Select `Modules` on the left, then the `Dependencies` tab on the right.
        * Click the `+` button, choose `JARs or Directories...`, and navigate to select your downloaded `mysql-connector-j-X.X.X.jar` file. Click `OK`.
    * **jBCrypt Library**:
        * Download `jbcrypt-X.X.jar` (e.g., `jbcrypt-0.4.jar` or `jbcrypt-0.5.jar`) from a reliable Maven repository like [mvnrepository.com](https://mvnrepository.com/search?q=jbcrypt).
        * Follow the same steps as above to add this `jbcrypt-X.X.jar` file to your project's module dependencies.
    * After adding both JARs, click `Apply` and `OK` to close the Project Structure window.

---

## ▶️ Run the Application

You have two ways to run the banking system:

1.  **Graphical User Interface (GUI)**:
    * Open `BankingSystemGUI.java`.
    * Right-click anywhere in the code editor.
    * Select `Run 'BankingSystemGUI.main()'`.
    * A desktop window will appear, allowing you to interact with the banking system graphically.

2.  **Console-Based Interface**:
    * Open `BankingSystemConsole.java`.
    * Right-click anywhere in the code editor.
    * Select `Run 'BankingSystemConsole.main()'`.
    * The application will run in your IntelliJ console, where you can type commands.

---

## 💡 How to Use

* **Create Account**: If you haven't inserted sample data directly into the database, use the "Create New Account" option (either in GUI or Console) to set up your first account. Remember your Account Number and 4-digit PIN.
* **Login**: Use your created Account Number and PIN to log in.
* **Perform Operations**: Once logged in, navigate through the dashboard/menu to perform deposits, withdrawals, transfers, change your PIN, or view your transaction history.

---

## ⚠️ Important Notes

* **Database Credentials**: Ensure the `DB_PASSWORD` in `DBConnection.java` matches your actual MySQL root user password.
* **PIN Hashing**: For security, PINs are stored as bcrypt hashes. When creating an account, the application will hash the plain-text PIN you provide. When logging in, it will compare the entered PIN with the stored hash.
* **Error Handling**: Basic error handling is implemented, but for a production-grade application, more robust error logging and user feedback mechanisms would be necessary.
* **Concurrency**: This simple desktop application does not extensively handle multi-user concurrency scenarios beyond basic database transactions.
* **User Management**: The current system does not have a separate "user" table for general user management; the `accounts` table serves as the primary entity.

Enjoy your Java Desktop Banking System!
