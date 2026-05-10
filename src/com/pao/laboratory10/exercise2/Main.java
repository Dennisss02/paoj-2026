package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        // 2. Procesează comenzile din stdin până la EOF:
        //
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON

        List<Tranzactie> tranzactii = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        for(int i = 0; i < N; i++) {
            int id = sc.nextInt();
            double suma = sc.nextDouble();
            String data = sc.next();
            TipTranzactie tip = TipTranzactie.valueOf(sc.next());
            tranzactii.add(new Tranzactie(id, suma, data, tip));
        }
        while(sc.hasNext()) {
            String comanda = sc.next();
            switch(comanda) {
                case "UNIQUE_IDS":
                    LinkedHashSet<Integer> unique_ids = new LinkedHashSet<>();
                    for(Tranzactie t: tranzactii) {
                        unique_ids.add(t.getId());
                    }
                    System.out.println("IDs unice (" + unique_ids.size() + "): " + unique_ids);
                    break;
                case "MONTHLY_REPORT":
                    TreeMap<String, double[]> raport_dupa_luna = new TreeMap<>();
                    for(Tranzactie t: tranzactii) {
                        String luna = t.getData().substring(0, 7);
                        double[] totaluri = raport_dupa_luna.computeIfAbsent(luna, r -> new double[2]);
                        if(t.getTip().equals(TipTranzactie.CREDIT)) {
                            totaluri[0] += t.getSuma();
                        }
                        else {
                            totaluri[1] += t.getSuma();
                        }
                    }
                    for(Map.Entry<String, double[]> entry: raport_dupa_luna.entrySet()) {
                        System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON\n", entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                    }
                    break;
                case "TOP":
                    int n = sc.nextInt();
                    List<Tranzactie> lista_topn = new ArrayList<>(tranzactii);
                    lista_topn.sort(new TranzactieSumaComparator());
                    lista_topn.reversed();
                    List<Tranzactie> sublista_topn = new ArrayList<>(lista_topn.subList(0, n));
                    System.out.println("Top " + n + ":");
                    for(int i = 0; i < n; i++) {
                        System.out.println(sublista_topn.get(i));
                    }
                    break;
                case "SORT_ASC":
                    tranzactii.sort(new TranzactieSumaComparator());
                    for(Tranzactie t: tranzactii) {
                        System.out.println(t);
                    }
                    break;
                case "SORT_DESC":
                    tranzactii.sort(new TranzactieSumaComparator());
                    tranzactii.reversed();
                    for(Tranzactie t: tranzactii) {
                        System.out.println(t);
                    }
                    break;
                case "REVERSE":
                    tranzactii.reversed();
                    for(Tranzactie t: tranzactii) {
                        System.out.println(t);
                    }
                    break;
                case "MIN_MAX":
                    Tranzactie[] min_max = new Tranzactie[2];
                    min_max[0] = Collections.min(tranzactii, new TranzactieSumaComparator());
                    min_max[1] = Collections.max(tranzactii, new TranzactieSumaComparator());
                    System.out.println("MIN: " + min_max[0]);
                    System.out.println("MAX: " + min_max[1]);
                    break;
                case "CME_DEMO":
                    try {
                        for (Tranzactie t : tranzactii) {
                            tranzactii.remove(t);
                        }
                    }
                    catch(ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");

                    }
                    break;
            }
        }
    }
}
