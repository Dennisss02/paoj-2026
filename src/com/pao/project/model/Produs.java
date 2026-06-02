package com.pao.project.model;

import com.pao.project.model.enums.CategorieProdus;

public class Produs {
    private int id;
    private String nume;
    private CategorieProdus categorieProdus;

    public Produs(int id, String nume, CategorieProdus categorieProdus) {
        this.id = id;
        this.nume = nume;
        this.categorieProdus = categorieProdus;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNume() { return nume; }
    public CategorieProdus getCategorieProdus() { return categorieProdus; }

    @Override
    public String toString() {
        return nume + " (" + categorieProdus + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Produs p = (Produs)obj;
        return (nume.compareTo(p.nume) == 0) && (categorieProdus == p.categorieProdus);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(nume, categorieProdus);
    }
}
