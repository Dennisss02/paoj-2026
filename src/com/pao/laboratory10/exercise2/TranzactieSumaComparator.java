package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.Comparator;

public class TranzactieSumaComparator implements Comparator<Tranzactie> {
    @Override
    public int compare(Tranzactie o1, Tranzactie o2) {
        return Double.compare(o1.getSuma(), o2.getSuma());
    }
}
