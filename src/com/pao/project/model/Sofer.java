package com.pao.project.model;

public class Sofer extends Angajat {
    private String tipVehicul;
    private boolean disponibilitate;

    public Sofer(int id, String nume, String prenume, String email, String telefon, int salariu, String tipVehicul) {
        super(id, nume, prenume, email, telefon, salariu);
        this.tipVehicul = tipVehicul;
        disponibilitate = true;
    }

    public String getTipVehicul() { return tipVehicul; }

    @Override
    public String getTipUtilizator() {
        return "Sofer";
    }

    public boolean getDisponibilitate() { return disponibilitate; }
    public boolean solicita() {
        if(disponibilitate) {
            disponibilitate = false;
            return true;
        }
        return false;
    }
    public void elibereaza() {
        disponibilitate = true;
    }

    @Override
    public String toString() {
        String str = "Sofer: " + getNumeComplet() + ", " + email + ", " + telefon + ", ";
        if(disponibilitate) {
            str += "disponibil";
        }
        else {
            str += "indisponibil";
        }
        return str;
    }
}
