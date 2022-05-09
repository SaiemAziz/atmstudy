package atm;

public class ATM {
    private boolean userAuthenticated;
    private int currentAccountNumber;
    private Screen screen; 
    private Keypad keypad;
    private CashDispenser cashDispenser;
    private DepositSlot depositSlot;
    private BankDatabase bankDatabase;

    private static final int BALANCE_INQUIRY = 1;
    private static final int WITHDRAWAL = 2;
    private static final int DEPOSIT = 3;
    private static final int EXIT = 4;

    public ATM() {
        userAuthenticated = false;
        currentAccountNumber = 0;
        screen = new Screen();
        keypad = new Keypad();
        cashDispenser = new CashDispenser();
        depositSlot = new DepositSlot();
        bankDatabase = new BankDatabase();
    }

    public void run() {
        while(true) {
            while(!userAuthenticated) {
                screen.displayMsg("\nWelcome");
                authenticateUser();
            }

            performTransactions();
            userAuthenticated = false;
            currentAccountNumber = 0;
            screen.displayMsgLine( "\nThank you! Goodbye!" );
        }
    }

    private void authenticateUser(){
        screen.displayMsg("\nPlease enter your account number: ");
        int accountNumber = keypad.getInput();
        screen.displayMsg("\nEnter your PIN: ");
        int pin = keypad.getInput();

        userAuthenticated = bankDatabase.authenticateUser(accountNumber, pin);

        if(userAuthenticated)
            currentAccountNumber = accountNumber;
        else
            screen.displayMsgLine("Invalid account number or PIN. Please try again.");
    }

    private void performTransactions() {
        Transaction currentTransaction = null;
        boolean userExited = false;

        while(!userExited) {
            int mainMenuSelection = displayMainMenu();
            switch(mainMenuSelection) {
                case BALANCE_INQUIRY, WITHDRAWAL, DEPOSIT -> {
                    currentTransaction = createTransaction(mainMenuSelection);
                    currentTransaction.execute();
                }
                case EXIT -> {
                    screen.displayMsgLine( "\nExiting the system..." );
                    userExited = true;
                }
                default -> screen.displayMsgLine( "\nYou did not enter a valid selection. Try again!" );
            }
        }
    }

    private int displayMainMenu(){
        screen.displayMsgLine( "\nMain menu:" );
        screen.displayMsgLine( "1 - View my balance" );
        screen.displayMsgLine( "2 - Withdraw cash" );
        screen.displayMsgLine( "3 - Deposit funds" );
        screen.displayMsgLine( "4 - Exit\n" );
        screen.displayMsgLine( "Enter a choice: " );
        return keypad.getInput();
    }

    private Transaction  createTransaction(int type) {
        Transaction temp = null;
        switch(type) {
            case BALANCE_INQUIRY -> // create new BalanceInquiry transaction
                temp = new BalanceInquiry(currentAccountNumber, screen, bankDatabase );
            case WITHDRAWAL -> // create new Withdrawal transaction
                temp = new Withdrawal( currentAccountNumber, screen, bankDatabase, keypad, cashDispenser );
            case DEPOSIT -> // create new Deposit transaction
                temp = new Deposit( currentAccountNumber, screen, bankDatabase, keypad, depositSlot );
        }
        return temp;
    }
}
