
import org.mindrot.jbcrypt.BCrypt; // Import BCrypt for hashing
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BankOperations {

    /**
     * Authenticates a user based on account number and PIN.
     *
     * @param accountNumber The account number.
     * @param pin The PIN provided by the user.
     * @return The Account object if login is successful, null otherwise.
     */
    public Account login(String accountNumber, String pin) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Account account = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT account_number, account_holder_name, balance, pin_hash, account_type, " +
                    "phone_number, email, address, branch, ifsc_code FROM accounts WHERE account_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPinHash = rs.getString("pin_hash");
                // Verify the provided PIN against the stored hash
                if (BCrypt.checkpw(pin, storedPinHash)) {
                    account = new Account(
                            rs.getString("account_number"),
                            rs.getString("account_holder_name"),
                            rs.getBigDecimal("balance"),
                            storedPinHash, // Store hash in object for consistency
                            rs.getString("account_type"),
                            rs.getString("phone_number"),
                            rs.getString("email"),
                            rs.getString("address"),
                            rs.getString("branch"),
                            rs.getString("ifsc_code")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignore */ }
            DBConnection.closeConnection(conn);
        }
        return account;
    }

    /**
     * Creates a new bank account.
     *
     * @param account The Account object containing new account details.
     * @param pin The plain-text PIN for the new account.
     * @return True if account creation is successful, false otherwise.
     */
    public boolean createAccount(Account account, String pin) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Check if account number already exists
            if (getAccountDetails(account.getAccountNumber()) != null) {
                System.err.println("Account number already exists: " + account.getAccountNumber());
                return false;
            }

            // Hash the PIN before storing
            String hashedPin = BCrypt.hashpw(pin, BCrypt.gensalt(10)); // 10 is the salt rounds

            String sql = "INSERT INTO accounts (account_number, account_holder_name, balance, pin_hash, " +
                    "account_type, phone_number, email, address, branch, ifsc_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getAccountHolderName());
            pstmt.setBigDecimal(3, account.getBalance());
            pstmt.setString(4, hashedPin); // Store hashed PIN
            pstmt.setString(5, account.getAccountType());
            pstmt.setString(6, account.getPhoneNumber());
            pstmt.setString(7, account.getEmail());
            pstmt.setString(8, account.getAddress());
            pstmt.setString(9, account.getBranch());
            pstmt.setString(10, account.getIfscCode());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit(); // Commit transaction
                return true;
            } else {
                conn.rollback(); // Rollback if no rows affected
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Database error during account creation: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignore */ }
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignore */ } // Reset auto-commit
            DBConnection.closeConnection(conn);
        }
    }

    /**
     * Deposits funds into an account.
     *
     * @param accountNumber The account number to deposit into.
     * @param amount The amount to deposit.
     * @return True if deposit is successful, false otherwise.
     */
    public boolean deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("Deposit amount must be positive.");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmtUpdate = null;
        PreparedStatement pstmtInsert = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update account balance
            String sqlUpdate = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            pstmtUpdate = conn.prepareStatement(sqlUpdate);
            pstmtUpdate.setBigDecimal(1, amount);
            pstmtUpdate.setString(2, accountNumber);
            int rowsAffected = pstmtUpdate.executeUpdate();

            if (rowsAffected == 0) {
                conn.rollback();
                System.err.println("Account not found for deposit: " + accountNumber);
                return false;
            }

            // Record transaction
            String sqlInsert = "INSERT INTO transactions (account_number, amount, transaction_type, target_account_number, description) VALUES (?, ?, ?, ?, ?)";
            pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setString(1, accountNumber);
            pstmtInsert.setBigDecimal(2, amount);
            pstmtInsert.setString(3, "Deposit");
            pstmtInsert.setString(4, accountNumber); // Target is the same account
            pstmtInsert.setString(5, "Cash Deposit");
            pstmtInsert.executeUpdate();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            System.err.println("Database error during deposit: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            return false;
        } finally {
            try { if (pstmtUpdate != null) pstmtUpdate.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtInsert != null) pstmtInsert.close(); } catch (SQLException e) { /* ignore */ }
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
            DBConnection.closeConnection(conn);
        }
    }

    /**
     * Withdraws funds from an account.
     *
     * @param accountNumber The account number to withdraw from.
     * @param amount The amount to withdraw.
     * @return True if withdrawal is successful, false otherwise.
     */
    public boolean withdraw(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("Withdrawal amount must be positive.");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmtSelect = null;
        PreparedStatement pstmtUpdate = null;
        PreparedStatement pstmtInsert = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Check current balance
            String sqlSelect = "SELECT balance FROM accounts WHERE account_number = ? FOR UPDATE"; // Lock row
            pstmtSelect = conn.prepareStatement(sqlSelect);
            pstmtSelect.setString(1, accountNumber);
            rs = pstmtSelect.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                System.err.println("Account not found for withdrawal: " + accountNumber);
                return false;
            }

            BigDecimal currentBalance = rs.getBigDecimal("balance");
            if (currentBalance.compareTo(amount) < 0) {
                conn.rollback();
                System.err.println("Insufficient funds in account: " + accountNumber);
                return false;
            }

            // Update account balance
            String sqlUpdate = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            pstmtUpdate = conn.prepareStatement(sqlUpdate);
            pstmtUpdate.setBigDecimal(1, amount);
            pstmtUpdate.setString(2, accountNumber);
            pstmtUpdate.executeUpdate();

            // Record transaction
            String sqlInsert = "INSERT INTO transactions (account_number, amount, transaction_type, source_account_number, description) VALUES (?, ?, ?, ?, ?)";
            pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setString(1, accountNumber);
            pstmtInsert.setBigDecimal(2, amount);
            pstmtInsert.setString(3, "Withdraw");
            pstmtInsert.setString(4, accountNumber); // Source is the same account
            pstmtInsert.setString(5, "ATM Withdrawal");
            pstmtInsert.executeUpdate();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            System.err.println("Database error during withdrawal: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtSelect != null) pstmtSelect.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtUpdate != null) pstmtUpdate.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtInsert != null) pstmtInsert.close(); } catch (SQLException e) { /* ignore */ }
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
            DBConnection.closeConnection(conn);
        }
    }

    /**
     * Transfers funds from one account to another.
     *
     * @param sourceAccountNumber The account number to transfer from.
     * @param targetAccountNumber The account number to transfer to.
     * @param amount The amount to transfer.
     * @return True if transfer is successful, false otherwise.
     */
    public boolean transfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("Transfer amount must be positive.");
            return false;
        }
        if (sourceAccountNumber.equals(targetAccountNumber)) {
            System.err.println("Cannot transfer to the same account.");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmtSelectSender = null;
        PreparedStatement pstmtSelectTarget = null;
        PreparedStatement pstmtUpdateSender = null;
        PreparedStatement pstmtUpdateTarget = null;
        PreparedStatement pstmtInsertSenderTx = null;
        PreparedStatement pstmtInsertTargetTx = null;
        ResultSet rsSender = null;
        ResultSet rsTarget = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Get sender's account with lock
            String sqlSelectSender = "SELECT balance FROM accounts WHERE account_number = ? FOR UPDATE";
            pstmtSelectSender = conn.prepareStatement(sqlSelectSender);
            pstmtSelectSender.setString(1, sourceAccountNumber);
            rsSender = pstmtSelectSender.executeQuery();

            if (!rsSender.next()) {
                conn.rollback();
                System.err.println("Source account not found: " + sourceAccountNumber);
                return false;
            }
            BigDecimal senderBalance = rsSender.getBigDecimal("balance");

            if (senderBalance.compareTo(amount) < 0) {
                conn.rollback();
                System.err.println("Insufficient funds in source account: " + sourceAccountNumber);
                return false;
            }

            // Get recipient's account with lock
            String sqlSelectTarget = "SELECT balance FROM accounts WHERE account_number = ? FOR UPDATE";
            pstmtSelectTarget = conn.prepareStatement(sqlSelectTarget);
            pstmtSelectTarget.setString(1, targetAccountNumber);
            rsTarget = pstmtSelectTarget.executeQuery();

            if (!rsTarget.next()) {
                conn.rollback();
                System.err.println("Target account not found: " + targetAccountNumber);
                return false;
            }

            // Update sender's balance
            String sqlUpdateSender = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            pstmtUpdateSender = conn.prepareStatement(sqlUpdateSender);
            pstmtUpdateSender.setBigDecimal(1, amount);
            pstmtUpdateSender.setString(2, sourceAccountNumber);
            pstmtUpdateSender.executeUpdate();

            // Update recipient's balance
            String sqlUpdateTarget = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            pstmtUpdateTarget = conn.prepareStatement(sqlUpdateTarget);
            pstmtUpdateTarget.setBigDecimal(1, amount);
            pstmtUpdateTarget.setString(2, targetAccountNumber);
            pstmtUpdateTarget.executeUpdate();

            // Record transaction for sender
            String sqlInsertSenderTx = "INSERT INTO transactions (account_number, amount, transaction_type, source_account_number, target_account_number, description) VALUES (?, ?, ?, ?, ?, ?)";
            pstmtInsertSenderTx = conn.prepareStatement(sqlInsertSenderTx);
            pstmtInsertSenderTx.setString(1, sourceAccountNumber);
            pstmtInsertSenderTx.setBigDecimal(2, amount);
            pstmtInsertSenderTx.setString(3, "Transfer");
            pstmtInsertSenderTx.setString(4, sourceAccountNumber);
            pstmtInsertSenderTx.setString(5, targetAccountNumber);
            pstmtInsertSenderTx.setString(6, "Online Transfer Out");
            pstmtInsertSenderTx.executeUpdate();

            // Record transaction for recipient
            String sqlInsertTargetTx = "INSERT INTO transactions (account_number, amount, transaction_type, source_account_number, target_account_number, description) VALUES (?, ?, ?, ?, ?, ?)";
            pstmtInsertTargetTx = conn.prepareStatement(sqlInsertTargetTx);
            pstmtInsertTargetTx.setString(1, targetAccountNumber);
            pstmtInsertTargetTx.setBigDecimal(2, amount);
            pstmtInsertTargetTx.setString(3, "Deposit"); // For recipient, it's a deposit via transfer
            pstmtInsertTargetTx.setString(4, sourceAccountNumber);
            pstmtInsertTargetTx.setString(5, targetAccountNumber);
            pstmtInsertTargetTx.setString(6, "Online Transfer In");
            pstmtInsertTargetTx.executeUpdate();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            System.err.println("Database error during transfer: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            return false;
        } finally {
            try { if (rsSender != null) rsSender.close(); } catch (SQLException e) { /* ignore */ }
            try { if (rsTarget != null) rsTarget.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtSelectSender != null) pstmtSelectSender.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtSelectTarget != null) pstmtSelectTarget.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtUpdateSender != null) pstmtUpdateSender.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtUpdateTarget != null) pstmtUpdateTarget.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtInsertSenderTx != null) pstmtInsertSenderTx.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtInsertTargetTx != null) pstmtInsertTargetTx.close(); } catch (SQLException e) { /* ignore */ }
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
            DBConnection.closeConnection(conn);
        }
    }

    /**
     * Changes the PIN for an account.
     *
     * @param accountNumber The account number.
     * @param currentPin The current plain-text PIN.
     * @param newPin The new plain-text PIN.
     * @return True if PIN change is successful, false otherwise.
     */
    public boolean changePin(String accountNumber, String currentPin, String newPin) {
        Connection conn = null;
        PreparedStatement pstmtSelect = null;
        PreparedStatement pstmtUpdate = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // Retrieve current PIN hash
            String sqlSelect = "SELECT pin_hash FROM accounts WHERE account_number = ?";
            pstmtSelect = conn.prepareStatement(sqlSelect);
            pstmtSelect.setString(1, accountNumber);
            rs = pstmtSelect.executeQuery();

            if (!rs.next()) {
                System.err.println("Account not found for PIN change: " + accountNumber);
                return false;
            }

            String storedPinHash = rs.getString("pin_hash");
            // Verify current PIN
            if (!BCrypt.checkpw(currentPin, storedPinHash)) {
                System.err.println("Incorrect current PIN for account: " + accountNumber);
                return false;
            }

            // Hash the new PIN
            String hashedNewPin = BCrypt.hashpw(newPin, BCrypt.gensalt(10));

            // Update PIN hash
            String sqlUpdate = "UPDATE accounts SET pin_hash = ? WHERE account_number = ?";
            pstmtUpdate = conn.prepareStatement(sqlUpdate);
            pstmtUpdate.setString(1, hashedNewPin);
            pstmtUpdate.setString(2, accountNumber);
            int rowsAffected = pstmtUpdate.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error during PIN change: " + e.getMessage());
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtSelect != null) pstmtSelect.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmtUpdate != null) pstmtUpdate.close(); } catch (SQLException e) { /* ignore */ }
            DBConnection.closeConnection(conn);
        }
    }

    /**
     * Retrieves all details for a given account number.
     *
     * @param accountNumber The account number to retrieve details for.
     * @return The Account object if found, null otherwise.
     */
    public Account getAccountDetails(String accountNumber) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Account account = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT account_number, account_holder_name, balance, pin_hash, account_type, " +
                    "phone_number, email, address, branch, ifsc_code FROM accounts WHERE account_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                account = new Account(
                        rs.getString("account_number"),
                        rs.getString("account_holder_name"),
                        rs.getBigDecimal("balance"),
                        rs.getString("pin_hash"), // Include hash for internal use
                        rs.getString("account_type"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("branch"),
                        rs.getString("ifsc_code")
                );
            }
        } catch (SQLException e) {
            System.err.println("Database error fetching account details: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignore */ }
            DBConnection.closeConnection(conn);
        }
        return account;
    }

    /**
     * Retrieves transaction history for a given account.
     *
     * @param accountNumber The account number to retrieve transactions for.
     * @return A list of Transaction objects.
     */
    public List<Transaction> getTransactions(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT transaction_id, account_number, amount, transaction_type, transaction_date, " +
                    "source_account_number, target_account_number, description FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNumber);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getString("account_number"),
                        rs.getBigDecimal("amount"),
                        rs.getString("transaction_type"),
                        rs.getTimestamp("transaction_date"),
                        rs.getString("source_account_number"),
                        rs.getString("target_account_number"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database error fetching transactions: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignore */ }
            DBConnection.closeConnection(conn);
        }
        return transactions;
    }
}
