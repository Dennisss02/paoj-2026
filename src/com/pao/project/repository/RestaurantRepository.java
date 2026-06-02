package com.pao.project.repository;

import com.pao.project.model.Manager;
import com.pao.project.model.Produs;
import com.pao.project.model.Restaurant;
import com.pao.project.model.ArticolMeniu;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantRepository implements Repository<Restaurant, Integer> {

    private final ManagerRepository managerRepository;
    private final ProdusRepository produsRepository;

    public RestaurantRepository() {
        this.managerRepository = new ManagerRepository();
        this.produsRepository = new ProdusRepository();
    }

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Restaurant mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nume = rs.getString("nume");
        int idManager = rs.getInt("id_manager");

        Manager manager = null;
        Optional<Manager> optionalManager = managerRepository.findById(idManager);
        if (optionalManager.isPresent()) {
            manager = optionalManager.get();
        }

        Restaurant r = new Restaurant(id, nume, manager);

        String sqlArticole = "SELECT id_produs, pret FROM articole_meniu WHERE id_restaurant = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sqlArticole)) {
            ps.setInt(1, id);
            try (ResultSet rsArticole = ps.executeQuery()) {
                while (rsArticole.next()) {
                    int idProdus = rsArticole.getInt("id_produs");
                    double pret = rsArticole.getDouble("pret");
                    Optional<Produs> optProdus = produsRepository.findById(idProdus);
                    if (optProdus.isPresent()) {
                        r.adaugaArticol(new ArticolMeniu(optProdus.get(), pret));
                    }
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return r;
    }

    @Override
    public void save(Restaurant entity) throws SQLException {
        String sql = "INSERT INTO restaurante (nume, id_manager) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNume());
            ps.setInt(2, entity.getManager().getId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getInt(1));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Restaurant> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM restaurante WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Restaurant> findAll() throws SQLException {
        String sql = "SELECT * FROM restaurante ORDER BY id";
        List<Restaurant> restaurante = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                restaurante.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return restaurante;
    }

    @Override
    public void update(Restaurant entity) throws SQLException {
        String sql = "UPDATE restaurante SET nume = ?, id_manager = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setInt(2, entity.getManager().getId());
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM restaurante WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
