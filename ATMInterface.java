import java.util.ArrayList;
import java.util.Scanner;

// ── Step 4: BankAccount class ─────────────────────────────────────────────────
class BankAccount {

    private String accountHolder;
    private String accountNumber;
    private double balance;
    private ArrayList<String> transactionHistory;

    public BankAccount(String accountHolder, String accountNumber, double initialBalance) {
        this.accountHolder      = accountHolder;
        this.accountNumber      = accountNumber;
        this.balance            = initialBalance;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add("Account opened with initial balance: ₹" +
                String.format("%.2f", initialBalance));
    }

    // Getters
    public String getAccountHolder() { return accountHolder; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance()        { return balance; }
    public ArrayList<String> getTransactionHistory() { return transactionHistory; }

    // Step 3: checkBalance()
    public double checkBalance() {
        return balance;
    }

    // Step 3: deposit(amount)
    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        if (amount > 100000) return false;   // max single deposit limit
        balance += amount;
        transactionHistory.add(String.format("DEPOSIT   : +₹%.2f  |  Balance: ₹%.2f",
                amount, balance));
        return true;
    }

    // Step 3: withdraw(amount)
    public int withdraw(double amount) {
        if (amount <= 0)          return -1;  // invalid amount
        if (amount > balance)     return -2;  // insufficient balance
        if (amount > 50000)       return -3;  // exceeds withdrawal limit
        if (amount % 100 != 0)    return -4;  // must be multiple of 100
        balance -= amount;
        transactionHistory.add(String.format("WITHDRAWAL: -₹%.2f  |  Balance: ₹%.2f",
                amount, balance));
        return 1; // success
    }
}


// ── Step 1: ATM class ─────────────────────────────────────────────────────────
class ATM {

    // Step 5: ATM connected to BankAccount
    private BankAccount account;
    private Scanner scanner;
    private static final String ATM_ID = "ATM-CODSOFT-001";

    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    // ── Display welcome screen ────────────────────────────────────────────────
    private void showWelcome() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║           CODSOFT BANK  ATM                 ║");
        System.out.println("║              " + ATM_ID + "              ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.printf( "║  Welcome,  %-33s║%n", account.getAccountHolder() + "!");
        System.out.printf( "║  Account : %-33s║%n", account.getAccountNumber());
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    // ── Step 2: Main menu ─────────────────────────────────────────────────────
    private void showMenu() {
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│           MAIN MENU              │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Check Balance                │");
        System.out.println("│  2. Deposit                      │");
        System.out.println("│  3. Withdraw                     │");
        System.out.println("│  4. Transaction History          │");
        System.out.println("│  5. Exit                         │");
        System.out.println("└──────────────────────────────────┘");
        System.out.print("  Choose an option (1-5): ");
    }

    // ── Option 1: Check Balance ───────────────────────────────────────────────
    private void handleCheckBalance() {
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│          ACCOUNT BALANCE         │");
        System.out.println("├──────────────────────────────────┤");
        System.out.printf( "│  Available Balance : ₹%-11.2f│%n", account.checkBalance());
        System.out.println("└──────────────────────────────────┘");
    }

    // ── Option 2: Deposit ─────────────────────────────────────────────────────
    private void handleDeposit() {
        System.out.println("\n  ── DEPOSIT ──────────────────────────");
        System.out.println("  Max deposit per transaction: ₹1,00,000");
        System.out.print("  Enter amount to deposit: ₹");

        double amount = getValidAmount();
        if (amount == -1) return;

        // Step 7: Display appropriate messages
        if (account.deposit(amount)) {
            System.out.println("\n  ✅ ₹" + String.format("%.2f", amount)
                    + " deposited successfully!");
            System.out.printf("  Updated Balance : ₹%.2f%n", account.checkBalance());
        } else if (amount > 100000) {
            System.out.println("\n  ❌ Deposit failed! Maximum limit is ₹1,00,000.");
        } else {
            System.out.println("\n  ❌ Invalid deposit amount.");
        }
    }

    // ── Option 3: Withdraw ────────────────────────────────────────────────────
    private void handleWithdraw() {
        System.out.println("\n  ── WITHDRAWAL ───────────────────────");
        System.out.println("  Max withdrawal per transaction : ₹50,000");
        System.out.println("  Amount must be a multiple of  : ₹100");
        System.out.printf( "  Current Balance                : ₹%.2f%n",
                account.checkBalance());
        System.out.print("\n  Enter amount to withdraw: ₹");

        double amount = getValidAmount();
        if (amount == -1) return;

        // Step 6: Validate & Step 7: Display messages
        int result = account.withdraw(amount);
        switch (result) {
            case 1:
                System.out.println("\n  ✅ ₹" + String.format("%.2f", amount)
                        + " dispensed successfully!");
                System.out.printf("  Remaining Balance : ₹%.2f%n", account.checkBalance());
                break;
            case -1:
                System.out.println("\n  ❌ Invalid amount! Enter a positive value.");
                break;
            case -2:
                System.out.printf("\n  ❌ Insufficient balance!%n");
                System.out.printf("  Available: ₹%.2f | Requested: ₹%.2f%n",
                        account.checkBalance(), amount);
                break;
            case -3:
                System.out.println("\n  ❌ Amount exceeds the withdrawal limit of ₹50,000.");
                break;
            case -4:
                System.out.println("\n  ❌ Amount must be a multiple of ₹100.");
                break;
        }
    }

    // ── Option 4: Transaction History ─────────────────────────────────────────
    private void handleTransactionHistory() {
        ArrayList<String> history = account.getTransactionHistory();
        System.out.println("\n┌──────────────────────────────────────────────────┐");
        System.out.println("│               TRANSACTION HISTORY               │");
        System.out.println("├──────────────────────────────────────────────────┤");
        if (history.isEmpty()) {
            System.out.println("│  No transactions yet.                            │");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.printf("│  %2d. %-45s│%n", (i + 1), history.get(i));
            }
        }
        System.out.println("└──────────────────────────────────────────────────┘");
    }

    // ── Input helper: get valid numeric amount ────────────────────────────────
    private double getValidAmount() {
        if (!scanner.hasNextDouble()) {
            System.out.println("\n  ⚠  Invalid input! Please enter a numeric value.");
            scanner.nextLine();
            return -1;
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();
        if (amount <= 0) {
            System.out.println("\n  ⚠  Amount must be greater than zero.");
            return -1;
        }
        return amount;
    }

    // ── Step 2: Run the ATM session ───────────────────────────────────────────
    public void run() {
        showWelcome();

        boolean running = true;
        while (running) {
            showMenu();

            if (!scanner.hasNextInt()) {
                System.out.println("\n  ⚠  Invalid choice! Enter a number between 1 and 5.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            // Step 7: Appropriate messages for each option
            switch (choice) {
                case 1: handleCheckBalance();      break;
                case 2: handleDeposit();           break;
                case 3: handleWithdraw();          break;
                case 4: handleTransactionHistory();break;
                case 5:
                    running = false;
                    System.out.println("\n╔══════════════════════════════════════════════╗");
                    System.out.println("║   Thank you for using CODSOFT Bank ATM!     ║");
                    System.out.println("║   Please collect your card. Goodbye! 👋     ║");
                    System.out.println("╚══════════════════════════════════════════════╝\n");
                    break;
                default:
                    System.out.println("\n  ⚠  Invalid option! Please choose between 1-5.");
            }
        }

        scanner.close();
    }
}


// ── Main class ────────────────────────────────────────────────────────────────
public class ATMInterface {

    public static void main(String[] args) {
        // Pre-loaded account (simulating card insert)
        BankAccount userAccount = new BankAccount(
                "Kaviyadharshini",   // Account holder name
                "ACC-2024-00123",    // Account number
                25000.00             // Initial balance
        );

        // Step 5: Connect ATM to BankAccount
        ATM atm = new ATM(userAccount);
        atm.run();
    }
}