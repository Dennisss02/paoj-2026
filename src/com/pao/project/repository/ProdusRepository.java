package com.pao.project.repository;

import com.pao.project.model.Produs;
import com.pao.project.model.enums.CategorieProdus;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdusRepository implements Repository<Produs, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Produs mapRow(ResultSet rs) throws SQLException {
        return new Produs(
                rs.getInt("id"),
                rs.getString("nume"),
                CategorieProdus.valueOf(rs.getString("categorie_produs"))
        );
    }

    @Override
    public void save(Produs entity) throws SQLException {
        String sql = "INSERT INTO produse (nume, categorie_produs) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getCategorieProdus().name());
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
    public Optional<Produs> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM produse WHERE id = ?";
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
    public List<Produs> findAll() throws SQLException {
        String sql = "SELECT * FROM produse ORDER BY id";
        List<Produs> produse = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                produse.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return produse;
    }

    @Override
    public void update(Produs entity) throws SQLException {
        String sql = "UPDATE produse SET nume = ?, categorie_produs = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getCategorieProdus().name());
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM produse WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
