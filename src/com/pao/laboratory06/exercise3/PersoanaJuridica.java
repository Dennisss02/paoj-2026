package com.pao.laboratory06.exercise3;

import java.util.ArrayList;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double sold;
    ArrayList<String> smsTrimise = new ArrayList<String>();

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.sold = sold;
    }

    public void afiseazaMesaje() {
        if(smsTrimise.isEmpty()) {
            System.out.println("Lista este goala.");
        }
        else {
            for (String sms : smsTrimise) {
                System.out.println("- " + sms);
            }
        }
    }

    @Override
    public String toString() {
        return "Persoana juridica: " +  nume + " " + prenume + " " + telefon + " " + sold;
    }

    @Override
    public void autentificare(String user, String parola) {
        if(user == null || user.trim().isEmpty()) {
            throw new IllegalArgumentException("Introduceti numele de utilizator.");
        }
        if(parola == null || parola.trim().isEmpty()) {
            throw new IllegalArgumentException("Introduceti parola.");
        }
        System.out.println("Autentificare realizata cu succes!");
    }
    @Override
    public double consultareSold() {
        return sold;
    }
    @Override
    public boolean efectuarePlata(double suma) {
        if(sold >= suma) {
            sold -= suma;
            return true;
        }
        return false;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if(mesaj == null || mesaj.trim().isEmpty()) {
            return false;
        }
        if(telefon == null || telefon.trim().isEmpty()) {
            throw new UnsupportedOperationException("Utilizatorul nu are numar de telefon.");
        }
        smsTrimise.add(mesaj);
        return true;
    }
}
