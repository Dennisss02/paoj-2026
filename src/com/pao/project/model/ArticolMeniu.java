package com.pao.project.model;

public class ArticolMeniu implements Comparable<ArticolMeniu> {
    private Produs produs;
    private double pret;

    public ArticolMeniu(Produs produs, double pret) {
        this.produs = produs;
        this.pret = pret;
    }

    public Produs getProdus() { return produs; }
    public double getPret() { return pret; }
    public void setPret(double pret) { this.pret = pret; }

    @Override
    public String toString() {
        return produs + " - " + pret + " lei";
    }

    @Override
    public int compareTo(ArticolMeniu o) {
        return Double.compare(pret, o.pret);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ArticolMeniu a = (ArticolMeniu)obj;
        return produs == a.produs && (Double.compare(pret, a.pret) == 0);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(produs, pret);
    }
}
