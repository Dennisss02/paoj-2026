package com.pao.project.service;

import com.pao.project.exception.*;
import com.pao.project.model.*;

import com.pao.project.repository.ClientRepository;
import com.pao.project.repository.ManagerRepository;
import com.pao.project.repository.SoferRepository;

import java.sql.SQLException;
import java.util.*;

public class UtilizatorService {
    private Map<Integer, Persoana> utilizatori;
    
    private final ClientRepository clientRepository;
    private final ManagerRepository managerRepository;
    private final SoferRepository soferRepository;

    private UtilizatorService() { 
        utilizatori = new HashMap<>(); 
        clientRepository = new ClientRepository();
        managerRepository = new ManagerRepository();
        soferRepository = new SoferRepository();
        
        try {
            for (Client c : clientRepository.findAll()) utilizatori.put(c.getId(), c);
            for (Manager m : managerRepository.findAll()) utilizatori.put(m.getId(), m);
            for (Sofer s : soferRepository.findAll()) utilizatori.put(s.getId(), s);
        } catch (SQLException e) {
            System.err.println("Eroare la incarcarea utilizatorilor din baza de date: " + e.getMessage());
        }
    }
    private static class UtilizatorServiceHolder {
        private static final UtilizatorService Instance = new UtilizatorService();
    }
    public static UtilizatorService getInstance() { return UtilizatorServiceHolder.Instance; }

    public Map<Integer, Persoana> getUtilizatori() { return utilizatori; }

    public Persoana cautaDupaEmail(String email) {
        return utilizatori.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
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

        try {
            if (p instanceof Client) {
                clientRepository.save((Client) p);
            } else if (p instanceof Sofer) {
                soferRepository.save((Sofer) p);
            } else if (p instanceof Manager) {
                managerRepository.save((Manager) p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea in baza de date: " + e.getMessage(), e);
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

    public void elibereazaSofer(int idSofer) throws SQLException {
        soferRepository.elibereazaSofer(idSofer);
        Persoana p = utilizatori.get(idSofer);
        if (p instanceof Sofer) {
            ((Sofer) p).elibereaza();
        }
    }

    public void sterge(Persoana p) {
        if(p == null) {
            throw new EntitateNegasitaException("Utilizatorul nu poate fi nul!");
        }
        utilizatori.remove(p.getId());
    }
}
