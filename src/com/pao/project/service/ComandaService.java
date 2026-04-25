package com.pao.project.service;

import com.pao.project.exception.EntitateNegasitaException;
import com.pao.project.exception.OperatieInvalidaException;
import com.pao.project.model.Client;
import com.pao.project.model.Comanda;

import java.util.*;

public class ComandaService {
    private Map<Integer, Comanda> comenzi;
    private static int idCounter = 1;

    private ComandaService() { comenzi = new HashMap<>(); }
    private static class ComandaServiceHolder {
        private static final ComandaService Instance = new ComandaService();
    }
    public static ComandaService getInstance() { return ComandaServiceHolder.Instance; }

    public int getIdCounter() { return idCounter++; }
    public Map<Integer, Comanda> getComenzi() { return comenzi; }

    public void adaugaComanda(Comanda c) {
        if(c == null) {
            throw new OperatieInvalidaException("Comanda nu poate fi nula!");
        }
        comenzi.put(c.getId(), c);
    }

    public List<Comanda> cautaDupaUtilizator(Client client) {
        return comenzi.values().stream()
                .filter(c -> c.getClient() == client)
                .toList();
    }

    public Comanda getComandaDupaId(int id) {
        Comanda c = comenzi.get(id);
        if(c == null) throw new EntitateNegasitaException("Comanda invalida!");
        return c;
    }

    public List<Comanda> istoricComenziFinalizate() {
        return comenzi.values().stream()
                .filter(c -> c.getStareComanda().stareFinala())
                .toList();
    }
}
