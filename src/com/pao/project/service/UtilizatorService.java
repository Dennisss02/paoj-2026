package com.pao.project.service;

import com.pao.project.exception.*;
import com.pao.project.model.*;

import java.util.*;

public class UtilizatorService {
    private Map<Integer, Persoana> utilizatori;
    private static int idCounter = 1;

    private UtilizatorService() { utilizatori = new HashMap<>(); }
    private static class UtilizatorServiceHolder {
        private static final UtilizatorService Instance = new UtilizatorService();
    }
    public static UtilizatorService getInstance() { return UtilizatorServiceHolder.Instance; }

    public int getIdCounter() {
        return idCounter++;
    }
    public Map<Integer, Persoana> getUtilizatori() { return utilizatori; }

    public Persoana cautaDupaEmail(String email) {
        return utilizatori.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public void inregistreaza(Persoana p) {
        if(p == null || p.getEmail() == null)
            throw new UtilizatorInvalidException("Utilizatorul sau email-ul nu pot fi nule!");
        Persoana emailExista = cautaDupaEmail(p.getEmail());
        if(emailExista != null) {
            throw new ExistaDejaException("Adresa de email " + p.getEmail() + " este deja folosita!");
        }

        utilizatori.put(p.getId(), p);
    }

    public Sofer gasesteSoferDisponibil() throws SoferiIndisponibiliException {
        return utilizatori.values().stream()
                .filter(p -> p instanceof Sofer)
                .map(p -> (Sofer)p)
                .filter(Sofer::getDisponibilitate)
                .findFirst()
                .orElseThrow(() -> new SoferiIndisponibiliException("Nu exista soferi disponibili!"));
    }

    public void sterge(Persoana p) {
        if(p == null) {
            throw new EntitateNegasitaException("Utilizatorul nu poate fi nul!");
        }
        utilizatori.remove(p.getId());
    }
}
