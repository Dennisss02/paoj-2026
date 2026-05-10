package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.Serializable;

public class Tranzactie implements Serializable {
    private int id;
    private double suma;
    private String data;
    private TipTranzactie tip;
    private transient String note;

    private static final long serialVersionUID = 1L;

    public Tranzactie(int id, double suma, String data, TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
    }

    public int getId() { return id; }
    public double getSuma() { return suma; }
    public String getData() { return data; }
    public TipTranzactie getTip() { return tip; }
    public String getNote() { return note; }

    public void setId(int id) { this.id = id; }
    public void setSuma(double suma) { this.suma = suma; }
    public void setData(String data) { this.data = data; }
    public void setNote(String note) { this.note = note; }


    @Override
    public String toString() {
        return String.format("id=%d data=%s tip=%s suma=%.2f RON", id, data, tip, suma);
    }
}
