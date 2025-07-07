
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class BankingSystemConsole {

    private static BankOperations bankOperations = new BankOperations();
    private static Scanner scanner = new Scanner(System.in);
    private static Account loggedInAccount = null; // Stores the currently logged-in account

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₹#,##0.00");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        int choice;
        do {
            if (loggedInAccount == null) {
                displayMainMenu();
                choice = getUserChoice();
                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        loginAccount();
                        break;
                    case 0:
                        System.out.println("Exiting OneStopBank. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                displayLoggedInMenu();
                choice = getUserChoice();
                switch (choice) {
                    case 1:
                        viewDashboard();
                        break;
                    case 2:
                        depositFunds();
                        break;
                    case 3:
                        withdrawFunds();
                        break;
                    case 4:
                        transferFunds();
                        break;
                    case 5:
                        changeAccountPin();
                        break;
                    case 6:
                        viewTransactionHistory();
                        break;
                    case 0:
                        logoutAccount();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            System.out.println();
        } while (choice != 0 || loggedInAccount != null);
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("--- OneStopBank Main Menu ---");
        System.out.println("1. Create New Account");
        System.out.println("2. Login to Account");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void displayLoggedInMenu() {
        System.out.println("--- Welcome, " + loggedInAccount.getAccountHolderName() + " ---");
        System.out.println("1. View Dashboard");
        System.out.println("2. Deposit Funds");
        System.out.println("3. Withdraw Funds");
        System.out.println("4. Fund Transfer");
        System.out.println("5. Change Account PIN");
        System.out.println("6. View Transaction History");
        System.out.println("0. Logout");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume the invalid input
            System.out.print("Enter your choice: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static void createAccount() {
        System.out.println("\n--- Create New Account ---");
        System.out.print("Enter desired Account Number (e.g., 123456): ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter Full Name: ");
        String fullName = scanner.nextLine();

        System.out.print("Enter a 4-digit PIN: ");
        String pin = scanner.nextLine();
        if (pin.length() != 4 || !pin.matches("\\d{4}")) {
            System.out.println("Error: PIN must be a 4-digit number.");
            return;
        }

        System.out.print("Confirm PIN: ");
        String confirmPin = scanner.nextLine();
        if (!pin.equals(confirmPin)) {
            System.out.println("Error: PINs do not match.");
            return;
        }

        System.out.print("Enter Initial Deposit Amount (e.g., 500.00): ");
        BigDecimal initialDeposit;
        try {
            initialDeposit = new BigDecimal(scanner.nextLine());
            if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Error: Initial deposit cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format.");
            return;
        }

        System.out.print("Enter Account Type (Saving/Current, default Saving): ");
        String accountType = scanner.nextLine();
        if (accountType.isEmpty()) accountType = "Saving";

        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        // Default branch and IFSC for simplicity
        String branch = "Main Branch";
        String ifscCode = "JAVABNK001";

        Account newAccount = new Account(accountNumber, fullName, initialDeposit, accountType, phoneNumber, email, address, branch, ifscCode);

        if (bankOperations.createAccount(newAccount, pin)) {
            System.out.println("Account created successfully! Please login.");
        } else {
            System.out.println("Failed to create account. Account number or email might already exist or other database error.");
        }
    }

    private static void loginAccount() {
        System.out.println("\n--- Login to Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        loggedInAccount = bankOperations.login(accountNumber, pin);

        if (loggedInAccount != null) {
            System.out.println("Login successful! Welcome, " + loggedInAccount.getAccountHolderName() + ".");
        } else {
            System.out.println("Login failed. Invalid account number or PIN.");
        }
    }

    private static void viewDashboard() {
        if (loggedInAccount == null) {
            System.out.println("Please login first.");
            return;
        }
        // Refresh account details to get latest balance
        loggedInAccount = bankOperations.getAccountDetails(loggedInAccount.getAccountNumber());

        System.out.println("\n--- Your Dashboard ---");
        System.out.println("Welcome, " + loggedInAccount.getAccountHolderName() + "!");
        System.out.println("Account Number: " + loggedInAccount.getAccountNumber());
        System.out.println("Account Type: " + loggedInAccount.getAccountType());
        System.out.println("Current Balance: " + CURRENCY_FORMAT.format(loggedInAccount.getBalance()));
        System.out.println("Phone: " + loggedInAccount.getPhoneNumber());
        System.out.println("Email: " + loggedInAccount.getEmail());
        System.out.println("Address: " + loggedInAccount.getAddress());
        System.out.println("Branch: " + loggedInAccount.getBranch());
        System.out.println("IFSC Code: " + loggedInAccount.getIfscCode());
    }


    private static void depositFunds() {
        if (loggedInAccount == null) {
            System.out.println("Please login first.");
            return;
        }
        System.out.println("\n--- Deposit Funds ---");
        System.out.print("Enter amount to deposit: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a numeric value.");
            return;
        }

        if (bankOperations.deposit(loggedInAccount.getAccountNumber(), amount)) {
            System.out.println("Deposit successful!");
            // Refresh loggedInAccount to reflect new balance
            loggedInAccount = bankOperations.getAccountDetails(loggedInAccount.getAccountNumber());
            System.out.println("New balance: " + CURRENCY_FORMAT.format(loggedInAccount.getBalance()));
        } else {
            System.out.println("Deposit failed. Please check the amount.");
        }
    }

    private static void withdrawFunds() {
        if (loggedInAccount == null) {
            System.out.println("Please login first.");
            return;
        }
        System.out.println("\n--- Withdraw Funds ---");
        System.out.print("Enter amount to withdraw: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a numeric value.");
            return;
        }

        if (bankOperations.withdraw(loggedInAccount.getAccountNumber(), amount)) {
            System.out.println("Withdrawal successful!");
            // Refresh loggedInAccount to reflect new balance
            loggedInAccount = bankOperations.getAccountDetails(loggedInAccount.getAccountNumber());
            System.out.println("New balance: " + CURRENCY_FORMAT.format(loggedInAccount.getBalance()));
        } else {
            System.out.println("Withdrawal failed. Insufficient funds or invalid amount.");
        }
    }

    private static void transferFunds() {
        if (loggedInAccount == null) {
            System.out.println("Please login first.");
            return;
        }
        System.out.println("\n--- Fund Transfer ---");
        System.out.print("Enter recipient's account number: ");
        String recipientAccountNumber = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a numeric value.");
            return;
        }

        if (bankOperations.transfer(loggedInAccount.getAccountNumber(), recipientAccountNumber, amount)) {
            System.out.println("Transfer successful!");
            // Refresh loggedInAccount to reflect new balance
            loggedInAccount = bankOperations.getAccountDetails(loggedInAccount.getAccountNumber());
            System.out.println("Your new balance: " + CURRENCY_FORMAT.format(loggedInAccount.getBalance()));
        } else {
            System.out.println("Transfer failed. Check recipient account, amount, or your funds.");
        }
    }

    private static void changeAccountPin() {
        if (loggedInAccount == null) {
            System.out.println("Please login first.");
            return;
        }
        System.out.println("\n--- Change Account PIN ---");
        System.out.print("Enter current PIN: ");
        String currentPin = scanner.nextLine();
        System.out.print("Enter new 4-digit PIN: ");
        String newPin = scanner.nextLine();
        System.out.print("Confirm new PIN: ");
        String confirmNewPin = scanner.nextLine();

        if (newPin.length() != 4 || !newPin.matches("\\d{4}")) {
            System.out.println("Error: New PIN must be a 4-digit number.");
            return;
        }
        if (!newPin.equals(confirmNewPin)) {
            System.out.println("Error: New PINs do not match.");
            return;
        }

        if (bankOperations.changePin(loggedInAccount.getAccountNumber(), currentPin, newPin)) {
            System.out.println("PIN changed successfully!");
            // No need to refresh loggedInAccount as balance doesn't change
        } else {
            System.out.println("PIN change failed. Incorrect current PIN or other error.");
        }
    }

    private static void viewTransactionHistory() {
        if (loggedInAccount == null) {
            System.out.println("Please login first.");
            return;
        }
        System.out.println("\n--- Transaction History for " + loggedInAccount.getAccountNumber() + " ---");
        List<Transaction> transactions = bankOperations.getTransactions(loggedInAccount.getAccountNumber());

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        System.out.printf("%-5s %-15s %-10s %-20s %-15s %-15s %s%n",
                "ID", "Amount", "Type", "Date", "Source Acc", "Target Acc", "Description");
        System.out.println("--------------------------------------------------------------------------------------------------");

        for (Transaction tx : transactions) {
            System.out.printf("%-5d %-15s %-10s %-20s %-15s %-15s %s%n",
                    tx.getTransactionId(),
                    CURRENCY_FORMAT.format(tx.getAmount()),
                    tx.getTransactionType(),
                    DATE_FORMAT.format(tx.getTransactionDate()),
                    tx.getSourceAccountNumber() != null ? tx.getSourceAccountNumber() : "N/A",
                    tx.getTargetAccountNumber() != null ? tx.getTargetAccountNumber() : "N/A",
                    tx.getDescription() != null ? tx.getDescription() : "N/A");
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    private static void logoutAccount() {
        loggedInAccount = null;
        System.out.println("Logged out successfully.");
    }
}
