package com.pao.project.repository;

import com.pao.project.model.Manager;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManagerRepository implements Repository<Manager, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Manager mapRow(ResultSet rs) throws SQLException {
        return new Manager(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                rs.getInt("salariu")
        );
    }

    @Override
    public void save(Manager entity) throws SQLException {
        String sqlP = "INSERT INTO persoane (nume, prenume, email, telefon) VALUES (?, ?, ?, ?)";
        String sqlA = "INSERT INTO angajati (id, salariu, tip_angajat) VALUES (?, ?, 'MANAGER')";
        try {
            Connection conn = getConn();
            conn.setAutoCommit(false);
            try (PreparedStatement psP = conn.prepareStatement(sqlP, Statement.RETURN_GENERATED_KEYS)) {
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
            try (PreparedStatement psA = conn.prepareStatement(sqlA)) {
                psA.setInt(1, entity.getId());
                psA.setInt(2, entity.getSalariu());
                psA.executeUpdate();
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Manager> findById(Integer id) throws SQLException {
        String sql = "SELECT p.id, p.nume, p.prenume, p.email, p.telefon, a.salariu FROM angajati a JOIN persoane p ON a.id = p.id WHERE a.id = ? AND a.tip_angajat = 'MANAGER'";
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
    public List<Manager> findAll() throws SQLException {
        String sql = "SELECT p.id, p.nume, p.prenume, p.email, p.telefon, a.salariu FROM angajati a JOIN persoane p ON a.id = p.id WHERE a.tip_angajat = 'MANAGER' ORDER BY p.id";
        List<Manager> manageri = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                manageri.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return manageri;
    }

    @Override
    public void update(Manager entity) throws SQLException {
        String sqlP = "UPDATE persoane SET nume = ?, prenume = ?, email = ?, telefon = ? WHERE id = ?";
        String sqlA = "UPDATE angajati SET salariu = ? WHERE id = ? AND tip_angajat = 'MANAGER'";
        try {
            Connection conn = getConn();
            conn.setAutoCommit(false);
            try (PreparedStatement psP = conn.prepareStatement(sqlP)) {
                psP.setString(1, entity.getNume());
                psP.setString(2, entity.getPrenume());
                psP.setString(3, entity.getEmail());
                psP.setString(4, entity.getTelefon());
                psP.setInt(5, entity.getId());
                psP.executeUpdate();
            }
            try (PreparedStatement psA = conn.prepareStatement(sqlA)) {
                psA.setInt(1, entity.getSalariu());
                psA.setInt(2, entity.getId());
                psA.executeUpdate();
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM persoane WHERE id = (SELECT id FROM angajati WHERE id = ? AND tip_angajat = 'MANAGER')";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
