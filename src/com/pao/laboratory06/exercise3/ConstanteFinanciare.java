package com.pao.laboratory06.exercise3;

enum ConstanteFinanciare {
    TVA(0.19),
    SALARIU_MINIM(4050),
    COTA_IMPOZIT(0.01);

    private final double valoare;
    ConstanteFinanciare(double valoare) { this.valoare = valoare; }

    public double getValoare() { return valoare; }
}
