package atm;

import atm.Receiver;
import atm.User;
import atm.UserForLogin;
import java.util.Scanner;
import java.util.Random;

public class Main {
    private static ATM atm = new ATM();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===============WELCOME TO ATM===============");
        start();
    }

    private static String generateAccountNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }

    private static void start() {
        System.out.println("Choose the option: \n1. Login \n2. New user \n3. Exit");
        String check = scanner.nextLine();
        switch (check) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "3":
                System.exit(0);
                break;
            default:
                System.out.print("Choose the correct option");
                for (int i = 0; i < 5; i++) {
                    System.out.print(".");
                    try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                System.out.println();
                start();
        }
    }

    private static void balanceCheck(User user) {
        int balance = atm.checkBalance(user);
        System.out.println(user.getAccHolder() + "(xxxxxxxx" + user.getAccNo().substring(user.getAccNo().length() - 4) + "), your available balance is " + balance);
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Thank you for using ATM");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        start();
    }

    private static void chooseFunctionality(User user) {
        System.out.println("What do you want to do? \n1. Withdraw \n2. Deposit \n3. Pin change \n4. Balance check \n5. Mini statement \n6. Transfer money \n7. Exit");
        String check = scanner.nextLine();
        switch (check) {
            case "1":
                System.out.println("......Quick withdrawal.....\nSelect the amount \n1. 100 \n2. 500 \n3. 1000 \n4. 5000 \n5. 10000 \n6. Other");
                String checkAmount = scanner.nextLine();
                int amount = 0;
                switch (checkAmount) {
                    case "1":
                        amount = 100;
                        break;
                    case "2":
                        amount = 500;
                        break;
                    case "3":
                        amount = 1000;
                        break;
                    case "4":
                        amount = 5000;
                        break;
                    case "5":
                        amount = 10000;
                        break;
                    case "6":
                        System.out.print("Enter the amount: ");
                        amount = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        break;
                    default:
                        System.out.println("Choose the option properly...");
                        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                        start();
                }
                int res = atm.withdraw(user, amount);
                if (res == 3) {
                    System.out.println("Rs" + amount + " has been debited from xxxxxxxx" + user.getAccNo().substring(user.getAccNo().length() - 4));
                    System.out.print("Do you want to check the available balance? Yes/No: ");
                    String checkBal = scanner.nextLine();
                    if (checkBal.equalsIgnoreCase("yes")) {
                        balanceCheck(user);
                    } else {
                        start();
                    }
                } else if (res == 1) {
                    System.out.println("Thank you for reporting....");
                    try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                    System.out.println("You can use the ATM now.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                    start();
                } else {
                    start();
                }
                break;
            case "2":
                System.out.print("Enter the amount: ");
                amount = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                atm.deposit(user, amount);
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("Rs" + amount + " has been credited successfully into xxxxxxxx" + user.getAccNo().substring(user.getAccNo().length() - 4));
                System.out.print("Do you want to check the available balance? Yes/No: ");
                String checkBal = scanner.nextLine();
                if (checkBal.equalsIgnoreCase("yes")) {
                    balanceCheck(user);
                } else {
                    start();
                }
                break;
            case "3":
                System.out.print("Set your 4 digit new pin: ");
                String newPin = scanner.nextLine();
                while (newPin.equals(user.getPin())) {
                    System.out.println("New pin can't be same as old pin...!");
                    System.out.print("Set your 4 digit new pin: ");
                    newPin = scanner.nextLine();
                }
                atm.pinchange(user, newPin);
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println(user.getAccHolder() + "(xxxxxxxx" + user.getAccNo().substring(user.getAccNo().length() - 4) + "), your pin has been changed.");
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                start();
                break;
            case "4":
                balanceCheck(user);
                break;
            case "5":
                atm.getMinistatement(user);
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                start();
                break;
            case "6":
                System.out.print("Enter the account number of the receiver: ");
                String accNo = scanner.nextLine();
                System.out.print("Enter the name of the receiver: ");
                String accHolder = scanner.nextLine();
                Receiver receiver = new Receiver(accNo, accHolder);
                res = atm.moneyTransfer(user, receiver);
                if (res == 1) {
                    System.out.print("Do you want to check the available balance? Yes/No: ");
                    checkBal = scanner.nextLine();
                    if (checkBal.equalsIgnoreCase("yes")) {
                        balanceCheck(user);
                    } else {
                        start();
                    }
                } else {
                    start();
                }
                break;
            case "7":
                exit1();
                break;
            default:
                System.out.println("Select the option properly.....!");
                chooseFunctionality(user);
        }
    }

    private static void login() {
        System.out.print("Enter your account number: ");
        String accNo = scanner.nextLine();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.print("Enter your pin: ");
        String pin = scanner.nextLine();
        UserForLogin objFor = new UserForLogin(accNo, pin);
        if (atm.isCustomer(objFor.getAccNo())) {
            if (atm.checkPin(objFor)) {
                String name = atm.getNameOfCustomer(objFor.getAccNo(), objFor.getPin());
                User user = new User(objFor.getAccNo(), name, objFor.getPin());
                System.out.println("Hello " + user.getAccHolder() + ",");
                chooseFunctionality(user);
            } else {
                System.out.println("Wrong PIN....!");
                start();
            }
        } else {
            System.out.println("You are not a customer. Do register...!");
            start();
        }
    }

    private static void register() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        String accNo = generateAccountNumber();
        while (atm.isCustomer(accNo)) {
            accNo = generateAccountNumber();
        }
        System.out.println(accNo + ", this is your Account number.");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.print("Set your 4 digit pin: ");
        String pin = scanner.nextLine();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.print("Do you want to deposit cash now? Yes/No: ");
        String depCheck = scanner.nextLine();
        int balance = 0;
        if (depCheck.equalsIgnoreCase("yes")) {
            System.out.print("Enter the cash: ");
            balance = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        }
        User user = new User(accNo, name, pin);
        user.setBalance(balance);
        atm.addCustomer(user);
        start();
    }

    private static void exit1() {
        start();
    }
}
