package com.pao.project.model;

public class Manager extends Angajat {
    public Manager(int id, String nume, String prenume, String email, String telefon, int salariu) {
        super(id, nume, prenume, email, telefon, salariu);
    }

    @Override
    public String getTipUtilizator() {
        return "Manager";
    }

    @Override
    public String toString() {
        return "Manager: " + getNumeComplet() + ", " + email + ", " + telefon;
    }
}
