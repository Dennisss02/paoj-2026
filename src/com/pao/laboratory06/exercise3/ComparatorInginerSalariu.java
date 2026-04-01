package com.pao.laboratory06.exercise3;

import java.util.Comparator;

class ComparatorInginerSalariu implements Comparator<Inginer> {
    @Override
    public int compare(Inginer o1, Inginer o2) {
        return Double.compare(o2.salariu, o1.salariu);
    }
}
