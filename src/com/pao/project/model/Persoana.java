package com.pao.project.model;

public abstract class Persoana {
    protected final int id;
    protected String nume;
    protected String prenume;
    protected String email;
    protected String telefon;

    public Persoana(int id, String nume, String prenume, String email, String telefon) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public String getEmail() { return email; }
    public String getTelefon() { return telefon; }
    public String getNumeComplet() { return nume + " " + prenume; }

    public abstract String getTipUtilizator();

    @Override
    public boolean equals(Object obj) {
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Persoana p = (Persoana)obj;
        return id == p.id && getNumeComplet().equals(p.getNumeComplet()) && email.equals(p.email);
    }
}
