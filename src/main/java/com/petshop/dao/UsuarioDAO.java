package com.petshop.dao;

import com.petshop.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends BaseDAO {

    public Usuario findByEmail(String email) {
        String sql = "SELECT id_usuario, nombre, email, password_hash, rol, activo, fecha_registro " +
                     "FROM USUARIO WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando usuario por email", e);
        }
        return null;
    }

    // mapea una fila del ResultSet a un objeto Usuario
    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRol(rs.getString("rol"));
        u.setActivo(rs.getBoolean("activo"));
        u.setFechaRegistro(rs.getObject("fecha_registro", LocalDateTime.class));
        return u;
    }

    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, email, password_hash, rol, activo, fecha_registro " +
                     "FROM USUARIO ORDER BY nombre";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando usuarios", e);
        }
        return lista;
    }

    public Usuario findById(int id) {
        String sql = "SELECT id_usuario, nombre, email, password_hash, rol, activo, fecha_registro " +
                     "FROM USUARIO WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando usuario", e);
        }
        return null;
    }

    public void save(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (nombre, email, password_hash, rol) VALUES (?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPasswordHash());
            ps.setString(4, usuario.getRol());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando usuario", e);
        }
    }

    // no actualiza password; usar metodo dedicado si se requiere
    public void update(Usuario usuario) {
        String sql = "UPDATE USUARIO SET nombre=?, email=?, rol=? WHERE id_usuario=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getRol());
            ps.setInt(4, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando usuario", e);
        }
    }

    public void toggleActivo(int id) {
        String sql = "UPDATE USUARIO SET activo = NOT activo WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error cambiando estado del usuario", e);
        }
    }
}
