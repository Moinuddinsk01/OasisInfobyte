package Task3;
import java.util.*;

public class BankingSystem {
    public static String name;
    public static int balance=0;
    public static String accno;
    public static ArrayList<String> transactions=new ArrayList<>();

    static void updatebalance(int depositAmount){
        balance=balance+depositAmount;
    }
    static void showbalance(){
        System.out.println(balance);
    }

    static void dashboard(ArrayList<String> arr){
        Scanner in=new Scanner(System.in);
        System.out.println("Choose one of the following options: ");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. Check balance");
        System.out.println("5. Transaction History");
        System.out.println("6. Exit");
        System.out.print("Option -> ");
        int option=in.nextInt();
        switch (option) {
            case 1:
                Credit.creditAmount();
                break;
            case 2:
                Debit.debitAmount();
                break;
            case 3:
                Transfer.transferAmount();
                break;
            case 4:
                Balance.balanceEnquiry();
                break;
            case 5:
                History.transactionHistory(arr);
                break;
            case 6:
                in.close();
                System.exit(0);
        }
        dashboard(arr);
    }

    public static void main(String[] args) {
        System.out.printf("%n==================================== WELCOME TO ATM INTERFACE ====================================%n");
        ArrayList<String> user = new ArrayList<>();
        user = Login.userLogin();
        BankingSystem.dashboard(user);
    }
}
class Login{
    static ArrayList<String> userLogin(){
        Scanner in=new Scanner(System.in);
        ArrayList<String> lst = new ArrayList<>();
        System.out.println("Enter User ID :");
        String userName=in.nextLine();
        System.out.println("Enter User PIN :");
        String userPIN=in.nextLine();
        System.out.println("Enter your Account number :");
        BankingSystem.accno=in.nextLine();
        System.out.println("====================================REGISTRATION SUCCESSFUL==========================================");
        lst.add(userName);
        lst.add(userPIN);
        return lst;
    }
}

class Debit{
    static void debitAmount(){
        Scanner in=new Scanner(System.in);
        System.out.println("Enter amount to withdraw :");
        int withdrawAmount=in.nextInt();
        if(withdrawAmount<=BankingSystem.balance){
            BankingSystem.balance=BankingSystem.balance-withdrawAmount;
            BankingSystem.transactions.add("Withdrawn -"+withdrawAmount);
            System.out.println("Amount Rs"+withdrawAmount+"/- withdrawn successfully");
        }
        else{
            System.out.println("insufficient balance to withdraw the cash");
        }
        System.out.println("=========================================================================================");
    }
}

class Credit{
    static void creditAmount(){
        Scanner in=new Scanner(System.in);
        System.out.print("Enter amount to deposit :");
        int depositAmount=in.nextInt();
        BankingSystem.updatebalance(depositAmount);
        BankingSystem.transactions.add("Deposited -"+depositAmount);
        System.out.println("Amount Rs."+depositAmount+"/- deposit successful!");
        System.out.println("=========================================================================================");
    }
}

class Transfer{
    static void transferAmount(){
        Scanner in=new Scanner(System.in);
        System.out.println("Enter the receiving body:");
        String receiver = in.nextLine();
        System.out.println("Enter the account number of the receiving body");
        int receiverAccNum =in.nextInt();
        System.out.println("Enter the amount to be transferred :");
        int transferAmount=in.nextInt();
        if(transferAmount<=BankingSystem.balance){
            BankingSystem.balance=BankingSystem.balance-transferAmount;
            BankingSystem.transactions.add("Transferred -"+transferAmount);
            System.out.println("Amount Rs."+transferAmount+"/- transferred successfully");
        }
        else{
            System.out.println("insufficient balance to transfer the cash");
        }
        System.out.println("=========================================================================================");
        // in.close();
    }

}

class Balance{
    static void balanceEnquiry(){
        System.out.println("The available balance is : ");
        BankingSystem.showbalance();
        System.out.println("=========================================================================================");
    }
}

class History{
    static void transactionHistory(ArrayList<String> userData){
        System.out.println("Transaction History :");
        int k=1;
        System.out.println("User Name : "+userData.get(0));
        if(BankingSystem.transactions.size()!=0){
        for(String i : BankingSystem.transactions){
            System.out.println("ID."+(k++)+" "+i);
        }
        }else{
            System.out.println("There are no transactions");
        }
        System.out.println("=========================================================================================");
    }
}
