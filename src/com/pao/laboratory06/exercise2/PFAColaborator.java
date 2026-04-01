package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica implements IOperatiiCitireScriere {
    double cheltuieli_lunare;

    @Override
    public double calculeazaVenitNetAnual() {
        double venit_net = (venit_lunar - cheltuieli_lunare) * 12;
        double impozit = 0.1 * venit_net;
        double cass;
        if(venit_net < 6 * venit_minim_brut) {
            cass = 0.1 * (6 * venit_minim_brut);
        }
        else if(venit_net < 72 * venit_minim_brut) {
            cass = 0.1 * venit_net;
        }
        else {
            cass = 0.1 * (72 * venit_minim_brut);
        }
        double cas;
        if(venit_net < 12 * venit_minim_brut) {
            cas = 0;
        }
        else if(venit_net < 24 * venit_minim_brut) {
            cas = 0.25 * (12 * venit_minim_brut);
        }
        else {
            cas = 0.25 * (24 * venit_minim_brut);
        }
        return venit_net - impozit - cass - cas;
    }

    @Override
    public void citeste(Scanner in) {
        this.tip_colaborator = TipColaborator.PFA;
        String linie = in.nextLine();
        String[] l_in = linie.trim().split(" ");
        this.nume = l_in[0];
        this.prenume = l_in[1];
        this.venit_lunar = Double.parseDouble(l_in[2]);
        this.cheltuieli_lunare = Double.parseDouble(l_in[3]);
    }

    @Override
    public String tipContract() {
        return "PFA";
    }
}
