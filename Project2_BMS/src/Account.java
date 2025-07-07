
import java.math.BigDecimal;

public class Account {
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;
    private String pinHash;
    private String accountType;
    private String phoneNumber;
    private String email;
    private String address;
    private String branch;
    private String ifscCode;

    public Account(String accountNumber, String accountHolderName, BigDecimal balance,
                   String accountType, String phoneNumber, String email, String address,
                   String branch, String ifscCode) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.branch = branch;
        this.ifscCode = ifscCode;
    }


    public Account(String accountNumber, String accountHolderName, BigDecimal balance, String pinHash,
                   String accountType, String phoneNumber, String email, String address,
                   String branch, String ifscCode) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.pinHash = pinHash;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.branch = branch;
        this.ifscCode = ifscCode;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getPinHash() {
        return pinHash;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getBranch() {
        return branch;
    }

    public String getIfscCode() {
        return ifscCode;
    }


    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", branch='" + branch + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                '}';
    }
}
