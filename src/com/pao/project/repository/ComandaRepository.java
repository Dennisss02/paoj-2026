package com.pao.project.repository;

import com.pao.project.model.*;
import com.pao.project.service.RestaurantService;
import com.pao.project.service.UtilizatorService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComandaRepository {

    public List<String> findIstoricByClientId(int idClient) throws SQLException {
        List<String> istoric = new ArrayList<>();
        String sql = "SELECT c.id, r.nume as restaurant_nume, c.stare_comanda, " +
                     "CONCAT(p.nume, ' ', p.prenume) as client_nume, " +
                     "CONCAT(c.adresa_oras, ', ', c.adresa_strada, ', ', c.adresa_numar) as adresa, " +
                     "COALESCE(SUM(am.pret * ac.cantitate), 0) as total " +
                     "FROM comenzi c " +
                     "JOIN restaurante r ON c.id_restaurant = r.id " +
                     "JOIN persoane p ON c.id_client = p.id " +
                     "LEFT JOIN articole_comanda ac ON c.id = ac.id_comanda " +
                     "LEFT JOIN articole_meniu am ON ac.id_restaurant = am.id_restaurant AND ac.id_produs = am.id_produs " +
                     "WHERE c.id_client = ? " +
                     "GROUP BY c.id, r.nume, c.stare_comanda, p.nume, p.prenume, c.adresa_oras, c.adresa_strada, c.adresa_numar";

        try (Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String restaurantNume = rs.getString("restaurant_nume");
                    String stare = rs.getString("stare_comanda");
                    String clientNume = rs.getString("client_nume");
                    String adresa = rs.getString("adresa");
                    double total = rs.getDouble("total");
                    
                    String rand = String.format("%d. %s - %.2f lei (%s)\n- %s\n- %s", 
                            id, restaurantNume, total, stare, clientNume, adresa);
                    istoric.add(rand);
                }
            }
        } catch (java.io.IOException e) {
            throw new SQLException(e);
        }
        return istoric;
    }

    public Optional<Comanda> findById(int id) {
        String sql = "SELECT * FROM comenzi WHERE id = ?";
        try {
            Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int idClient = rs.getInt("id_client");
                        int idSofer = rs.getInt("id_sofer");
                        int idRestaurant = rs.getInt("id_restaurant");
                        String oras = rs.getString("adresa_oras");
                        String strada = rs.getString("adresa_strada");
                        int numar = rs.getInt("adresa_numar");
                        String stareStr = rs.getString("stare_comanda");

                        Persoana client = UtilizatorService.getInstance().getUtilizatori().values().stream()
                                .filter(u -> u.getId() == idClient).findFirst().orElse(null);
                        Restaurant restaurant = RestaurantService.getInstance().getRestaurantDupaId(idRestaurant);
                        AdresaLivrare adresa = new AdresaLivrare(oras, strada, numar);

                        Comanda comandaDB = new Comanda(id, (Client) client, restaurant, adresa);
                        if (idSofer != 0) {
                            Persoana pSofer = UtilizatorService.getInstance().getUtilizatori().values().stream()
                                    .filter(u -> u.getId() == idSofer).findFirst().orElse(null);
                            comandaDB.setSofer((Sofer) pSofer);
                        }
                        
                        String sqlArt = "SELECT id_produs, cantitate FROM articole_comanda WHERE id_comanda = ?";
                        try (PreparedStatement psArt = conn.prepareStatement(sqlArt)) {
                            psArt.setInt(1, id);
                            try (ResultSet rsArt = psArt.executeQuery()) {
                                while (rsArt.next()) {
                                    int idP = rsArt.getInt("id_produs");
                                    int cant = rsArt.getInt("cantitate");
                                    com.pao.project.model.ArticolMeniu am = restaurant.getArticoleSortateDupaPret().stream()
                                            .filter(a -> a.getProdus().getId() == idP).findFirst().orElse(null);
                                    if(am != null) comandaDB.adaugaArticol(new com.pao.project.model.ArticolComanda(am, cant));
                                }
                            }
                        }
                        comandaDB.setStareComanda(com.pao.project.model.enums.StareComanda.valueOf(stareStr));
                        return Optional.of(comandaDB);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la extragerea comenzii din baza de date: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<String> findIstoricComenziFinalizate() throws SQLException, java.io.IOException {
        String sql = "SELECT c.id, r.nume as nume_r, SUM(ac.cantitate * am.pret) as total, c.stare_comanda, " +
                "p.nume as nume_cl, p.prenume, p.email, c.adresa_oras, c.adresa_strada, c.adresa_numar " +
                "FROM comenzi c " +
                "JOIN restaurante r ON r.id = c.id_restaurant " +
                "JOIN persoane p ON p.id = c.id_client " +
                "JOIN articole_comanda ac ON ac.id_comanda = c.id " +
                "JOIN articole_meniu am ON ac.id_restaurant = am.id_restaurant AND ac.id_produs = am.id_produs " +
                "WHERE c.stare_comanda = 'FINALIZATA' " +
                "GROUP BY c.id, r.nume, p.nume, p.prenume, p.email, c.stare_comanda, c.adresa_oras, c.adresa_strada, c.adresa_numar";
        List<String> results = new ArrayList<>();
        Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(String.format("%d. %s - %d lei (%s)\n" +
                        "- %s %s, %s\n" +
                        "- %s, %s, nr. %d",
                        rs.getInt("id"),
                        rs.getString("nume_r"),
                        rs.getInt("total"),
                        rs.getString("stare_comanda"),
                        rs.getString("nume_cl"),
                        rs.getString("prenume"),
                        rs.getString("email"),
                        rs.getString("adresa_oras"),
                        rs.getString("adresa_strada"),
                        rs.getInt("adresa_numar")));
            }
            return results;
        }
    }

    public void updateStare(int idComanda, String stare) {
        String sql = "UPDATE comenzi SET stare_comanda = ? WHERE id = ?";
        try {
            Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, stare);
                ps.setInt(2, idComanda);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la modificarea starii in baza de date: " + e.getMessage(), e);
        }
    }

    public void save(Comanda c) throws SQLException {
        Connection conn;
        try {
            conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
        } catch (java.io.IOException e) {
            throw new SQLException(e);
        }
        String sqlComanda = "INSERT INTO comenzi (id_client, id_sofer, id_restaurant, adresa_oras, adresa_strada, adresa_numar, stare_comanda) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlComanda, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getClient().getId());
            if (c.getSofer() != null) {
                ps.setInt(2, c.getSofer().getId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setInt(3, c.getRestaurant().getId());
            ps.setString(4, c.getAdresaLivrare().getOras());
            ps.setString(5, c.getAdresaLivrare().getStrada());
            ps.setInt(6, c.getAdresaLivrare().getNumar());
            ps.setString(7, c.getStareComanda().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
        }
    }

    public void saveArticole(Comanda c) throws SQLException {
        Connection conn;
        try {
            conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
        } catch (java.io.IOException e) {
            throw new SQLException(e);
        }
        try {
            conn.setAutoCommit(false);

            String sqlArticole = "INSERT INTO articole_comanda (id_comanda, id_restaurant, id_produs, cantitate) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlArticole)) {
                for (com.pao.project.model.ArticolComanda articol : c.getArticole()) {
                    ps.setInt(1, c.getId());
                    ps.setInt(2, c.getRestaurant().getId());
                    ps.setInt(3, articol.getArticol().getProdus().getId());
                    ps.setInt(4, articol.getCantitate());
                    ps.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("Eroare la salvarea articolelor in tranzactie", e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void atribuieSofer(int idComanda, int idSofer) throws SQLException, java.io.IOException {
        Connection conn = com.pao.project.util.DatabaseConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlUpdateComanda = "UPDATE comenzi SET id_sofer = ?, stare_comanda = 'PLASATA' WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateComanda)) {
                ps.setInt(1, idSofer);
                ps.setInt(2, idComanda);
                ps.executeUpdate();
            }

            String sqlUpdateSofer = "UPDATE angajati SET disponibilitate = false WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateSofer)) {
                ps.setInt(1, idSofer);
                ps.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("Eroare la atribuirea soferului in tranzactie", e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
