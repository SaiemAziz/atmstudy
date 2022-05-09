package atm;


public class Screen {
    public void displayMsg(String message) {
        System.out.println(message);
    }

    public void displayMsgLine(String message) {
        System.out.println(message);
    }

    public void displayDollarAmount(double amount) {
        System.out.printf("$%,.2f", amount);
    }
}