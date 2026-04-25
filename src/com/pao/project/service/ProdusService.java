package com.pao.project.service;

import com.pao.project.exception.OperatieInvalidaException;
import com.pao.project.model.Produs;
import com.pao.project.model.enums.CategorieProdus;

import java.util.*;

public class ProdusService {
    private Set<Produs> produse;
    private static int idCounter = 1;

    private ProdusService() { produse = new HashSet<>(); }
    private static class ProdusServiceHolder {
        private static final ProdusService Instance = new ProdusService();
    }
    public static ProdusService getInstance() { return ProdusServiceHolder.Instance; }

    public int getIdCounter() { return idCounter++; }
    public Set<Produs> getProduse() { return produse; }

    public void adaugaProdus(Produs p) {
        if(p == null) {
            throw new OperatieInvalidaException("Produsul nu poate fi nul!");
        }
        produse.add(p);
    }
    public boolean existaProdus(String nume, CategorieProdus categorieProdus) {
        return produse.stream()
                .anyMatch(p -> p.getNume().equals(nume) && p.getCategorieProdus().equals(categorieProdus));
    }
    public Produs cautaProdus(String nume, CategorieProdus categorieProdus) {
        return produse.stream()
                .filter(p -> p.getNume().equals(nume) && p.getCategorieProdus().equals(categorieProdus))
                .findFirst()
                .orElse(null);
    }

    public void stergeProdus(Produs p) {
        produse.remove(p);
    }
}
