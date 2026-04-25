package com.pao.project.service;

import com.pao.project.exception.*;
import com.pao.project.model.*;
import com.pao.project.model.enums.*;

import java.util.List;
import java.util.Map;

public class FoodDeliveryApp {
    private final UtilizatorService utilizatorService;
    private final ProdusService produsService;
    private final RestaurantService restaurantService;
    private final ComandaService comandaService;

    public FoodDeliveryApp() {
        this.utilizatorService = UtilizatorService.getInstance();
        this.restaurantService = RestaurantService.getInstance();
        this.produsService = ProdusService.getInstance();
        this.comandaService = ComandaService.getInstance();
    }

    public void inregistreazaClient(String nume, String prenume, String email, String telefon) {
        if(nume == null || nume.isEmpty()) {
            throw new IllegalArgumentException("Numele nu poate fi nul!");
        }
        if(prenume == null || prenume.isEmpty()) {
            throw new IllegalArgumentException("Prenumele nu poate fi nul!");
        }
        if(email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email-ul nu poate fi nul!");
        }

        int id = utilizatorService.getIdCounter();
        Client client = new Client(id, nume, prenume, email, telefon);
        utilizatorService.inregistreaza(client);
        System.out.println("Client inregistrat cu id-ul " + id + "!");
    }

    public void inregistreazaManager(String nume, String prenume, String email, String telefon, int salariu) {
        if(nume == null || nume.isEmpty()) {
            throw new IllegalArgumentException("Numele nu poate fi nul!");
        }
        if(prenume == null || prenume.isEmpty()) {
            throw new IllegalArgumentException("Prenumele nu poate fi nul!");
        }
        if(email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email-ul nu poate fi nul!");
        }
        if(salariu < 0) {
            throw new IllegalArgumentException("Salariul nu poate fi negativ!");
        }

        int id = utilizatorService.getIdCounter();
        Manager manager = new Manager(id, nume, prenume, email, telefon, salariu);
        utilizatorService.inregistreaza(manager);
        System.out.println("Manager inregistrat cu id-ul " + id + "!");
    }

    public void inregistreazaSofer(String nume, String prenume, String email, String telefon, int salariu, String tipVehicul) {
        if(nume == null || nume.isEmpty()) {
            throw new IllegalArgumentException("Numele nu poate fi nul!");
        }
        if(prenume == null || prenume.isEmpty()) {
            throw new IllegalArgumentException("Prenumele nu poate fi nul!");
        }
        if(email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email-ul nu poate fi nul!");
        }
        if(salariu < 0) {
            throw new IllegalArgumentException("Salariul nu poate fi negativ!");
        }

        int id = utilizatorService.getIdCounter();
        Sofer sofer = new Sofer(id, nume, prenume, email, telefon, salariu, tipVehicul);
        utilizatorService.inregistreaza(sofer);
        System.out.println("Sofer inregistrat cu id-ul " + id + "!");
    }

    public void inregistreazaRestaurant(String nume, String email_manager) {
        if(nume == null || nume.isEmpty()) {
            throw new IllegalArgumentException("Numele restaurantului nu poate fi nul!");
        }

        Persoana persoana = utilizatorService.cautaDupaEmail(email_manager);
        if(!(persoana instanceof Manager)) {
            throw new UtilizatorInvalidException("Managerul nu a fost gasit!");
        }
        else {
            int id = restaurantService.getIdCounter();
            Manager manager = (Manager)persoana;
            Restaurant restaurant = new Restaurant(id, nume, manager);
            restaurantService.adaugaRestaurant(restaurant);
            System.out.println("Restaurant inregistrat cu id-ul " + id + "!");
        }
    }


    public void adaugaProdusInMeniu(int idRestaurant, String numeProdus, CategorieProdus categorie, double pret) {
        if(numeProdus == null || numeProdus.isEmpty()) {
            throw new IllegalArgumentException("Numele produsului nu poate fi nul!");
        }
        if(pret < 0) {
            throw new IllegalArgumentException("Pretul nu poate fi negativ!");
        }

        Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);

        Produs p;
        if (!produsService.existaProdus(numeProdus, categorie)) {
            int idProdus = produsService.getIdCounter();
            p = new Produs(idProdus, numeProdus, categorie);
            produsService.adaugaProdus(p);
        }
        else {
            p = produsService.cautaProdus(numeProdus, categorie);
        }

        ArticolMeniu articol = new ArticolMeniu(p, pret);
        r.adaugaArticol(articol);
        System.out.println("Produs adaugat in meniu!");
    }

    public void eliminaProdusDinMeniu(int idRestaurant, String nume_articol) {
        Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
        ArticolMeniu articol = r.getArticolDupaNume(nume_articol);
        r.stergeArticol(articol);
        System.out.println("Produs eliminat din meniu!");
    }

    public void modificaPretProdus(int idRestaurant, String nume_articol, double pretNou) {
        Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
        ArticolMeniu articol = r.getArticolDupaNume(nume_articol);
        r.modificaPretProdus(articol, pretNou);
        System.out.println("Pretul a fost modificat!");
    }

    public void afiseazaMeniuSortat(int idRestaurant) {
        Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
        System.out.println("--- Meniu sortat - " + r + " ---");
        for(ArticolMeniu a: r.getArticoleSortateDupaPret()) {
            System.out.println("- " + a);
        }
    }

    public void afiseazaMeniuPeCategorii(int idRestaurant) {
        Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
        Map<CategorieProdus, List<ArticolMeniu>> grupate = r.grupeazaDupaCategorie();

        System.out.println("--- Meniu (pe categorii) - " + r + " ---");
        for(Map.Entry<CategorieProdus, List<ArticolMeniu>> entry: grupate.entrySet()) {
            System.out.println(entry.getKey());
            for(ArticolMeniu a: entry.getValue()) {
                System.out.println("- " + a);
            }
        }
    }

    public void afiseazaRestauranteSortateDupaPret() {
        List<Restaurant> restaurante_dupa_medie = restaurantService.getRestauranteDupaMedie();
        if(restaurante_dupa_medie.isEmpty()) {
            System.out.println("Nu exista niciun restaurant inregistrat!");
        }
        else {
            System.out.println("--- Restaurante sortate dupa media preturilor ---");
            int i = 1;
            for (Restaurant r : restaurante_dupa_medie) {
                System.out.println(i++ + ". " + r + " (" + String.format("%.2f", r.getMediePret()) + ")");
            }
        }
    }


    public void initiazaComanda(String email, int idRestaurant, AdresaLivrare adresa) {
        Persoana persoana = utilizatorService.cautaDupaEmail(email);
        if(!(persoana instanceof Client)) {
            throw new UtilizatorInvalidException("Clientul nu a fost gasit!");
        }
        else {
            Client client = (Client)persoana;
            Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
            int idComanda = comandaService.getIdCounter();
            Comanda comanda = new Comanda(idComanda, client, r, adresa);
            comandaService.adaugaComanda(comanda);
            System.out.println("Comanda initiata cu id-ul " + idComanda + "!");
        }
    }

    public void adaugaArticolLaComanda(int idComanda, String nume_articol, int cantitate) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        ArticolMeniu articol = c.getRestaurant().getArticolDupaNume(nume_articol);
        c.adaugaArticol(new ArticolComanda(articol, cantitate));
        System.out.println("Articolul a fost adaugat in cos!");
    }

    public void eliminaArticolDinComanda(int idComanda, String nume_articol) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        ArticolMeniu articol = c.getRestaurant().getArticolDupaNume(nume_articol);
        c.stergeArticol(articol);
        System.out.println("Articolul a fost eliminat din cos!");
    }

    public void plaseazaComanda(int idComanda) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        if (c.getArticole().isEmpty()) {
            throw new OperatieInvalidaException("Cosul este gol!");
        }

        try {
            Sofer sofer = utilizatorService.gasesteSoferDisponibil();
            c.setSofer(sofer);
            c.setStareComanda(StareComanda.PLASATA);
            System.out.println(c);
            System.out.println("- Sofer: " + sofer);
        } catch (SoferiIndisponibiliException e) {
            System.out.println(e.getMessage());
        }
    }

    public void finalizeazaComanda(int idComanda) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        if(c.getStareComanda().equals(StareComanda.PLASATA)) {
            c.setStareComanda(StareComanda.FINALIZATA);
            System.out.println("Comanda " + idComanda + " a fost finalizata!");
            System.out.println(c);
        }
        else {
            System.out.println("Comanda cu starea " + c.getStareComanda() + " nu poate fi finalizata!");
        }
    }

    public void anuleazaComanda(int idComanda) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        c.setStareComanda(StareComanda.ANULATA);
        System.out.println("Comanda " + idComanda + " a fost anulata!");
        System.out.println(c);
    }


    public void afiseazaComenziFinalizate() {
        List<Comanda> finalizate = comandaService.istoricComenziFinalizate();
        if (finalizate.isEmpty()) {
            System.out.println("Nu exista comenzi finalizate!");
        } else {
            System.out.println("--- Istoricul comenzilor finalizate ---");
            for (Comanda c : finalizate) {
                System.out.println(c);
            }
        }
    }

    public void afiseazaIstoricClient(String email) {
        Persoana persoana = utilizatorService.cautaDupaEmail(email);
        if(!(persoana instanceof Client)) {
            throw new UtilizatorInvalidException("Clientul nu a fost gasit!");
        }
        else {
            Client client = (Client)persoana;
            List<Comanda> comenziClient = comandaService.cautaDupaUtilizator(client);

            if (comenziClient.isEmpty()) {
                System.out.println("Clientul nu a plasat nicio comanda!");
            } else {
                System.out.println("--- Istoric comenzi pentru " + client.getNumeComplet() + " ---");
                for (Comanda c : comenziClient) {
                    System.out.println(c);
                }
            }
        }
    }
}