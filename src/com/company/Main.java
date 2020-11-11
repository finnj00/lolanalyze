package com.company;

import com.company.util.ChampDict;
import com.company.util.Fetch;

public class Main {

    public static void main(String[] args) {
        Fetch f = new Fetch("RGAPI-43d259cf-7aa5-46c5-b035-5815e026b843", "chubbywubbycat");
        f.match();
    }
}
