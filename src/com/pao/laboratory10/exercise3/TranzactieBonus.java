package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;

public class TranzactieBonus {
    private int id;
    private String contSursa;
    private double suma;
    private String data;
    private TipTranzactie tip;

    public TranzactieBonus(int id, String contSursa, double suma, String data, TipTranzactie tip) {
        this.id = id;
        this.contSursa = contSursa;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
    }

    public int getId() { return id; }
    public double getSuma() { return suma; }
    public String getData() { return data; }
    public TipTranzactie getTip() { return tip; }
    public String getContSursa() { return contSursa; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s %s: %.2f RON", id, contSursa, data, tip, suma);
    }
}
