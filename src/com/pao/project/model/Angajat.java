package com.pao.project.model;

public class Angajat extends Persoana {
    private int salariu;

    public Angajat(int id, String nume, String prenume, String email, String telefon, int salariu) {
        super(id, nume, prenume, email, telefon);
        this.salariu = salariu;
    }

    public int getSalariu() { return salariu; }
    public void setSalariu(int salariu) { this.salariu = salariu; }

    @Override
    public String getTipUtilizator() { return "Angajat"; }
}
