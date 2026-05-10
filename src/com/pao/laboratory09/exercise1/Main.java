package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();
        for(int i = 0; i < N; i++) {
            int id = sc.nextInt();
            double suma = sc.nextDouble();
            String data = sc.next();
            String contSursa = sc.next();
            String contDestinatie = sc.next();
            TipTranzactie tip = TipTranzactie.valueOf(sc.next());
            Tranzactie tranzactie = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            tranzactie.setNote("procesat");
            tranzactii.add(tranzactie);
        }

        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs();
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(tranzactii);
        }

        List<Tranzactie> tranzactii_deserializate;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            tranzactii_deserializate = (List<Tranzactie>)ois.readObject();
        }

        while(sc.hasNext()) {
            String comanda = sc.next();
            switch(comanda) {
                case "LIST":
                    for(Tranzactie t: tranzactii_deserializate) {
                        System.out.println(t);
                    }
                    break;
                case "FILTER":
                    String prefix = sc.next();
                    boolean found = false;
                    for(Tranzactie t: tranzactii_deserializate) {
                        if(t.getData().startsWith(prefix)) {
                            found = true;
                            System.out.println(t);
                        }
                    }
                    if(!found) {
                        System.out.println("Niciun rezultat.");
                    }
                    break;
                case "NOTE":
                    int id_cautat = sc.nextInt();
                    Tranzactie tranzactie_gasita = null;
                    for(Tranzactie t: tranzactii_deserializate) {
                        if(t.getId() == id_cautat) {
                            tranzactie_gasita = t;
                            break;
                        }
                    }
                    if(tranzactie_gasita != null) {
                        System.out.println("NOTE[" + id_cautat + "]: " + tranzactie_gasita.getNote());
                    }
                    else {
                        System.out.println("NOTE[" + id_cautat + "]: not found");
                    }
                default:
                    break;
            }
        }
        sc.close();
    }
}
