import java.util.Scanner;
public class Sys {
    public static Scanner in = new Scanner(System.in);

    public static void clearScreen() {
        System.out.print("\033[2J\033[;H"); 
    }

    public static void pause() {
        System.out.print("Press Enter to continue");
        in.nextLine();
    }
    
    public static void print(String s) {
        System.out.println(s);
    }



}