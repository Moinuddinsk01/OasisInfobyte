package Task2;

import java.util.*;
public class GuessNumber {
    public static void timer(){
        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
    public static int log2(int n){
        return (int)(Math.log(n) / Math.log(2))+1;
    }
    /* This method "randomNumber()" is used to get the random number 
     between 1 and 100 */
    public static int randomNumber(int num){
        Random random = new Random(); //Random module is used for generating random number....
        return random.nextInt(num)+1; 
        /* nextInt() method of Random module generates a random number 
          within specified bound i.e., if bound is 100, will get number between 0 and 99 inclusively*/
    }
    /* findNumber() method is used for guessing the number whether it is correct or not */
    /* It tells that the guessed number is equal, greater or smaller */
    public static boolean findNumber(int bound,int round){
        System.out.print("      Welcome to round "+round);
        for(int i=0;i<5;i++){
            System.out.print(".");
            timer();
        }
        System.out.println();
        Scanner in = new Scanner(System.in);
        int random_num = randomNumber(bound);
        int chances = log2(bound);
        System.out.println("You have to guess the correct number between 1 to "+bound+". You have only "+chances+" chances.");
        timer();
        int i;
        for(i=0;i<chances;i++){
            System.out.print("Guess the number: ");
            int entered_num = in.nextInt();
            timer();
            if(random_num == entered_num){
                 System.out.println("Congratulations, You guessed the correct number. In round "+round+" You\'ve scored "+(chances-i)+".");
                 return true;
            }
            else if(entered_num > random_num){
                System.out.println("Oops, You guessed the greater number.");
                timer();
            }
            else{
                System.out.println("Oops, You guessed the lesser number.");
                timer();
            }
            
        }
        System.out.println("You are out of chances. In round "+round+" You\'ve scored "+(chances-i)+".");
        timer();
        return false;

    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int bound = 100,round = 1;
        int n;
        while(true){
            boolean flag = findNumber(bound,round);
            round++;
            if(flag){
                timer();
                System.out.println("Enter \'1\' to go to round "+round+ " or \'0\' to quit the game.");
                n = in.nextInt();
                if(n==1){
                    bound *= 5;
                }
                else{
                    break;
                }
            }
            else{
                timer();
                System.out.println("Enter \'1\' to try again.");
                n = in.nextInt();
                if(n==1){
                    System.out.print("      Restarting");
                    for(int i=0;i<5;i++){
                        System.out.print(".");
                        timer();
                    }
                    System.out.println("\n");
                    bound = 100;
                    round = 1;
                }
                else{
                    break;
                }
            }
        }
        in.close();
        timer();
        System.out.println("Thanks for playing the game.");
    }
}
