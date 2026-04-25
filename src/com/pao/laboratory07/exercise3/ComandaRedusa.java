package com.pao.laboratory07.exercise3;

public final class ComandaRedusa extends Comanda {
    private int discountProcent;
    public ComandaRedusa(String nume, double pret, int discountProcent, String client) {
        super(nume, pret, client);
        this.discountProcent = discountProcent;
    }

    public int getDiscountProcent() { return discountProcent; }

    @Override
    public double pretFinal() {
        return pret * (1 - (double) discountProcent / 100);
    }

    @Override
    public String descriere() {
        return "DISCOUNTED: " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei (-" + discountProcent + "%) [" + stare + "] - client: " + client;
    }
}
