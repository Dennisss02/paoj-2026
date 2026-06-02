package com.pao.project.model;

public class ArticolComanda {
    private ArticolMeniu articol;
    private int cantitate;

    public ArticolComanda(ArticolMeniu articol, int cantitate) {
        this.articol = articol;
        this.cantitate = cantitate;
    }

    public ArticolMeniu getArticol() { return articol; }
    public int getCantitate() { return cantitate; }
    public void setCantitate(int cantitate) { this.cantitate = cantitate; }

    public double getPretTotal() {
        return articol.getPret() * cantitate;
    }

    @Override
    public String toString() {
        return articol + " | (" + cantitate + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ArticolComanda a = (ArticolComanda)obj;
        return articol.equals(a.articol);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(articol, cantitate);
    }
}
