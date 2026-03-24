package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        AngajatService angajatService = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");
            // citește opțiunea și execută acțiunea
            int option = scanner.nextInt();
            switch(option) {
                case 1:
                    System.out.print("Nume: ");
                    String nume = scanner.next();
                    System.out.print("Departament (nume): ");
                    String numeDept = scanner.next();
                    System.out.print("Departament (locatie): ");
                    String locatie = scanner.next();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();
                    angajatService.addAngajat(new Angajat(nume, new Departament(numeDept, locatie), salariu));
                    System.out.println("Angajat adaugat: " + nume);
                    break;
                case 2:
                    System.out.println("--- Angajati dupa salariu (descrescator) ---");
                    angajatService.listBySalary();
                    break;
                case 3:
                    System.out.print("Departament: ");
                    String dept = scanner.next();
                    System.out.println("--- Angajati din " + dept + " ---");
                    angajatService.findMyDepartment(dept);
                    break;
                case 0:
                    System.out.println("La revedere!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opțiune invalidă. Încearcă din nou.");
            }
        }
    }
}
