package com.company;

import com.company.util.Fetch;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Fetch f = null;
        while(f == null) {
            try {
                /*System.out.print("Username: ");
                String user = s.nextLine();
                System.out.print("API Key: ");
                String key = s.nextLine();*/
                f = new Fetch("RGAPI-4c79f535-1766-451b-8a77-64c13aae574d", "chubbywubbycat");

            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid username or key");
            }
        }
        System.out.print("How many matches do you want to see? (integer) ");
        int num = s.nextInt();
        f.prevMatches(num);
    }
}
