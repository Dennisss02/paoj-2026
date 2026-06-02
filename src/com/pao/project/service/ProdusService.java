package com.pao.project.service;

import com.pao.project.exception.OperatieInvalidaException;
import com.pao.project.model.Produs;
import com.pao.project.model.Restaurant;
import com.pao.project.model.enums.CategorieProdus;

import com.pao.project.repository.ProdusRepository;

import java.util.*;
import java.sql.*;

public class ProdusService {
    private Set<Produs> produse;
    private final ProdusRepository produsRepository;

    private ProdusService() { 
        produse = new HashSet<>(); 
        produsRepository = new ProdusRepository();
        
        try {
            for (Produs p: produsRepository.findAll()) produse.add(p);
        } catch (SQLException e) {
            System.err.println("Eroare la incarcarea produselor din baza de date: " + e.getMessage());
        }
    }
    private static class ProdusServiceHolder {
        private static final ProdusService Instance = new ProdusService();
    }
    public static ProdusService getInstance() { return ProdusServiceHolder.Instance; }

    public Set<Produs> getProduse() { return produse; }

    public void adaugaProdus(Produs p) {
        if(p == null) {
            throw new OperatieInvalidaException("Produsul nu poate fi nul!");
        }
        try {
            produsRepository.save(p);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea in baza de date: " + e.getMessage(), e);
        }
        produse.add(p);
    }
    public boolean existaProdus(String nume, CategorieProdus categorieProdus) {
        return produse.stream()
                .anyMatch(p -> p.getNume().equals(nume) && p.getCategorieProdus().equals(categorieProdus));
    }
    public Produs cautaProdus(String nume, CategorieProdus categorieProdus) {
        return produse.stream()
                .filter(p -> p.getNume().equals(nume) && p.getCategorieProdus().equals(categorieProdus))
                .findFirst()
                .orElse(null);
    }

    public void stergeProdus(Produs p) {
        produse.remove(p);
    }

    public List<String> getTop5ProduseVandute() throws SQLException, java.io.IOException {
        String sql = "SELECT p.nume, p.categorie_produs, SUM(ac.cantitate) as total_vandut " +
                     "FROM produse p " +
                     "JOIN articole_comanda ac ON p.id = ac.id_produs " +
                     "GROUP BY p.id, p.nume, p.categorie_produs " +
                     "ORDER BY total_vandut DESC LIMIT 5";
        List<String> results = new ArrayList<>();
        Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(String.format("%s (%s) - %d buc.",
                        rs.getString("nume"),
                        rs.getString("categorie_produs"),
                        rs.getInt("total_vandut")));
            }
        }
        return results;
    }
}
