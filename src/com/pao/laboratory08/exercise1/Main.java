package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează

        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));

        ArrayList<Student> studenti = new ArrayList<>();
        String linie;
        while ((linie = fin.readLine()) != null) {
            if(linie.trim().isEmpty()) {
                continue;
            }

            String[] param = linie.split(",");
            String nume = param[0].trim();
            int varsta = Integer.parseInt(param[1].trim());
            String oras = param[2].trim();
            String strada = param[3].trim();
            Adresa adresa = new Adresa(oras, strada);
            studenti.add(new Student(nume, varsta, adresa));
        }
        fin.close();

        Scanner scanner = new Scanner(System.in);
        String[] comanda = scanner.nextLine().split(" ");
        if(comanda[0].equals("PRINT")) {
            for(Student student: studenti) {
                System.out.println(student);
            }
        }
        else if(comanda[0].equals("SHALLOW")) {
            Student studentGasit = studenti.stream()
                    .filter(s -> s.getNume().equals(comanda[1]))
                    .findFirst()
                    .orElse(null);
            if (studentGasit == null) {
                System.out.println("Studentul nu a fost gasit.");
            } else {
                Student clona = studentGasit.shallowClone();
                clona.setOras("MODIFICAT");
                System.out.println("Original: " + studentGasit);
                System.out.println("Clona: " + clona);
            }
        }
        else if(comanda[0].equals("DEEP")) {
            Student studentGasit = studenti.stream()
                    .filter(s -> s.getNume().equals(comanda[1]))
                    .findFirst()
                    .orElse(null);
            if (studentGasit == null) {
                System.out.println("Studentul nu a fost gasit.");
            } else {
                Student clona = studentGasit.deepClone();
                clona.setOras("MODIFICAT");
                System.out.println("Original: " + studentGasit);
                System.out.println("Clona: " + clona);
            }
        }
        scanner.close();
    }
}
