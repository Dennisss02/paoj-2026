package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        List<TranzactieBonus> tranzactii = new ArrayList<>();
        tranzactii.add(new TranzactieBonus(1, "CONT_A", 100.0, "2025-11-06", TipTranzactie.DEBIT));
        tranzactii.add(new TranzactieBonus(2, "CONT_B", 200.0, "2025-11-23", TipTranzactie.CREDIT));
        tranzactii.add(new TranzactieBonus(3, "CONT_C", 300.0, "2025-11-31", TipTranzactie.DEBIT));
        tranzactii.add(new TranzactieBonus(4, "CONT_D", 400.0, "2025-12-02", TipTranzactie.DEBIT));
        tranzactii.add(new TranzactieBonus(5, "CONT_A", 150.0, "2025-12-18", TipTranzactie.CREDIT));
        tranzactii.add(new TranzactieBonus(6, "CONT_B", 250.0, "2026-01-01", TipTranzactie.CREDIT));
        tranzactii.add(new TranzactieBonus(7, "CONT_C", 350.0, "2026-01-12", TipTranzactie.DEBIT));
        tranzactii.add(new TranzactieBonus(8, "CONT_A", 450.0, "2026-02-19", TipTranzactie.DEBIT));
        tranzactii.add(new TranzactieBonus(9, "CONT_B", 125.0, "2026-03-08", TipTranzactie.CREDIT));
        tranzactii.add(new TranzactieBonus(10, "CONT_A", 525.0, "2026-04-20", TipTranzactie.DEBIT));

        // filter
        System.out.println("FILTER");
        List<TranzactieBonus> tranzactiiCredit = tranzactii.stream()
                .filter(t -> t.getTip().equals(TipTranzactie.CREDIT))
                .toList();
        for(TranzactieBonus t: tranzactiiCredit) {
            System.out.println(t);
        }
        System.out.println();

        // suma totala
        double suma_totala = tranzactii.stream()
                .mapToDouble(TranzactieBonus::getSuma).sum();
        System.out.printf("TOTAL PROCESAT: %.2f RON\n", suma_totala);
        System.out.println();

        // grupare dupa luna
        System.out.println("GRUPARE DUPA LUNA");
        Map<String, Double> grupare_luna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(TranzactieBonus::getSuma)
                ));
        for(Map.Entry<String, Double> entry: grupare_luna.entrySet()) {
            System.out.printf("%s: %.2f RON\n", entry.getKey(), entry.getValue());
        }
        System.out.println();

        // top 3
        System.out.println("TOP 3 TRANZACTII");
        List<TranzactieBonus> top_3 = tranzactii.stream()
                .sorted(Comparator.comparingDouble(TranzactieBonus::getSuma).reversed())
                .limit(3)
                .toList();
        for(TranzactieBonus t: top_3) {
            System.out.println(t);
        }
        System.out.println();

        // conturi unice
        List<String> conturi = tranzactii.stream()
                .map(TranzactieBonus::getContSursa)
                .distinct()
                .toList();
        System.out.println("CONTURI SURSA UNICE: " + conturi);
        System.out.println();

        // suma medie
        double suma_medie = tranzactii.stream()
                .mapToDouble(TranzactieBonus::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("SUMA MEDIE: %.2f RON\n", suma_medie);
        System.out.println();

        // extras de cont
        System.out.println("EXTRASE DE CONT");
        Map<String, List<Double>> extrase = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.mapping(TranzactieBonus::getSuma, Collectors.toList())
                ));
        for(Map.Entry<String, List<Double>> entry: extrase.entrySet()) {
            int n = entry.getValue().size();
            double total = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON\n", entry.getKey(), n, total);
        }
    }
}
