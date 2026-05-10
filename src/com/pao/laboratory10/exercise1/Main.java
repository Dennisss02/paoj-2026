package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON

        Scanner sc = new Scanner(System.in);
        LinkedList<Tranzactie> tranzactii = new LinkedList<>();
        while(sc.hasNext()) {
            String comanda = sc.next();
            switch(comanda) {
                case "ENQUEUE":
                    int id1 = sc.nextInt();
                    double suma1 = sc.nextDouble();
                    String data1 = sc.next();
                    TipTranzactie tip1 = TipTranzactie.valueOf(sc.next());
                    tranzactii.addLast(new Tranzactie(id1, suma1, data1, tip1));
                    break;
                case "DEQUEUE":
                    if(tranzactii.isEmpty()) {
                        System.out.println("Coada goala.");
                    }
                    else {
                        System.out.println("Procesat: " + tranzactii.peekFirst());
                        tranzactii.removeFirst();
                    }
                    break;
                case "PUSH":
                    int id2 = sc.nextInt();
                    double suma2 = sc.nextDouble();
                    String data2 = sc.next();
                    TipTranzactie tip2 = TipTranzactie.valueOf(sc.next());
                    tranzactii.addFirst(new Tranzactie(id2, suma2, data2, tip2));
                    break;
                case "POP":
                    if(tranzactii.isEmpty()) {
                        System.out.println("Coada goala.");
                    }
                    else {
                        System.out.println("Extras: " + tranzactii.peekFirst());
                        tranzactii.removeFirst();
                    }
                    break;
                case "REMOVE_DEBIT":
                    int cnt_debit = 0;
                    Iterator<Tranzactie> iterator1 = tranzactii.iterator();
                    while(iterator1.hasNext()) {
                        Tranzactie t = iterator1.next();
                        if(t.getTip().equals(TipTranzactie.DEBIT)) {
                            iterator1.remove();
                            cnt_debit++;
                        }
                    }
                    System.out.println("Eliminat " + cnt_debit + " tranzactii DEBIT.");
                    break;
                case "REMOVE_BELOW":
                    double threshold = sc.nextDouble();
                    int cnt_threshold = 0;
                    Iterator<Tranzactie> iterator2 = tranzactii.iterator();
                    while(iterator2.hasNext()) {
                        Tranzactie t = iterator2.next();
                        if(t.getSuma() < threshold) {
                            iterator2.remove();
                            cnt_threshold++;
                        }
                    }
                    System.out.println(String.format("Eliminat %d tranzactii sub %.2f RON.", cnt_threshold, threshold));
                    break;
                case "PRINT":
                    for(Tranzactie t: tranzactii) {
                        System.out.println(t);
                    }
                    break;
                case "SIZE":
                    System.out.println("Dimensiune coada: " + tranzactii.size());
                    break;
            }
        }
    }
}
