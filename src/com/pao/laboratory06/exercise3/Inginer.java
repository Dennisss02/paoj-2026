package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
    }

    @Override
    public String toString() {
        return "Inginer: " + nume + " " + prenume + " " + telefon + " " + salariu + " " + sold;
    }

    @Override
    public int compareTo(Inginer o) {
        return nume.compareTo(o.nume);
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
}
