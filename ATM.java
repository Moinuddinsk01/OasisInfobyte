package atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Scanner;

public class ATM {
    private static int cashInATM = 100000; // Initially our ATM has 100000 Rs
    private static Scanner in = new Scanner(System.in);
    public ATM() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // Connecting to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            stmt = conn.createStatement();

            // Creating ATMDB table
            String createATMDBTable = "CREATE TABLE IF NOT EXISTS ATMDB (" +
                                      "Account_Number VARCHAR(12) PRIMARY KEY, " +
                                      "Account_Holder_Name TEXT NOT NULL, " +
                                      "Pin VARCHAR(4) NOT NULL, " +
                                      "Balance INTEGER NOT NULL)";
            stmt.execute(createATMDBTable);

            // Creating TransactionDB table
            String createTransactionDBTable = "CREATE TABLE IF NOT EXISTS TransactionDB (" +
                                               "Account_Number VARCHAR(12) NOT NULL, " +
                                               "Account_Holder_Name TEXT NOT NULL, " +
                                               "Transaction_Type VARCHAR(100) NOT NULL, " +
                                               "Sender_name TEXT, " +
                                               "Sender_Account_Number VARCHAR(12), " +
                                               "Receiver_name TEXT, " +
                                               "Receiver_Account_Number VARCHAR(12), " +
                                               "Transaction_Date DATETIME NOT NULL, " +
                                               "Available_Balance INTEGER NOT NULL)";
            stmt.execute(createTransactionDBTable);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Closing statement and connection
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setAmount() { // This method is used for refilling the cash in the ATM when the ATM doesn't have enough cash
        cashInATM = 100000;
    }

    public void getDB() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM ATMDB");
            while (res.next()) {
                System.out.println(res.getString("Account_Number") + ", " + res.getString("Account_Holder_Name") + ", " + res.getString("Pin") + ", " + res.getInt("Balance"));
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPin(UserForLogin obj) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("SELECT Pin FROM ATMDB WHERE Pin = ?");
            pstmt.setString(1, obj.getPin());
            ResultSet res = pstmt.executeQuery();
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNameOfCustomer(String accNo, String pin) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("SELECT Account_Holder_Name FROM ATMDB WHERE Account_Number = ? AND Pin = ?");
            pstmt.setString(1, accNo);
            pstmt.setString(2, pin);
            ResultSet res = pstmt.executeQuery();
            if (res.next()) {
                return res.getString("Account_Holder_Name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isCustomer(String accNo) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ATMDB WHERE Account_Number = ?");
            pstmt.setString(1, accNo);
            ResultSet res = pstmt.executeQuery();
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addCustomer(User obj) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ATMDB(Account_Number, Account_Holder_Name, Pin, Balance) VALUES(?,?,?,?)");
            pstmt.setString(1, obj.getAccNo());
            pstmt.setString(2, obj.getAccHolder());
            pstmt.setString(3, obj.getPin());
            pstmt.setInt(4, obj.getBalance());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int checkBalance(User obj) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("SELECT Balance FROM ATMDB WHERE Account_Number = ?");
            pstmt.setString(1, obj.getAccNo());
            ResultSet res = pstmt.executeQuery();
            if (res.next()) {
                return res.getInt("Balance");
            } else {
                return -1; // Return -1 if account not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int checkBalance(Receiver obj) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("SELECT Balance FROM ATMDB WHERE Account_Number = ?");
            pstmt.setString(1, obj.getAccNo());
            ResultSet res = pstmt.executeQuery();
            if (res.next()) {
                return res.getInt("Balance");
            } else {
                return -1; // Return -1 if account not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void deposit(User obj, int amount) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE ATMDB SET Balance = Balance + ? WHERE Account_Number = ?");
            pstmt.setInt(1, amount);
            pstmt.setString(2, obj.getAccNo());
            pstmt.executeUpdate();

            // Adding transaction copy to the TransactionDB for further use
            pstmt = conn.prepareStatement("INSERT INTO TransactionDB(Account_Number, Account_Holder_Name, Transaction_Type, Transaction_Date, Available_Balance) VALUES(?,?,?,?,?)");
            pstmt.setString(1, obj.getAccNo());
            pstmt.setString(2, obj.getAccHolder());
            pstmt.setString(3, "cREDITED");
            pstmt.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setInt(5, checkBalance(obj));
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pinchange(User obj, String newPin) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE ATMDB SET Pin = ? WHERE Pin = ?");
            pstmt.setString(1, newPin);
            pstmt.setString(2, obj.getPin());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int withdraw(User obj, int amount) {
        try {
            if (amount > cashInATM) { // Whenever the money in ATM is not enough for withdrawal, we can report to
                System.out.println("Insufficient money in the ATM.....!");
                System.out.println("Do you want to report? Yes/no: ");
                String check = in.nextLine().trim();
                if (check.toLowerCase().equals("yes")) {
                    setAmount();
                    return 1;
                } else {
                    return 2;
                }
            }
            if (amount > checkBalance(obj)) { // Can't make withdrawal when we don't have enough money
                System.out.println("Insufficient balance. You don't enough amount in your account.");
                System.out.println("Do you want to see the balance? Yes/No: ");
                String check = in.nextLine().trim();
                if (check.toLowerCase().equals("yes")) {
                    int balance = checkBalance(obj);
                    System.out.println(obj.getAccHolder() + "(xxxxxxxx" + obj.getAccNo().substring(obj.getAccNo().length() - 4) + "), " + "your available balance is " + balance);
                    return 2;
                }
            }
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE ATMDB SET Balance = Balance - ? WHERE Account_Number = ?");
            pstmt.setInt(1, amount);
            pstmt.setString(2, obj.getAccNo());
            pstmt.executeUpdate();
            cashInATM -= amount;

            // Adding transaction copy to the TransactionDB for further use
            pstmt = conn.prepareStatement("INSERT INTO TransactionDB(Account_Number, Account_Holder_Name, Transaction_Type, Transaction_Date, Available_Balance) VALUES(?,?,?,?,?)");
            pstmt.setString(1, obj.getAccNo());
            pstmt.setString(2, obj.getAccHolder());
            pstmt.setString(3, "Rs" + amount + " Debited");
            pstmt.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setInt(5, checkBalance(obj));
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
            return 3;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int moneyTransfer(User obj, Receiver recv) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("SELECT Account_Holder_Name FROM ATMDB WHERE Account_Number = ?");
            pstmt.setString(1, recv.getAccNo());
            ResultSet res = pstmt.executeQuery();
            if (res.next() && isCustomer(recv.getAccNo()) && recv.getAccNo().equals(res.getString("Account_Holder_Name"))) { // Checking whether the receiver exists or not
                System.out.println("Enter the amount that you want to transfer: ");
                int amount = Integer.parseInt(in.nextLine().trim());
                if (checkBalance(obj) < amount) { // Can't make transfer when we don't have enough money
                    System.out.println("Insufficient balance. You don't enough amount in your account to transfer.");
                    return 1;
                }
                // Updating their balances after transfer
                pstmt = conn.prepareStatement("UPDATE ATMDB SET Balance = Balance - ? WHERE Account_Number = ?");
                pstmt.setInt(1, amount);
                pstmt.setString(2, obj.getAccNo());
                pstmt.executeUpdate();

                pstmt = conn.prepareStatement("UPDATE ATMDB SET Balance = Balance + ? WHERE Account_Number = ?");
                pstmt.setInt(1, amount);
                pstmt.setString(2, recv.getAccNo());
                pstmt.executeUpdate();

                // Updating transaction copies to the Transaction DB
                pstmt = conn.prepareStatement("INSERT INTO TransactionDB(Account_Number, Account_Holder_Name, Transaction_Type, Receiver_name, Receiver_Account_Number, Transaction_Date, Available_Balance) VALUES(?,?,?,?,?,?,?)");
                pstmt.setString(1, obj.getAccNo());
                pstmt.setString(2, obj.getAccHolder());
                pstmt.setString(3, "Rs" + amount + " Debited");
                pstmt.setString(4, recv.getAccNo());
                pstmt.setString(5, recv.getAccNo());
                pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
                pstmt.setInt(7, checkBalance(obj));
                pstmt.executeUpdate();

                pstmt = conn.prepareStatement("INSERT INTO TransactionDB(Account_Number, Account_Holder_Name, Transaction_Type, Sender_name, Sender_Account_Number, Transaction_Date, Available_Balance) VALUES(?,?,?,?,?,?,?)");
                pstmt.setString(1, recv.getAccNo());
                pstmt.setString(2, recv.getAccNo());
                pstmt.setString(3, "Rs" + amount + " Credited");
                pstmt.setString(4, obj.getAccHolder());
                pstmt.setString(5, obj.getAccNo());
                pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
                pstmt.setInt(7, checkBalance(recv));
                pstmt.executeUpdate();

                System.out.println("Rs" + amount + " has been transferred from " + obj.getAccHolder() + "(xxxxxxxx" + obj.getAccNo().substring(obj.getAccNo().length() - 4) + ") to " + recv.getAccNo() + "(xxxxxxxx" + recv.getAccNo().substring(recv.getAccNo().length() - 4) + ")");
                return 1;
            } else {
                System.out.println("Details, you have provided, doesn't exist. Check the details.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void getMinistatement(User obj) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
            PreparedStatement pstmt = conn.prepareStatement("SELECT Account_Number, Account_Holder_Name, Transaction_Type, COALESCE(Sender_name, '') AS Sender_name, COALESCE(Sender_Account_Number, '') AS Sender_Account_Number, COALESCE(Receiver_name, '') AS Receiver_name, COALESCE(Receiver_Account_Number, '') AS Receiver_Account_Number, Transaction_Date, Available_Balance FROM TransactionDB WHERE Account_Number = ? LIMIT 10");
            pstmt.setString(1, obj.getAccNo());
            ResultSet res = pstmt.executeQuery();

            String[] column = {"Account_Number", "Account_Holder_Name", "Transaction_Type", "Sender_name", "Sender_Account_Number", "Receiver_name", "Receiver_Account_Number", "Transaction_Date", "Available_Balance"};
            int[] max_widths = new int[column.length];
            for (int i = 0; i < column.length; i++) {
                max_widths[i] = column[i].length();
            }
            while (res.next()) {
                for (int i = 0; i < column.length; i++) {
                    max_widths[i] = Math.max(max_widths[i], res.getString(column[i]).length());
                }
            }
            String headerLine = "";
            for (int i = 0; i < column.length; i++) {
                headerLine += String.format("%-" + (max_widths[i] + 2) + "s", column[i]);
            }
            System.out.println(headerLine);
            System.out.println("-".repeat(headerLine.length()));

            res.beforeFirst();
            while (res.next()) {
                String rowLine = "";
                for (int i = 0; i < column.length; i++) {
                    rowLine += String.format("%-" + (max_widths[i] + 2) + "s", res.getString(column[i]));
                }
                System.out.println(rowLine);
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
