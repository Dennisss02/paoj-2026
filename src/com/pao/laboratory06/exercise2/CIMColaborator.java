package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica implements IOperatiiCitireScriere {
    boolean bonus;

    @Override
    public double calculeazaVenitNetAnual() {
        double rez = venit_lunar * 12 * 0.55;
        if(bonus) {
            rez *= 1.10;
        }
        return rez;
    }

    @Override
    public void citeste(Scanner in) {
        this.tip_colaborator = TipColaborator.CIM;
        String linie = in.nextLine();
        String[] l_in = linie.trim().split(" ");
        this.nume = l_in[0];
        this.prenume = l_in[1];
        this.venit_lunar = Double.parseDouble(l_in[2]);
        try {
            this.bonus = l_in[3].equals("DA");
        }
        catch(Exception e) {
            this.bonus = false;
        }
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }
}
