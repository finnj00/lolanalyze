package com.company;

import com.company.util.Fetch;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Fetch f = new Fetch("RGAPI-43d259cf-7aa5-46c5-b035-5815e026b843", "chubbywubbycat");
        System.out.print("How many matches do you want to see? ");
        int num = s.nextInt();
        f.prevMatches(num);
    }
}
