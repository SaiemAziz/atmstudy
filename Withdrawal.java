package atm;


public class Withdrawal extends Transaction {
    private int amount;
    private Keypad keypad;
    private CashDispenser cashDispenser;

    private final static int CANCELED = 6;

    public Withdrawal(int userAccountNumber, Screen atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad, CashDispenser atmCashDispenser) {
        super(userAccountNumber, atmScreen, atmBankDatabase);

        keypad = atmKeypad;
        cashDispenser = atmCashDispenser;
    }

    @Override
    public void execute(){
        boolean cashDispensed = false;
        double availableBalance = 0;

        BankDatabase bankDatabase = getBankDatabase();
        Screen screen = getScreen();

        do {
            amount = displayMenuOfAmounts();

            if(amount != CANCELED) {
                availableBalance = bankDatabase.getAvailableBalance(getAccountNumber());

                if(amount <= availableBalance) {
                    if(cashDispenser.isSufficientCashAvailable(amount)){
                        bankDatabase.debit( getAccountNumber(), amount );
                        cashDispenser.dispenseCash( amount );
                        cashDispensed = true;

                        screen.displayMsgLine("\nYour cash has been dispensed. Please take your cash now.");
                    }
                    else {
                        screen.displayMsgLine("\nInsufficient cash available in the ATM.\n\nPlease choose a smaller amount.");
                    }
                }
                else {
                    screen.displayMsgLine("\nInsufficient funds available in your account.\n\nPlease choose a smaller amount.");
                }
            }
            else {
               screen.displayMsgLine( "\nCanceling transaction..." );
               return;
            }
        }
        while(!cashDispensed); 
    }

    private int displayMenuOfAmounts() {
        int userChoice = 0;

        Screen screen = getScreen();

        int[] amounts = {0, 20, 40, 60, 100, 200};

        while(userChoice == 0) {
            screen.displayMsgLine( "\nWithdrawal Menu:" );
            screen.displayMsgLine("1 - $20");
            screen.displayMsgLine("2 - $40");
            screen.displayMsgLine("3 - $60");
            screen.displayMsgLine("4 - $100");
            screen.displayMsgLine("5 - $200");
            screen.displayMsgLine("6 - Cancel transaction");
            screen.displayMsg("\nChoose a withdrawal amount: ");

            int input = keypad.getInput();

            switch(input) {
                case 1, 2, 3, 4, 5 -> userChoice = amounts[input];
                case CANCELED -> userChoice = CANCELED;
                default -> screen.displayMsgLine("\nInvalid selection. Try again.");
            }
        }   
        return userChoice;
    }
}