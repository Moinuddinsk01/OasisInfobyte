package atm;

public class User {
    private String accNo;
    private String accHolder;
    private String pin;
    private int balance;

    public User(String accNo, String accHolder, String pin) {
        this.accNo = accNo;
        this.accHolder = accHolder;
        this.pin = pin;
    }

    public String getAccNo() {
        return accNo;
    }

    public String getAccHolder() {
        return accHolder;
    }

    public String getPin() {
        return pin;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }
}
