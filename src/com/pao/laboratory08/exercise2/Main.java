package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String INPUT_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_PATH = "src/com/pao/laboratory08/exercise2/rezultate.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă

        BufferedReader fin = new BufferedReader(new FileReader(INPUT_PATH));
        BufferedWriter fout = new BufferedWriter(new FileWriter(OUTPUT_PATH));

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

        Scanner scanner = new Scanner(System.in);
        int prag = scanner.nextInt();
        List<Student> rezultat = studenti.stream()
                        .filter(s -> s.getVarsta() >= prag)
                        .toList();

        for(Student s: rezultat) {
            fout.write(s.toString());
            fout.newLine();
        }
        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + rezultat.size() + " studenti\n");
        for(Student s: rezultat) {
            System.out.println(s);
        }
        System.out.println("\nScris in: rezultate.txt");


        fin.close();
        fout.close();
    }
}

