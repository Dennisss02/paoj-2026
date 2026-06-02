package com.pao.project.repository;

import com.pao.project.model.Client;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon")
        );
    }

    @Override
    public void save(Client entity) throws SQLException {
        String sqlPersoane = "INSERT INTO persoane (nume, prenume, email, telefon) VALUES (?, ?, ?, ?)";
        String sqlClienti = "INSERT INTO clienti (id) VALUES (?)";
        try {
            Connection conn = getConn();
            conn.setAutoCommit(false);
            try (PreparedStatement psP = conn.prepareStatement(sqlPersoane, Statement.RETURN_GENERATED_KEYS)) {
                psP.setString(1, entity.getNume());
                psP.setString(2, entity.getPrenume());
                psP.setString(3, entity.getEmail());
                psP.setString(4, entity.getTelefon());
                psP.executeUpdate();
                try (ResultSet keys = psP.getGeneratedKeys()) {
                    if (keys.next()) {
                        entity.setId(keys.getInt(1));
                    }
                }
            }
            try (PreparedStatement psC = conn.prepareStatement(sqlClienti)) {
                psC.setInt(1, entity.getId());
                psC.executeUpdate();
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Client> findById(Integer id) throws SQLException {
        String sql = "SELECT p.id, p.nume, p.prenume, p.email, p.telefon FROM clienti c JOIN persoane p ON c.id = p.id WHERE c.id = ?";
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
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT p.id, p.nume, p.prenume, p.email, p.telefon FROM clienti c JOIN persoane p ON c.id = p.id ORDER BY p.id";
        List<Client> clienti = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clienti.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return clienti;
    }

    @Override
    public void update(Client entity) throws SQLException {
        String sql = "UPDATE persoane SET nume = ?, prenume = ?, email = ?, telefon = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getPrenume());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getTelefon());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM persoane WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
