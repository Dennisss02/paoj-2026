package com.pao.project.model;

public final class AdresaLivrare {
    private final String oras;
    private final String strada;
    private final int numar;

    public AdresaLivrare(String oras, String strada, int numar) {
        this.oras = oras;
        this.strada = strada;
        this.numar = numar;
    }

    public String getOras() { return oras; }
    public String getStrada() { return strada; }
    public int getNumar() { return numar; }

    @Override
    public String toString() {
        return "Adresa: " + oras + ", " + strada + ", nr. " + numar;
    }
}
