package main;

import java.util.Scanner;
import com.company.Fetch;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Fetch f = null;
        while(f == null) {
            try {
                System.out.print("Username: ");
                String user = s.nextLine();
                System.out.print("API Key: ");
                String key = s.nextLine();
                f = new Fetch(key, user);

            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid username or key");
            }
        }
        System.out.print("How many matches do you want to see? (integer) ");
        int num = s.nextInt();
        f.prevMatches(num);
    }
}
