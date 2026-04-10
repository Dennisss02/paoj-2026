package com.pao.laboratory07.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComandaService {
    private ArrayList<Comanda> comenzi;
    private ComandaService() { comenzi = new ArrayList<>(); }
    private static class ComandaServiceHolder {
        private static final ComandaService Instance = new ComandaService();
    }
    public static ComandaService getInstance() { return ComandaServiceHolder.Instance; }

    void addComanda(Comanda c) {
        comenzi.add(c);
    }

    void listaComenzi() {
        System.out.println();
        for(Comanda c: comenzi) {
            System.out.println(c.descriere());
        }
    }

    void stats() {
        System.out.println("\n--- STATS ---");
        Map<Class<? extends Comanda>, Double> medii = comenzi.stream()
                .collect(Collectors.groupingBy(
                        Comanda::getClass,
                        Collectors.averagingDouble(Comanda::pretFinal)
                ));
        for(var entry: medii.entrySet()) {
            Class<? extends Comanda> clasaComanda = entry.getKey();
            Double medie = entry.getValue();
            String numeTip = "";
            if(clasaComanda == ComandaStandard.class) {
                numeTip = "STANDARD";
            }
            else if(clasaComanda == ComandaRedusa.class) {
                numeTip = "DISCOUNTED";
            }
            else {
                numeTip = "GIFT";
            }
            System.out.printf("%s: medie = %.2f lei\n", numeTip, medie);
        }
    }

    void filter(double threshold) {
        System.out.printf("\n--- FILTER (>= %.2f ) ---\n", threshold);
        List<Comanda> comenziFiltrate = comenzi.stream()
                .filter(c -> c.pretFinal() >= threshold)
                .toList();
        for(Comanda c: comenziFiltrate) {
            System.out.println(c.descriere());
        }
    }

    void sort() {
        System.out.println("\n--- SORT (by client, then by pret) ---");
        List<Comanda> comenziSortate = comenzi.stream()
                .sorted(Comparator.comparing(Comanda::getClient)
                        .thenComparing(Comanda::pretFinal))
                .toList();
        for(Comanda c: comenziSortate) {
            System.out.println(c.descriere());
        }
    }

    void special() {
        System.out.println("\n--- SPECIAL (discount > 15%) ---");
        List<Comanda> comenziSpeciale = comenzi.stream()
                .filter(c -> c instanceof ComandaRedusa && ((ComandaRedusa) c).getDiscountProcent() > 15)
                .toList();
        for(Comanda c: comenziSpeciale) {
            System.out.println(c.descriere());
        }
    }
}
