
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class BankingSystemGUI extends JFrame {

    private BankOperations bankOperations;
    private Account loggedInAccount;


    private JPanel cardPanel;
    private JPanel loginPanel;
    private JPanel createAccountPanel;
    private JPanel dashboardPanel;
    private JPanel depositPanel;
    private JPanel withdrawPanel;
    private JPanel transferPanel;
    private JPanel changePinPanel;
    private JPanel transactionHistoryPanel;

    // Login/Create Account Components
    private JTextField loginAccNumField;
    private JPasswordField loginPinField;

    private JTextField createAccNumField;
    private JTextField createNameField;
    private JPasswordField createPinField;
    private JPasswordField createConfirmPinField;
    private JTextField createBalanceField;
    private JComboBox<String> createAccountTypeCombo;
    private JTextField createPhoneField;
    private JTextField createEmailField;
    private JTextField createAddressField;

    // Dashboard Components
    private JLabel dashboardWelcomeLabel;
    private JLabel dashboardAccNumLabel;
    private JLabel dashboardPhoneLabel;
    private JLabel dashboardEmailLabel;
    private JLabel dashboardAddressLabel;
    private JLabel dashboardBalanceLabel;
    private JLabel dashboardAccTypeLabel;
    private JLabel dashboardBranchLabel;
    private JLabel dashboardIFSCCodeLabel;
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;

    // Transaction Components
    private JTextField amountField;
    private JTextField recipientAccNumField;
    private JPasswordField currentPinField;
    private JPasswordField newPinField;
    private JPasswordField confirmNewPinField;

    // Formatters
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₹#,##0.00");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public BankingSystemGUI() {
        bankOperations = new BankOperations();
        setTitle("OneStopBank - Desktop Banking System");
        setSize(1000, 700); // Increased size for dashboard layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set up CardLayout for switching panels
        cardPanel = new JPanel(new CardLayout());
        add(cardPanel);

        createLoginPanel();
        createCreateAccountPanel();
        createDashboardPanel();
        createDepositPanel();
        createWithdrawPanel();
        createTransferPanel();
        createChangePinPanel();
        createTransactionHistoryPanel();

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(createAccountPanel, "CreateAccount");
        cardPanel.add(dashboardPanel, "Dashboard");
        cardPanel.add(depositPanel, "Deposit");
        cardPanel.add(withdrawPanel, "Withdraw");
        cardPanel.add(transferPanel, "Transfer");
        cardPanel.add(changePinPanel, "ChangePin");
        cardPanel.add(transactionHistoryPanel, "TransactionHistory");

        showPanel("Login"); // Start with the login panel
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, panelName);
        // Special actions when showing a panel
        if (panelName.equals("Dashboard")) {
            updateDashboard();
        } else if (panelName.equals("TransactionHistory")) {
            updateTransactionHistoryTable();
        }
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 242, 245)); // Light gray background
        loginPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Welcome to OneStopBank", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Your trusted banking partner", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 30, 10); // Smaller top inset, larger bottom
        loginPanel.add(subtitleLabel, gbc);

        // Account Number
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        loginAccNumField = new JTextField(20);
        loginPanel.add(loginAccNumField, gbc);

        // PIN
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        loginPanel.add(new JLabel("PIN:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        loginPinField = new JPasswordField(4);
        loginPanel.add(loginPinField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; // Don't fill horizontally
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        gbc.insets = new Insets(20, 10, 10, 10); // Add more space above
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(59, 130, 246)); // Tailwind blue-500
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        loginPanel.add(loginButton, gbc);

        // Create Account Button
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 10, 10, 10);
        JButton createAccountButton = new JButton("Create New Account");
        createAccountButton.setFont(new Font("Arial", Font.PLAIN, 14));
        createAccountButton.setBackground(new Color(107, 114, 128)); // Tailwind gray-500
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setPreferredSize(new Dimension(180, 35));
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("CreateAccount");
                clearCreateAccountFields();
            }
        });
        loginPanel.add(createAccountButton, gbc);
    }

    private void performLogin() {
        String accountNumber = loginAccNumField.getText();
        String pin = new String(loginPinField.getPassword());

        if (accountNumber.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both account number and PIN.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loggedInAccount = bankOperations.login(accountNumber, pin);

        if (loggedInAccount != null) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loginAccNumField.setText("");
            loginPinField.setText("");
            showPanel("Dashboard");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid account number or PIN.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createCreateAccountPanel() {
        createAccountPanel = new JPanel(new GridBagLayout());
        createAccountPanel.setBackground(new Color(240, 242, 245));
        createAccountPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create New Bank Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        createAccountPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.5;

        // Account Number
        gbc.gridy = 1; gbc.gridx = 0; createAccountPanel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1; createAccNumField = new JTextField(20); createAccountPanel.add(createAccNumField, gbc);

        // Account Holder Name
        gbc.gridy = 2; gbc.gridx = 0; createAccountPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; createNameField = new JTextField(20); createAccountPanel.add(createNameField, gbc);

        // PIN
        gbc.gridy = 3; gbc.gridx = 0; createAccountPanel.add(new JLabel("4-digit PIN:"), gbc);
        gbc.gridx = 1; createPinField = new JPasswordField(4); createAccountPanel.add(createPinField, gbc);

        // Confirm PIN
        gbc.gridy = 4; gbc.gridx = 0; createAccountPanel.add(new JLabel("Confirm PIN:"), gbc);
        gbc.gridx = 1; createConfirmPinField = new JPasswordField(4); createAccountPanel.add(createConfirmPinField, gbc);

        // Initial Balance
        gbc.gridy = 5; gbc.gridx = 0; createAccountPanel.add(new JLabel("Initial Deposit (₹):"), gbc);
        gbc.gridx = 1; createBalanceField = new JTextField(20); createAccountPanel.add(createBalanceField, gbc);

        // Account Type
        gbc.gridy = 6; gbc.gridx = 0; createAccountPanel.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1; createAccountTypeCombo = new JComboBox<>(new String[]{"Saving", "Current"}); createAccountPanel.add(createAccountTypeCombo, gbc);

        // Phone Number
        gbc.gridy = 7; gbc.gridx = 0; createAccountPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1; createPhoneField = new JTextField(20); createAccountPanel.add(createPhoneField, gbc);

        // Email
        gbc.gridy = 8; gbc.gridx = 0; createAccountPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; createEmailField = new JTextField(20); createAccountPanel.add(createEmailField, gbc);

        // Address
        gbc.gridy = 9; gbc.gridx = 0; createAccountPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; createAddressField = new JTextField(20); createAccountPanel.add(createAddressField, gbc);

        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        JButton createButton = new JButton("Create Account");
        createButton.setFont(new Font("Arial", Font.BOLD, 16));
        createButton.setBackground(new Color(34, 197, 94)); // Tailwind green-500
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setPreferredSize(new Dimension(200, 40));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performCreateAccount();
            }
        });
        gbc.gridy = 10;
        createAccountPanel.add(createButton, gbc);

        gbc.gridy = 11;
        gbc.insets = new Insets(5, 5, 5, 5);
        JButton backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backToLoginButton.setBackground(new Color(107, 114, 128)); // Tailwind gray-500
        backToLoginButton.setForeground(Color.WHITE);
        backToLoginButton.setFocusPainted(false);
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setPreferredSize(new Dimension(150, 35));
        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("Login");
            }
        });
        createAccountPanel.add(backToLoginButton, gbc);
    }

    private void performCreateAccount() {
        String accNum = createAccNumField.getText();
        String name = createNameField.getText();
        String pin = new String(createPinField.getPassword());
        String confirmPin = new String(createConfirmPinField.getPassword());
        String balanceStr = createBalanceField.getText();
        String accType = (String) createAccountTypeCombo.getSelectedItem();
        String phone = createPhoneField.getText();
        String email = createEmailField.getText();
        String address = createAddressField.getText();

        if (accNum.isEmpty() || name.isEmpty() || pin.isEmpty() || balanceStr.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pin.length() != 4 || !pin.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "PIN must be a 4-digit number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pin.equals(confirmPin)) {
            JOptionPane.showMessageDialog(this, "PINs do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal initialBalance;
        try {
            initialBalance = new BigDecimal(balanceStr);
            if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Initial deposit cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid balance amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Default values for branch and IFSC code
        String branch = "Main Branch";
        String ifscCode = "JAVABNK001";

        Account newAccount = new Account(accNum, name, initialBalance, accType, phone, email, address, branch, ifscCode);

        if (bankOperations.createAccount(newAccount, pin)) {
            JOptionPane.showMessageDialog(this, "Account created successfully! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            showPanel("Login");
            clearCreateAccountFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create account. Account number or email might already exist.", "Creation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearCreateAccountFields() {
        createAccNumField.setText("");
        createNameField.setText("");
        createPinField.setText("");
        createConfirmPinField.setText("");
        createBalanceField.setText("");
        createAccountTypeCombo.setSelectedIndex(0);
        createPhoneField.setText("");
        createEmailField.setText("");
        createAddressField.setText("");
    }

    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout(20, 20)); // Add gaps
        dashboardPanel.setBackground(new Color(240, 242, 245));
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top section with welcome and account summary
        JPanel topSection = new JPanel(new GridBagLayout());
        topSection.setOpaque(false); // Make it transparent to show main panel background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Welcome Label
        dashboardWelcomeLabel = new JLabel("Hi, [User Name]", SwingConstants.LEFT);
        dashboardWelcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        dashboardWelcomeLabel.setForeground(new Color(30, 30, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        topSection.add(dashboardWelcomeLabel, gbc);

        // User Info Card
        JPanel userInfoCard = createStyledPanel(new Color(109, 40, 217), new Color(139, 92, 246)); // Purple gradient
        userInfoCard.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0; // Allow it to expand vertically
        gbc.insets = new Insets(10, 0, 10, 10); // Adjust insets for cards
        topSection.add(userInfoCard, gbc);

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(5, 10, 5, 10);
        cardGbc.anchor = GridBagConstraints.WEST;
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.weightx = 1.0;

        JLabel userCardTitle = new JLabel("Your Details");
        userCardTitle.setFont(new Font("Arial", Font.BOLD, 20));
        userCardTitle.setForeground(Color.WHITE);
        cardGbc.gridx = 0; cardGbc.gridy = 0; cardGbc.gridwidth = 2; userInfoCard.add(userCardTitle, cardGbc);

        cardGbc.gridwidth = 1;
        cardGbc.gridy = 1; cardGbc.gridx = 0; userInfoCard.add(new JLabel("Account Number:"), cardGbc);
        cardGbc.gridx = 1; dashboardAccNumLabel = createWhiteLabel(""); userInfoCard.add(dashboardAccNumLabel, cardGbc);

        cardGbc.gridy = 2; cardGbc.gridx = 0; userInfoCard.add(new JLabel("Phone Number:"), cardGbc);
        cardGbc.gridx = 1; dashboardPhoneLabel = createWhiteLabel(""); userInfoCard.add(dashboardPhoneLabel, cardGbc);

        cardGbc.gridy = 3; cardGbc.gridx = 0; userInfoCard.add(new JLabel("Email:"), cardGbc);
        cardGbc.gridx = 1; dashboardEmailLabel = createWhiteLabel(""); userInfoCard.add(dashboardEmailLabel, cardGbc);

        cardGbc.gridy = 4; cardGbc.gridx = 0; userInfoCard.add(new JLabel("Address:"), cardGbc);
        cardGbc.gridx = 1; dashboardAddressLabel = createWhiteLabel(""); userInfoCard.add(dashboardAddressLabel, cardGbc);


        // Account Summary Card
        JPanel accountSummaryCard = createStyledPanel(new Color(59, 130, 246), new Color(34, 211, 238)); // Blue gradient
        accountSummaryCard.setLayout(new GridBagLayout());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 0); // Adjust insets for cards
        topSection.add(accountSummaryCard, gbc);

        cardGbc.gridx = 0; cardGbc.gridy = 0; cardGbc.gridwidth = 2;
        JLabel summaryCardTitle = new JLabel("Current Balance");
        summaryCardTitle.setFont(new Font("Arial", Font.BOLD, 20));
        summaryCardTitle.setForeground(Color.WHITE);
        accountSummaryCard.add(summaryCardTitle, cardGbc);

        cardGbc.gridy = 1;
        cardGbc.gridwidth = 2;
        cardGbc.anchor = GridBagConstraints.CENTER; // Center balance
        dashboardBalanceLabel = createWhiteLabel("₹0.00");
        dashboardBalanceLabel.setFont(new Font("Arial", Font.BOLD, 48));
        accountSummaryCard.add(dashboardBalanceLabel, cardGbc);

        cardGbc.gridwidth = 1;
        cardGbc.gridy = 2; cardGbc.gridx = 0; cardGbc.anchor = GridBagConstraints.WEST; accountSummaryCard.add(new JLabel("Account Type:"), cardGbc);
        cardGbc.gridx = 1; dashboardAccTypeLabel = createWhiteLabel(""); accountSummaryCard.add(dashboardAccTypeLabel, cardGbc);

        cardGbc.gridy = 3; cardGbc.gridx = 0; accountSummaryCard.add(new JLabel("Branch:"), cardGbc);
        cardGbc.gridx = 1; dashboardBranchLabel = createWhiteLabel(""); accountSummaryCard.add(dashboardBranchLabel, cardGbc);

        cardGbc.gridy = 4; cardGbc.gridx = 0; accountSummaryCard.add(new JLabel("IFSC Code:"), cardGbc);
        cardGbc.gridx = 1; dashboardIFSCCodeLabel = createWhiteLabel(""); accountSummaryCard.add(dashboardIFSCCodeLabel, cardGbc);

        dashboardPanel.add(topSection, BorderLayout.NORTH);

        // Sidebar (left)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(55, 65, 81)); // Tailwind gray-700
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(180, getHeight())); // Fixed width

        JLabel logoLabel = new JLabel("OneStopBank", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setForeground(new Color(255, 255, 255));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        sidebar.add(logoLabel);

        addSidebarButton(sidebar, "Dashboard", "Dashboard");
        addSidebarButton(sidebar, "Deposit", "Deposit");
        addSidebarButton(sidebar, "Withdraw", "Withdraw");
        addSidebarButton(sidebar, "Fund Transfer", "Transfer");
        addSidebarButton(sidebar, "Account PIN", "ChangePin");
        addSidebarButton(sidebar, "Transaction History", "TransactionHistory");
        addSidebarButton(sidebar, "Logout", "Logout"); // Logout button in sidebar

        dashboardPanel.add(sidebar, BorderLayout.WEST);

        // Main content area for dashboard (empty for now, transactions will go here)
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false); // Transparent
        dashboardPanel.add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createStyledPanel(Color startColor, Color endColor) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, startColor, w, h, endColor);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 20, 20); // Rounded corners
            }
        };
        panel.setOpaque(false); // Ensure transparency for custom painting
        panel.setForeground(Color.WHITE); // Set default foreground for labels inside
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Padding
        return panel;
    }

    private JLabel createWhiteLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }


    private void addSidebarButton(JPanel sidebar, String text, String panelName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT); // Align text to left
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Make button fill width
        button.setMinimumSize(new Dimension(150, 40));
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(55, 65, 81)); // Match sidebar background
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT); // Align text to left
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // Add left padding for text

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelName.equals("Logout")) {
                    performLogout();
                } else {
                    showPanel(panelName);
                }
            }
        });
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5)); // Add some spacing between buttons
    }

    private void updateDashboard() {
        if (loggedInAccount != null) {
            // Re-fetch account details to get latest balance
            loggedInAccount = bankOperations.getAccountDetails(loggedInAccount.getAccountNumber());

            dashboardWelcomeLabel.setText("Hi, " + loggedInAccount.getAccountHolderName());
            dashboardAccNumLabel.setText(loggedInAccount.getAccountNumber());
            dashboardPhoneLabel.setText(loggedInAccount.getPhoneNumber());
            dashboardEmailLabel.setText(loggedInAccount.getEmail());
            dashboardAddressLabel.setText(loggedInAccount.getAddress());
            dashboardBalanceLabel.setText(CURRENCY_FORMAT.format(loggedInAccount.getBalance()));
            dashboardAccTypeLabel.setText(loggedInAccount.getAccountType());
            dashboardBranchLabel.setText(loggedInAccount.getBranch());
            dashboardIFSCCodeLabel.setText(loggedInAccount.getIfscCode());
        }
    }

    private void performLogout() {
        loggedInAccount = null;
        JOptionPane.showMessageDialog(this, "Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        showPanel("Login");
        loginAccNumField.setText("");
        loginPinField.setText("");
    }

    private void createDepositPanel() {
        depositPanel = createOperationPanel("Deposit Funds", "Deposit");
    }

    private void createWithdrawPanel() {
        withdrawPanel = createOperationPanel("Withdraw Funds", "Withdraw");
    }

    private void createTransferPanel() {
        transferPanel = createOperationPanel("Fund Transfer", "Transfer");
    }

    private void createChangePinPanel() {
        changePinPanel = createOperationPanel("Change Account PIN", "ChangePin");
    }

    private JPanel createOperationPanel(String title, String type) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 1.0;

        if (type.equals("Deposit") || type.equals("Withdraw") || type.equals("Transfer")) {
            gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Amount (₹):"), gbc);
            gbc.gridx = 1; amountField = new JTextField(15); panel.add(amountField, gbc);
        }

        if (type.equals("Transfer")) {
            gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Recipient Account Number:"), gbc);
            gbc.gridx = 1; recipientAccNumField = new JTextField(20); panel.add(recipientAccNumField, gbc);
        }

        if (type.equals("ChangePin")) {
            gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Current PIN:"), gbc);
            gbc.gridx = 1; currentPinField = new JPasswordField(4); panel.add(currentPinField, gbc);

            gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("New 4-digit PIN:"), gbc);
            gbc.gridx = 1; newPinField = new JPasswordField(4); panel.add(newPinField, gbc);

            gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Confirm New PIN:"), gbc);
            gbc.gridx = 1; confirmNewPinField = new JPasswordField(4); panel.add(confirmNewPinField, gbc);
        }

        gbc.gridy = (type.equals("ChangePin") ? 4 : (type.equals("Transfer") ? 3 : 2));
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);

        JButton actionButton = new JButton(title);
        actionButton.setFont(new Font("Arial", Font.BOLD, 16));
        actionButton.setForeground(Color.WHITE);
        actionButton.setFocusPainted(false);
        actionButton.setBorderPainted(false);
        actionButton.setPreferredSize(new Dimension(200, 40));

        switch (type) {
            case "Deposit":
                actionButton.setBackground(new Color(34, 197, 94)); // Green
                actionButton.addActionListener(e -> performDeposit());
                break;
            case "Withdraw":
                actionButton.setBackground(new Color(239, 68, 68)); // Red
                actionButton.addActionListener(e -> performWithdraw());
                break;
            case "Transfer":
                actionButton.setBackground(new Color(124, 58, 237)); // Purple
                actionButton.addActionListener(e -> performTransfer());
                break;
            case "ChangePin":
                actionButton.setBackground(new Color(249, 115, 22)); // Orange
                actionButton.addActionListener(e -> performChangePin());
                break;
        }
        panel.add(actionButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 10, 10);
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(107, 114, 128)); // Gray
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(180, 35));
        backButton.addActionListener(e -> showPanel("Dashboard"));
        panel.add(backButton, gbc);

        return panel;
    }

    private void performDeposit() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(this, "Please login first.", "Error", JOptionPane.ERROR_MESSAGE);
            showPanel("Login");
            return;
        }
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            if (bankOperations.deposit(loggedInAccount.getAccountNumber(), amount)) {
                JOptionPane.showMessageDialog(this, "Deposit successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                amountField.setText("");
                updateDashboard(); // Update balance on dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed. Check amount or account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performWithdraw() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(this, "Please login first.", "Error", JOptionPane.ERROR_MESSAGE);
            showPanel("Login");
            return;
        }
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            if (bankOperations.withdraw(loggedInAccount.getAccountNumber(), amount)) {
                JOptionPane.showMessageDialog(this, "Withdrawal successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                amountField.setText("");
                updateDashboard(); // Update balance on dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Withdrawal failed. Insufficient funds or invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performTransfer() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(this, "Please login first.", "Error", JOptionPane.ERROR_MESSAGE);
            showPanel("Login");
            return;
        }
        try {
            String recipientAccNum = recipientAccNumField.getText();
            BigDecimal amount = new BigDecimal(amountField.getText());

            if (recipientAccNum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter recipient account number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (bankOperations.transfer(loggedInAccount.getAccountNumber(), recipientAccNum, amount)) {
                JOptionPane.showMessageDialog(this, "Transfer successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                amountField.setText("");
                recipientAccNumField.setText("");
                updateDashboard(); // Update balance on dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Transfer failed. Check accounts, amount, or funds.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performChangePin() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(this, "Please login first.", "Error", JOptionPane.ERROR_MESSAGE);
            showPanel("Login");
            return;
        }
        String currentPin = new String(currentPinField.getPassword());
        String newPin = new String(newPinField.getPassword());
        String confirmNewPin = new String(confirmNewPinField.getPassword());

        if (currentPin.isEmpty() || newPin.isEmpty() || confirmNewPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All PIN fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newPin.length() != 4 || !newPin.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "New PIN must be a 4-digit number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPin.equals(confirmNewPin)) {
            JOptionPane.showMessageDialog(this, "New PIN and confirmation do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (bankOperations.changePin(loggedInAccount.getAccountNumber(), currentPin, newPin)) {
            JOptionPane.showMessageDialog(this, "PIN changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            currentPinField.setText("");
            newPinField.setText("");
            confirmNewPinField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to change PIN. Incorrect current PIN or server error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTransactionHistoryPanel() {
        transactionHistoryPanel = new JPanel(new BorderLayout(10, 10));
        transactionHistoryPanel.setBackground(new Color(240, 242, 245));
        transactionHistoryPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Transaction History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        transactionHistoryPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Transaction ID", "Amount", "Type", "Date", "Source Account", "Target Account", "Description"};
        transactionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        transactionTable = new JTable(transactionTableModel);
        transactionTable.setFillsViewportHeight(true); // Table fills the height of the scroll pane
        transactionTable.setRowHeight(25); // Set row height
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        transactionTable.getTableHeader().setBackground(new Color(220, 220, 220));
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Add a subtle border
        transactionHistoryPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setOpaque(false); // Transparent
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(107, 114, 128)); // Gray
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(180, 35));
        backButton.addActionListener(e -> showPanel("Dashboard"));
        bottomPanel.add(backButton);
        transactionHistoryPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateTransactionHistoryTable() {
        transactionTableModel.setRowCount(0); // Clear existing data
        if (loggedInAccount != null) {
            List<Transaction> transactions = bankOperations.getTransactions(loggedInAccount.getAccountNumber());
            for (Transaction tx : transactions) {
                transactionTableModel.addRow(new Object[]{
                        tx.getTransactionId(),
                        CURRENCY_FORMAT.format(tx.getAmount()),
                        tx.getTransactionType(),
                        DATE_FORMAT.format(tx.getTransactionDate()),
                        tx.getSourceAccountNumber() != null ? tx.getSourceAccountNumber() : "N/A",
                        tx.getTargetAccountNumber() != null ? tx.getTargetAccountNumber() : "N/A",
                        tx.getDescription()
                });
            }
        }
    }

    public static void main(String[] args) {
        // Ensure GUI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BankingSystemGUI().setVisible(true);
            }
        });
    }
}
