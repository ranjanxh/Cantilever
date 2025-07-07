
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private String accountNumber;
    private BigDecimal amount;
    private String transactionType; // e.g., "Deposit", "Withdraw", "Transfer"
    private Timestamp transactionDate;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String description;

    public Transaction(int transactionId, String accountNumber, BigDecimal amount, String transactionType,
                       Timestamp transactionDate, String sourceAccountNumber, String targetAccountNumber, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.description = description;
    }

    // Getters
    public int getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", transactionDate=" + transactionDate +
                ", sourceAccountNumber='" + sourceAccountNumber + '\'' +
                ", targetAccountNumber='" + targetAccountNumber + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

