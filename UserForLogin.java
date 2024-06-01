package atm;

public class UserForLogin {
    private String accNo;
    private String pin;

    public UserForLogin(String accNo, String pin) {
        this.accNo = accNo;
        this.pin = pin;
    }

    public String getAccNo() {
        return accNo;
    }

    public String getPin() {
        return pin;
    }
}
