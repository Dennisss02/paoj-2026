package com.pao.project.model;

import com.pao.project.exception.ExistaDejaException;
import com.pao.project.exception.OperatieInvalidaException;
import com.pao.project.exception.ProdusInvalidException;
import com.pao.project.model.enums.StareComanda;

import java.util.*;

public class Comanda {
    private int id;
    private Client client;
    private Sofer sofer;
    private Restaurant restaurant;
    private AdresaLivrare adresaLivrare;
    private List<ArticolComanda> articole;
    private StareComanda stareComanda;

    public Comanda(int id, Client client, Restaurant restaurant, AdresaLivrare adresaLivrare) {
        this.id = id;
        this.client = client;
        this.restaurant = restaurant;
        this.adresaLivrare = adresaLivrare;
        this.articole = new ArrayList<>();
        this.stareComanda = StareComanda.INITIATA;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Client getClient() { return client; }
    public Sofer getSofer() { return sofer; }
    public Restaurant getRestaurant() { return restaurant; }
    public AdresaLivrare getAdresaLivrare() { return adresaLivrare; }
    public List<ArticolComanda> getArticole() { return articole; }
    public StareComanda getStareComanda() { return stareComanda; }

    public void setStareComanda(StareComanda stareComanda) throws OperatieInvalidaException {
        if(this.stareComanda.stareFinala()) {
            throw new OperatieInvalidaException("Comanda nu mai poate fi modificata!");
        }
        this.stareComanda = stareComanda;
    }
    public void setSofer(Sofer sofer) {
        if(this.stareComanda.stareFinala()) {
            throw new OperatieInvalidaException("Comanda nu mai poate fi modificata!");
        }
        this.sofer = sofer;
    }

    public void adaugaArticol(ArticolComanda a) {
        if(a == null) {
            throw new ProdusInvalidException("Articolul nu poate fi nul!");
        }
        int index = articole.indexOf(a);
        if(index != -1) {
            ArticolComanda articol_existent = articole.get(index);
            articol_existent.setCantitate(articol_existent.getCantitate() + a.getCantitate());
        }
        else {
            articole.add(a);
        }
    }
    public void stergeArticol(ArticolMeniu a_m) {
        if(a_m == null) {
            throw new ProdusInvalidException("Articolul nu poate fi nul!");
        }
        articole.removeIf(a_c -> a_c.getArticol().equals(a_m));
    }

    public double calculeazaTotal() {
        return articole.stream()
                .mapToDouble(ArticolComanda::getPretTotal)
                .sum();
    }

    @Override
    public String toString() {
        return id + ". " + restaurant + " - " + String.format("%.2f", calculeazaTotal()) + " lei (" + stareComanda + ")\n" +
                "- " + client + "\n" +
                "- " + adresaLivrare;
    }
}
