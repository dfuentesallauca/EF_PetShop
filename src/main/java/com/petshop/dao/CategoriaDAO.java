package com.petshop.dao;

import com.petshop.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO extends BaseDAO {

    public List<Categoria> findAll() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, activo FROM CATEGORIA ORDER BY nombre";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando categorías", e);
        }
        return lista;
    }

    public Categoria findById(int id) {
        String sql = "SELECT id_categoria, nombre, activo FROM CATEGORIA WHERE id_categoria = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando categoría", e);
        }
        return null;
    }

    public void save(Categoria categoria) {
        String sql = "INSERT INTO CATEGORIA (nombre) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando categoría", e);
        }
    }

    public void update(Categoria categoria) {
        String sql = "UPDATE CATEGORIA SET nombre = ? WHERE id_categoria = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando categoría", e);
        }
    }

    public void deactivate(int id) {
        String sql = "UPDATE CATEGORIA SET activo = 0 WHERE id_categoria = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error desactivando categoría", e);
        }
    }

    public void activate(int id) {
        String sql = "UPDATE CATEGORIA SET activo = 1 WHERE id_categoria = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error activando categoría", e);
        }
    }

    private Categoria mapRow(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getInt("id_categoria"));
        c.setNombre(rs.getString("nombre"));
        c.setActivo(rs.getBoolean("activo"));
        return c;
    }
}
