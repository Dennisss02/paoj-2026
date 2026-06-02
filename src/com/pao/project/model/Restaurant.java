package com.pao.project.model;

import com.pao.project.exception.*;
import com.pao.project.model.enums.CategorieProdus;

import java.util.*;
import java.util.stream.Collectors;

public class Restaurant implements Comparable<Restaurant> {
    private int id;
    private String nume;
    private Manager manager;
    private Set<ArticolMeniu> meniu;

    public Restaurant(int id, String nume, Manager manager) {
        this.id = id;
        this.nume = nume;
        this.manager = manager;
        this.meniu = new TreeSet<>();
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNume() { return nume; }
    public Manager getManager() { return manager; }

    public ArticolMeniu getArticolDupaNume(String nume) {
        return meniu.stream()
                .filter(a -> a.getProdus().getNume().equalsIgnoreCase(nume))
                .findFirst()
                .orElseThrow(() -> new EntitateNegasitaException("Produsul nu se afla in meniu!"));
    }

    public void adaugaArticol(ArticolMeniu a) {
        if(a == null) {
            throw new ProdusInvalidException("Articolul este invalid!");
        }
        if(meniu.contains(a)) {
            throw new ExistaDejaException("Articolul exista deja!");
        }
        meniu.add(a);
    }
    public void stergeArticol(ArticolMeniu a) {
        if(a == null) {
            throw new ProdusInvalidException("Articolul este invalid!");
        }
        meniu.remove(a);
    }
    public void modificaPretProdus(ArticolMeniu a, double pret_nou) {
        if(a == null || !meniu.contains(a)) {
            throw new ProdusInvalidException("Articolul este invalid!");
        }
        meniu.remove(a);
        a.setPret(pret_nou);
        adaugaArticol(a);
    }

    public List<ArticolMeniu> getArticoleSortateDupaPret() {
        return meniu.stream().toList();
    }
    public Map<CategorieProdus, List<ArticolMeniu>> grupeazaDupaCategorie() {
        return meniu.stream()
                .collect(Collectors.groupingBy(a -> a.getProdus().getCategorieProdus()));
    }

    public double getMediePret() {
        if(meniu.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for(ArticolMeniu a: meniu) {
            suma += a.getPret();
        }
        return suma / meniu.size();
    }

    @Override
    public int compareTo(Restaurant o) {
        return Double.compare(getMediePret(), o.getMediePret());
    }

    @Override
    public String toString() {
        return "Restaurant " + nume;
    }
}
