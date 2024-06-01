package atm;

public class Receiver {
    private String accNo;
    private String name;

    public Receiver(String accNo, String name) {
        this.accNo = accNo;
        this.name = name;
    }

    public String getAccNo() {
        return accNo;
    }

    public String getName() {
        return name;
    }
}
