package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica implements IOperatiiCitireScriere {
    double cheltuieli_lunare;

    @Override
    public double calculeazaVenitNetAnual() {
        return (venit_lunar - cheltuieli_lunare) * 12 * 0.84;
    }

    @Override
    public void citeste(Scanner in) {
        this.tip_colaborator = TipColaborator.SRL;
        String linie = in.nextLine();
        String[] l_in = linie.trim().split(" ");
        this.nume = l_in[0];
        this.prenume = l_in[1];
        this.venit_lunar = Double.parseDouble(l_in[2]);
        this.cheltuieli_lunare = Double.parseDouble(l_in[3]);
    }

    @Override
    public String tipContract() {
        return "SRL";
    }
}
