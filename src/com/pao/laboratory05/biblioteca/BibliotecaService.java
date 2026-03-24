package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti;

    private BibliotecaService() {
        carti = new Carte[0];
    }

    private static class BibliotecaHolder {
        private static final BibliotecaService Instance = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return BibliotecaHolder.Instance;
    }

    void addCarte(Carte carte) {
        int n = carti.length;
        Carte[] newcarti = new Carte[n + 1];
        System.arraycopy(carti, 0, newcarti, 0, n);
        newcarti[n] = carte;
        carti = newcarti;
        System.out.println("Carte adaugata: " + carte.getTitlu());
    }

    void listSortedByRating() {
        Carte[] clone = carti.clone();
        Arrays.sort(clone);
        for(Carte c: clone) {
            System.out.println(c);
        }
    }

    void listSortedBy(Comparator<Carte> comparator) {
        Carte[] clone = carti.clone();
        Arrays.sort(clone, comparator);
        for(Carte c: clone) {
            System.out.println(c);
        }
    }
}
