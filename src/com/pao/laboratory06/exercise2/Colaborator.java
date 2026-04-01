package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere {
    public static double venit_minim_brut = 48600;
    protected String nume;
    protected String prenume;
    protected double venit_lunar;
    protected TipColaborator tip_colaborator;

    public TipColaborator getTip() { return tip_colaborator; }

    public abstract double calculeazaVenitNetAnual();

// CIM: Ionescu Vlad, venit net anual: 46200.00 lei
    @Override
    public void afiseaza() {
        System.out.println(tipContract()  + ": " + nume + " " + prenume +
                ", venit net anual: " + String.format("%.2f", calculeazaVenitNetAnual()) + " lei");
    }
}
