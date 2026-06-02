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

        Client client = new Client(0, nume, prenume, email, telefon);
        utilizatorService.inregistreaza(client);
        AuditService.getInstance().log("inregistreaza_client");
        System.out.println("Client inregistrat cu id-ul " + client.getId() + "!");
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

        Manager manager = new Manager(0, nume, prenume, email, telefon, salariu);
        utilizatorService.inregistreaza(manager);
        AuditService.getInstance().log("inregistreaza_manager");
        System.out.println("Manager inregistrat cu id-ul " + manager.getId() + "!");
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

        Sofer sofer = new Sofer(0, nume, prenume, email, telefon, salariu, tipVehicul);
        utilizatorService.inregistreaza(sofer);
        AuditService.getInstance().log("inregistreaza_sofer");
        System.out.println("Sofer inregistrat cu id-ul " + sofer.getId() + "!");
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
            Manager manager = (Manager)persoana;
            Restaurant restaurant = new Restaurant(0, nume, manager);
            restaurantService.adaugaRestaurant(restaurant);
            AuditService.getInstance().log("inregistreaza_restaurant");
            System.out.println("Restaurant inregistrat cu id-ul " + restaurant.getId() + "!");
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
            p = new Produs(0, numeProdus, categorie);
            produsService.adaugaProdus(p);
        }
        else {
            p = produsService.cautaProdus(numeProdus, categorie);
        }

        ArticolMeniu articol = new ArticolMeniu(p, pret);
        r.adaugaArticol(articol);
        restaurantService.salveazaArticolMeniu(idRestaurant, p.getId(), pret);
        AuditService.getInstance().log("adauga_produs_meniu");
    }

    public void eliminaProdusDinMeniu(int idRestaurant, String nume_articol) {
        Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
        ArticolMeniu articol = r.getArticolDupaNume(nume_articol);
        r.stergeArticol(articol);
        restaurantService.eliminaArticolMeniu(idRestaurant, articol.getProdus().getId());
        System.out.println("Produs eliminat din meniu!");
        AuditService.getInstance().log("elimina_produs_meniu");
    }

    public void modificaPretProdus(int idRestaurant, String nume_articol, double pretNou) {
        Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
        ArticolMeniu articol = r.getArticolDupaNume(nume_articol);
        r.modificaPretProdus(articol, pretNou);
        restaurantService.modificaPretArticolMeniu(idRestaurant, articol.getProdus().getId(), pretNou);
        System.out.println("Pretul a fost modificat!");
        AuditService.getInstance().log("modifica_pret_produs");
    }

    public void afiseazaMeniuSortat(int idRestaurant) {
        Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
        System.out.println("--- Meniu sortat - " + r + " ---");
        for(ArticolMeniu a: r.getArticoleSortateDupaPret()) {
            System.out.println("- " + a);
        }
        AuditService.getInstance().log("afiseaza_meniu_sortat");
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
        AuditService.getInstance().log("afiseaza_meniu_categorii");
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
        AuditService.getInstance().log("afiseaza_restaurante_sortate");
    }


    public void initiazaComanda(String email, int idRestaurant, AdresaLivrare adresa) {
        Persoana persoana = utilizatorService.cautaDupaEmail(email);
        if(!(persoana instanceof Client)) {
            throw new UtilizatorInvalidException("Clientul nu a fost gasit!");
        }
        else {
            Client client = (Client)persoana;
            Restaurant r = restaurantService.getRestaurantDupaId(idRestaurant);
            Comanda comanda = new Comanda(0, client, r, adresa);
            comandaService.adaugaComanda(comanda);
            System.out.println("Comanda initiata cu id-ul " + comanda.getId() + "!");
            AuditService.getInstance().log("initiaza_comanda");
        }
    }

    public void adaugaArticolLaComanda(int idComanda, String nume_articol, int cantitate) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        ArticolMeniu articol = c.getRestaurant().getArticolDupaNume(nume_articol);
        c.adaugaArticol(new ArticolComanda(articol, cantitate));
        System.out.println("Articolul a fost adaugat in cos!");
        AuditService.getInstance().log("adauga_articol_comanda");
    }

    public void eliminaArticolDinComanda(int idComanda, String nume_articol) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        ArticolMeniu articol = c.getRestaurant().getArticolDupaNume(nume_articol);
        c.stergeArticol(articol);
        System.out.println("Articolul a fost eliminat din cos!");
        AuditService.getInstance().log("elimina_articol_comanda");
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

            comandaService.plaseazaComandaTransaction(c);
            comandaService.atribuieSoferTransaction(c.getId(), sofer.getId());
            
            AuditService.getInstance().log("plaseaza_comanda");
            System.out.println("Comanda a fost plasata cu succes!");
            System.out.println(c);
            System.out.println("- Sofer: " + sofer);
        } catch (SoferiIndisponibiliException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Eroare la baza de date: " + e.getMessage());
        }
    }

    public void finalizeazaComanda(int idComanda) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        if(c.getStareComanda().equals(StareComanda.PLASATA)) {
            c.setStareComanda(StareComanda.FINALIZATA);
            comandaService.updateStareBD(idComanda, "FINALIZATA");
            try {
                if (c.getSofer() != null) {
                    utilizatorService.elibereazaSofer(c.getSofer().getId());
                }
            } catch (Exception e) {
                System.out.println("Eroare la eliberarea soferului: " + e.getMessage());
            }
            System.out.println("Comanda " + idComanda + " a fost finalizata!");
            System.out.println(c);
            AuditService.getInstance().log("finalizeaza_comanda");
        }
        else {
            System.out.println("Comanda cu starea " + c.getStareComanda() + " nu poate fi finalizata!");
        }
    }

    public void anuleazaComanda(int idComanda) {
        Comanda c = comandaService.getComandaDupaId(idComanda);
        c.setStareComanda(StareComanda.ANULATA);
        comandaService.updateStareBD(idComanda, "ANULATA");
        try {
            if (c.getSofer() != null) {
                utilizatorService.elibereazaSofer(c.getSofer().getId());
            }
        } catch (Exception e) {
            System.out.println("Eroare la eliberarea soferului: " + e.getMessage());
        }
        System.out.println("Comanda " + idComanda + " a fost anulata!");
        System.out.println(c);
        AuditService.getInstance().log("anuleaza_comanda");
    }


    public void afiseazaComenziFinalizate() {
        try {
            List<String> finalizate = comandaService.istoricComenziFinalizate();
            if (finalizate.isEmpty()) {
                System.out.println("Nu exista comenzi finalizate!");
            } else {
                System.out.println("--- Istoricul comenzilor finalizate ---");
                for (String rand : finalizate) {
                    System.out.println(rand);
                }
            }
            AuditService.getInstance().log("afiseaza_comenzi_finalizate");
        } catch (Exception e) {
            System.out.println("Eroare baza de date: " + e.getMessage());
        }
    }

    public void afiseazaIstoricClient(String email) {
        try {
            Persoana persoana = utilizatorService.cautaDupaEmail(email);
            if(!(persoana instanceof Client)) {
                throw new UtilizatorInvalidException("Clientul nu a fost gasit!");
            }
            Client client = (Client)persoana;
            List<String> comenziClient = comandaService.cautaDupaUtilizator(client.getId());

            if (comenziClient.isEmpty()) {
                System.out.println("Clientul nu a plasat nicio comanda!");
            } else {
                System.out.println("--- Istoric comenzi pentru " + client.getNumeComplet() + " ---");
                for (String c : comenziClient) {
                    System.out.println(c);
                }
            }
            AuditService.getInstance().log("afiseaza_istoric_client");
        } catch (UtilizatorInvalidException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Eroare baza de date: " + e.getMessage());
        }
    }

    public void afiseazaTop5ProduseVandute() {
        try {
            List<String> top5 = produsService.getTop5ProduseVandute();
            System.out.println("--- Top 5 produse vandute ---");
            if (top5.isEmpty()) {
                System.out.println("Nu exista date!");
            } else {
                for (String rand : top5) {
                    System.out.println("- " + rand);
                }
            }
            AuditService.getInstance().log("raport_top5_produse");
        } catch (Exception e) {
            System.out.println("Eroare baza de date: " + e.getMessage());
        }
    }

    public void afiseazaIncasariRestaurante() {
        try {
            List<String> incasari = restaurantService.getIncasariRestaurante();
            System.out.println("--- Incasari restaurante ---");
            if (incasari.isEmpty()) {
                System.out.println("Nu exista date!");
            } else {
                for (String rand : incasari) {
                    System.out.println("- " + rand);
                }
            }
            AuditService.getInstance().log("raport_incasari_restaurante");
        } catch (Exception e) {
            System.out.println("Eroare baza de date: " + e.getMessage());
        }
    }
}