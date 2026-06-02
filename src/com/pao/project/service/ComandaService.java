package com.pao.project.service;

import com.pao.project.exception.EntitateNegasitaException;
import com.pao.project.exception.OperatieInvalidaException;
import com.pao.project.model.*;
import com.pao.project.repository.ComandaRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class ComandaService {
    private Map<Integer, Comanda> comenzi;
    private final ComandaRepository comandaRepository;

    private ComandaService() { 
        comenzi = new HashMap<>(); 
        comandaRepository = new ComandaRepository();
    }
    
    private static class ComandaServiceHolder {
        private static final ComandaService Instance = new ComandaService();
    }
    
    public static ComandaService getInstance() { return ComandaServiceHolder.Instance; }

    public Map<Integer, Comanda> getComenzi() { return comenzi; }

    public void adaugaComanda(Comanda c) {
        if(c == null) {
            throw new OperatieInvalidaException("Comanda nu poate fi nula!");
        }
        try {
            comandaRepository.save(c);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea comenzii in baza de date: " + e.getMessage(), e);
        }
        comenzi.put(c.getId(), c);
    }

    public List<String> cautaDupaUtilizator(int idClient) throws SQLException {
        return comandaRepository.findIstoricByClientId(idClient);
    }

    public Comanda getComandaDupaId(int id) {
        Comanda c = comenzi.get(id);
        if(c != null) return c;

        return comandaRepository.findById(id)
                .orElseThrow(() -> new EntitateNegasitaException("Comanda invalida!"));
    }

    public List<String> istoricComenziFinalizate() throws SQLException, java.io.IOException {
        return comandaRepository.findIstoricComenziFinalizate();
    }

    public void updateStareBD(int idComanda, String stare) {
        comandaRepository.updateStare(idComanda, stare);
    }

    public void plaseazaComandaTransaction(Comanda c) throws SQLException, IOException {
        comandaRepository.saveArticole(c);
    }

    public void atribuieSoferTransaction(int idComanda, int idSofer) throws SQLException, IOException {
        comandaRepository.atribuieSofer(idComanda, idSofer);
    }
}
