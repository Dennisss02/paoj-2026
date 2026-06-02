package com.pao.project.model;

public class Client extends Persoana {
    public Client(int id, String nume, String prenume, String email, String telefon) {
        super(id, nume, prenume, email, telefon);
    }

    @Override
    public String getTipUtilizator() {
        return "Client";
    }

    @Override
    public String toString() {
        return "Client: " + getNumeComplet() + ", " + email + ", " + telefon;
    }
}
