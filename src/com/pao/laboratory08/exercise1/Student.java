package com.pao.laboratory08.exercise1;

public class Student implements Cloneable {
    private String nume;
    private int varsta;
    private Adresa adresa;

    // constructor(String nume, int varsta, Adresa adresa)
    public Student(String nume, int varsta, Adresa adresa) {
        this.nume = nume;
        this.varsta = varsta;
        this.adresa = adresa;
    }
    // getteri, setteri
    public String getNume() { return nume; }
    public int getVarsta() { return varsta; }
    public Adresa getAdresa() { return adresa; }
    public void setNume(String nume) { this.nume = nume; }
    public void setVarsta(int varsta) { this.varsta = varsta; }
    public void setAdresa(Adresa adresa) { this.adresa = adresa; }
    // toString() → "Student{nume='...', varsta=..., adresa=Adresa{oras='...', strada='...'}}"

    @Override
    public String toString() {
        return "Student{nume='" + nume + "', varsta=" + varsta + ", adresa=" + adresa + "}";
    }

    // clone() — implementare diferită pentru shallow vs. deep (vezi mai jos)

    protected Student shallowClone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }
    public Student deepClone() throws CloneNotSupportedException {
        Student clona = (Student) super.clone();
        clona.setAdresa((Adresa) this.adresa.clone());
        return clona;
    }

    public void setOras(String oras) {
        adresa.setOras(oras);
    }
}
