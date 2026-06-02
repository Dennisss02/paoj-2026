package com.pao.project.service;

import com.pao.project.exception.EntitateNegasitaException;
import com.pao.project.exception.OperatieInvalidaException;
import com.pao.project.model.Restaurant;

import com.pao.project.repository.RestaurantRepository;

import java.util.*;
import java.sql.*;

public class RestaurantService {
    private List<Restaurant> restaurante;
    private final RestaurantRepository restaurantRepository;

    private RestaurantService() { 
        restaurante = new ArrayList<>(); 
        restaurantRepository = new RestaurantRepository();
        
        try {
            for (Restaurant r: restaurantRepository.findAll()) restaurante.add(r);
        } catch (SQLException e) {
            System.err.println("Eroare la incarcarea restaurantelor din baza de date: " + e.getMessage());
        }
    }
    private static class RestaurantServiceHolder {
        private static final RestaurantService Instance = new RestaurantService();
    }
    public static RestaurantService getInstance() { return RestaurantServiceHolder.Instance; }

    public void adaugaRestaurant(Restaurant r) {
        if(r == null) {
            throw new OperatieInvalidaException("Restaurantul nu poate fi nul!");
        }
        try {
            restaurantRepository.save(r);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea in baza de date: " + e.getMessage(), e);
        }
        restaurante.add(r);
    }

    public void salveazaArticolMeniu(int idRestaurant, int idProdus, double pret) {
        String sql = "INSERT INTO articole_meniu (id_restaurant, id_produs, pret) VALUES (?, ?, ?)";
        try {
            Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idRestaurant);
                ps.setInt(2, idProdus);
                ps.setDouble(3, pret);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea articolului in meniu: " + e.getMessage(), e);
        }
    }

    public void eliminaArticolMeniu(int idRestaurant, int idProdus) {
        String sql = "DELETE FROM articole_meniu WHERE id_restaurant = ? AND id_produs = ?";
        try {
            Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idRestaurant);
                ps.setInt(2, idProdus);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la stergerea articolului din meniu: " + e.getMessage(), e);
        }
    }

    public void modificaPretArticolMeniu(int idRestaurant, int idProdus, double pretNou) {
        String sql = "UPDATE articole_meniu SET pret = ? WHERE id_restaurant = ? AND id_produs = ?";
        try {
            Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, pretNou);
                ps.setInt(2, idRestaurant);
                ps.setInt(3, idProdus);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la modificarea pretului: " + e.getMessage(), e);
        }
    }

    public List<Restaurant> getRestauranteDupaMedie() {
        return restaurante.stream()
                .sorted()
                .toList();
    }

    public Restaurant getRestaurantDupaId(int id) {
        return restaurante.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntitateNegasitaException("Restaurant invalid!"));
    }

    public void stergeRestaurant(Restaurant r) {
        restaurante.remove(r);
    }

    public List<String> getIncasariRestaurante() throws SQLException, java.io.IOException {
        String sql = "SELECT r.nume, SUM(ac.cantitate * am.pret) as total_incasari " +
                "FROM restaurante r " +
                "JOIN comenzi c ON r.id = c.id_restaurant " +
                "JOIN articole_comanda ac ON c.id = ac.id_comanda " +
                "JOIN articole_meniu am ON ac.id_restaurant = am.id_restaurant AND ac.id_produs = am.id_produs " +
                "GROUP BY r.id, r.nume " +
                "ORDER BY total_incasari DESC";
        List<String> results = new ArrayList<>();
        Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(String.format("Restaurant %s - Total incasari: %.2f lei",
                        rs.getString("nume"),
                        rs.getDouble("total_incasari")));
            }
        }
        return results;
    }
}
