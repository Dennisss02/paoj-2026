package com.pao.project;

import com.pao.project.model.AdresaLivrare;
import com.pao.project.model.enums.CategorieProdus;
import com.pao.project.service.FoodDeliveryApp;

import java.util.Scanner;

public class Main {
    private static void meniu() {
        System.out.println("--- Meniu ---");
        System.out.println("1. Inregistreaza un utilizator (client, sofer, manager)");
        System.out.println("2. Inregistreaza un restaurant");
        System.out.println("3. Adauga un produs in meniul unui restaurant");
        System.out.println("4. Elimina un produs din meniul unui restaurant");
        System.out.println("5. Modifica pretul unui produs din meniul unui restaurant");
        System.out.println("6. Lista produselor unui restaurant, dupa pret");
        System.out.println("7. Lista produselor unui restaurant, dupa categorie");
        System.out.println("8. Lista restaurantelor, dupa media preturilor");
        System.out.println("9. Initiaza o comanda");
        System.out.println("10. Adauga un articol la comanda");
        System.out.println("11. Elimina un articol din comanda");
        System.out.println("12. Plaseaza o comanda");
        System.out.println("13. Marcheaza o comanda ca finalizata");
        System.out.println("14. Anuleaza o comanda");
        System.out.println("15. Istoricul comenzilor finalizate");
        System.out.println("16. Lista comenzilor unui utilizator");
        System.out.println("0. Iesire\n");
    }

    public static void main(String[] args) {
        FoodDeliveryApp app = new FoodDeliveryApp();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while(running) {
            meniu();
            System.out.print("Alege o optiune: ");
            try {
                int optiune = Integer.parseInt(sc.nextLine());
                switch(optiune) {
                    case 0:
                        running = false;
                        System.out.println("La revedere!!");
                        break;
                    case 1:
                        System.out.print("Tip utilizator (client, sofer, manager): ");
                        String tip1 = sc.nextLine().trim().toLowerCase();
                        if(!java.util.Set.of("client", "sofer", "manager").contains(tip1)) {
                            System.out.println("Tip invalid, incercati din nou!");
                            break;
                        }

                        System.out.print("Nume: ");
                        String nume1 = sc.nextLine().trim();
                        System.out.print("Prenume: ");
                        String prenume1 = sc.nextLine().trim();
                        System.out.print("Email: ");
                        String email1 = sc.nextLine().trim().toLowerCase();
                        System.out.print("Telefon: ");
                        String telefon1 = sc.nextLine().trim();
                        switch(tip1) {
                            case "client":
                                app.inregistreazaClient(nume1, prenume1, email1, telefon1);
                                break;
                            case "sofer":
                                System.out.print("Salariu: ");
                                int salariuS1 = Integer.parseInt(sc.nextLine());
                                System.out.print("Tip vehicul: ");
                                String tip_vehicul1 = sc.nextLine().trim();
                                app.inregistreazaSofer(nume1, prenume1, email1, telefon1, salariuS1, tip_vehicul1);
                                break;
                            case "manager":
                                System.out.print("Salariu: ");
                                int salariuM1 = Integer.parseInt(sc.nextLine());
                                app.inregistreazaManager(nume1, prenume1, email1, telefon1, salariuM1);
                                break;
                        }
                        break;
                    case 2:
                        System.out.print("Nume restaurant: ");
                        String nume_restaurant2 = sc.nextLine().trim();
                        System.out.print("Email manager: ");
                        String email_manager2 = sc.nextLine().trim().toLowerCase();
                        app.inregistreazaRestaurant(nume_restaurant2, email_manager2);
                        break;
                    case 3:
                        System.out.print("Id restaurant: ");
                        int id_r3 = Integer.parseInt(sc.nextLine());
                        System.out.print("Nume produs: ");
                        String nume_produs3 = sc.nextLine().trim();
                        System.out.print("Categorie produs (MENIU, MANCARE, DESERT, BAUTURA): ");
                        String categorie_str3 = sc.nextLine().trim();
                        System.out.print("Pret: ");
                        double pret3 = Double.parseDouble(sc.nextLine());
                        try {
                            CategorieProdus categorie3 = CategorieProdus.valueOf(categorie_str3.trim().toUpperCase());
                            app.adaugaProdusInMeniu(id_r3, nume_produs3, categorie3, pret3);
                        }
                        catch(IllegalArgumentException e) {
                            System.out.println("Categorie invalida, incercati din nou!");
                        }
                        break;
                    case 4:
                        System.out.print("Id restaurant: ");
                        int id_r4 = Integer.parseInt(sc.nextLine());
                        System.out.print("Nume produs: ");
                        String nume_produs4 = sc.nextLine().trim();
                        app.eliminaProdusDinMeniu(id_r4, nume_produs4);
                        break;
                    case 5:
                        System.out.print("Id restaurant: ");
                        int id_r5 = Integer.parseInt(sc.nextLine());
                        System.out.print("Nume produs: ");
                        String nume_produs5 = sc.nextLine().trim();
                        System.out.print("Pret nou: ");
                        double pret_nou5 = Double.parseDouble(sc.nextLine());
                        app.modificaPretProdus(id_r5, nume_produs5, pret_nou5);
                        break;
                    case 6:
                        System.out.print("Id restaurant: ");
                        int id_r6 = Integer.parseInt(sc.nextLine());
                        app.afiseazaMeniuSortat(id_r6);
                        break;
                    case 7:
                        System.out.print("Id restaurant: ");
                        int id_r7 = Integer.parseInt(sc.nextLine());
                        app.afiseazaMeniuPeCategorii(id_r7);
                        break;
                    case 8:
                        app.afiseazaRestauranteSortateDupaPret();
                        break;
                    case 9:
                        System.out.print("Email client: ");
                        String email9 = sc.nextLine().trim();
                        System.out.print("Id restaurant: ");
                        int id_r9 = Integer.parseInt(sc.nextLine());
                        System.out.println("Adresa");
                        System.out.print("- Oras: ");
                        String oras9 = sc.nextLine().trim();
                        System.out.print("- Strada: ");
                        String strada9 = sc.nextLine().trim();
                        System.out.print("- Numar: ");
                        int numar9 = Integer.parseInt(sc.nextLine());
                        app.initiazaComanda(email9, id_r9, new AdresaLivrare(oras9, strada9, numar9));
                        app.afiseazaMeniuPeCategorii(id_r9);
                        break;
                    case 10:
                        System.out.print("Id comanda: ");
                        int id_c10 = Integer.parseInt(sc.nextLine());
                        System.out.print("Nume articol: ");
                        String nume_articol10 = sc.nextLine().trim();
                        System.out.print("Cantitate: ");
                        int cantitate = Integer.parseInt(sc.nextLine());
                        app.adaugaArticolLaComanda(id_c10, nume_articol10, cantitate);
                        break;
                    case 11:
                        System.out.print("Id comanda: ");
                        int id_c11 = Integer.parseInt(sc.nextLine());
                        System.out.print("Nume articol: ");
                        String nume_articol11 = sc.nextLine().trim();
                        app.eliminaArticolDinComanda(id_c11, nume_articol11);
                        break;
                    case 12:
                        System.out.print("Id comanda: ");
                        int id_c12 = Integer.parseInt(sc.nextLine());
                        app.plaseazaComanda(id_c12);
                        break;
                    case 13:
                        System.out.print("Id comanda: ");
                        int id_c13 = Integer.parseInt(sc.nextLine());
                        app.finalizeazaComanda(id_c13);
                        break;
                    case 14:
                        System.out.print("Id comanda: ");
                        int id_c14 = Integer.parseInt(sc.nextLine());
                        app.anuleazaComanda(id_c14);
                        break;
                    case 15:
                        app.afiseazaComenziFinalizate();
                        break;
                    case 16:
                        System.out.print("Email client: ");
                        String email16 = sc.nextLine().trim();
                        app.afiseazaIstoricClient(email16);
                        break;
                    default:
                        System.out.println("Optiune invalida, incercati din nou!");
                }
            }
            catch(Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
    }
}
