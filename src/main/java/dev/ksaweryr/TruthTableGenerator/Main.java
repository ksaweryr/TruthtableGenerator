package dev.ksaweryr.TruthTableGenerator;

import dev.ksaweryr.TruthTableGenerator.TruthTable.TruthTable;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("> ");
        TruthTable tt = new TruthTable(sc.nextLine());

        System.out.println(tt);
    }
}
