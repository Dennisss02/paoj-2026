package com.pao.laboratory06.exercise3;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("-- Creare si sortare ingineri --");
        Inginer[] ingineri = {
            new Inginer("Popescu", "Ion", "0712345678", 7500, 2000),
            new Inginer("Anghel", "Andrei", "0723456789", 7000, 1000),
            new Inginer("Dumitru", "Bianca", "0734567890", 9000, 3000)
        };
        System.out.println("Sortare naturala ingineri:");
        Arrays.sort(ingineri);
        for(int i = 1; i <= ingineri.length; i++) {
            System.out.println(i + ". " + ingineri[i - 1]);
        }
        System.out.println("Sortare ingineri descrescator dupa salariu");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for(int i = 1; i <= ingineri.length; i++) {
            System.out.println(i + ". " + ingineri[i - 1]);
        }
        System.out.println();

        System.out.println("-- Accesarea unui inginer prin referinta de tip PlataOnline -- ");
        PlataOnline inginerPO = new Inginer("Constantin", "Ana", "0745678901", 7000, 1500);
        System.out.println("Sold: " + inginerPO.consultareSold());
        if(inginerPO.efectuarePlata(100)) {
            System.out.println("Plata realizata cu succes! Sold ramas: " + inginerPO.consultareSold());
        }
        else {
            System.out.println("Plata nu a putut fi realizata, sold insuficient.");
        }
        if(inginerPO.efectuarePlata(5000)) {
            System.out.println("Plata realizata cu succes! Sold ramas: " + inginerPO.consultareSold());
        }
        else {
            System.out.println("Plata nu a putut fi realizata, sold insuficient.");
        }
//        inginerPO.getSalariu(); /// nu poate fi accesata
        System.out.println();

        System.out.println("-- Accesarea unei persoane juridice prin referinta de tip PlataOnlineSMS --");
        System.out.println("Caz valid:");
        PlataOnlineSMS persoanaJuridicaSMS1 = new PersoanaJuridica("Costache", "Alin", "0756789012", 1000);
        try {
            if(persoanaJuridicaSMS1.trimiteSMS("Avem promotii noi!!!!!!")) {
                System.out.println("Mesajul a fost trimis cu succes!");
            }
            else {
                System.out.println("Mesajul nu a putut fi trimis.");
            }
        }
        catch(UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Caz fara mesaj:");
        try {
            if(persoanaJuridicaSMS1.trimiteSMS(" ")) {
                System.out.println("Mesajul a fost trimis cu succes!");
            }
            else {
                System.out.println("Mesajul nu a putut fi trimis.");
            }
        }
        catch(UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("  Lista mesajelor (mesajul invalid nu a fost salvat)");
        ((PersoanaJuridica) persoanaJuridicaSMS1).afiseazaMesaje();
        // facem downcast pentru ca nu putem accesa metoda afiseazaMesaje pt referinta de tip plataOnlineSMS
        System.out.println("Caz fara numar de telefon:");
        PlataOnlineSMS persoanaJuridicaSMS2 = new PersoanaJuridica("Barbu", "Adelina", " ", 1250);
        try {
            if(persoanaJuridicaSMS2.trimiteSMS("Cf")) {
                System.out.println("Mesajul a fost trimis cu succes!");
            }
            else {
                System.out.println("Mesajul nu a putut fi trimis.");
            }
        }
        catch(UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("-- Constantele financiare --");
        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariu minim: " + ConstanteFinanciare.SALARIU_MINIM.getValoare());
        System.out.println("Cota impozit: " + ConstanteFinanciare.COTA_IMPOZIT.getValoare());
        System.out.println();

        System.out.println("-- Tratarea erorilor --");
        System.out.println("Autentificare cu username null:");
        try {
            inginerPO.autentificare("", "parola");
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        try {
            inginerPO.autentificare("ingPO", "");
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Nu se poate apela metoda trimiteSMS pentru ingineri, " +
                "nici macar pentru referinte de tip PlataOnlineSMS");
    }
}
