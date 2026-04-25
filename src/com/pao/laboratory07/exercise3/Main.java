package com.pao.laboratory07.exercise3;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ComandaService service = ComandaService.getInstance();
        int n = Integer.parseInt(scanner.nextLine().trim());
        for(int i = 0; i < n; i++) {
            String[] tokens = scanner.nextLine().trim().split(" ");
            if(tokens[0].equals("STANDARD")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                String client = tokens[3];
                Comanda c = new ComandaStandard(nume, pret, client);
                service.addComanda(c);
            }
            else if(tokens[0].equals("DISCOUNTED")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                int discount = Integer.parseInt(tokens[3]);
                String client = tokens[4];
                Comanda c = new ComandaRedusa(nume, pret, discount, client);
                service.addComanda(c);
            }
            else if(tokens[0].equals("GIFT")) {
                String nume = tokens[1];
                String client = tokens[2];
                Comanda c = new ComandaGratuita(nume, client);
                service.addComanda(c);
            }
        }

        service.listaComenzi();

        while(true) {
            String operation = scanner.nextLine().trim();
            if(operation.equals("STATS")) {
                service.stats();
            }
            else if(operation.startsWith("FILTER ")) {
                try {
                    double threshold = Double.parseDouble(operation.split(" ")[1]);
                    service.filter(threshold);
                }
                catch(Exception e) {
                    System.out.println("Operatie invalida!");
                }
            }
            else if(operation.equals("SORT")) {
                service.sort();
            }
            else if(operation.equals("SPECIAL")) {
                service.special();
            }
            else if(operation.equals("QUIT")) {
                System.out.println("\nGoodbye!\n");
                return;
            }
        }
    }
}
