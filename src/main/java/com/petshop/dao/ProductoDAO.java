package com.petshop.dao;

import com.petshop.model.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends BaseDAO {

    private static final String SELECT_COLS =
            "SELECT p.id_producto, p.id_categoria, c.nombre AS cat_nombre, " +
            "p.nombre, p.precio, p.stock, p.imagen_url, p.activo " +
            "FROM PRODUCTO p JOIN CATEGORIA c ON p.id_categoria = c.id_categoria ";

    public List<Producto> findAll() {
        List<Producto> lista = new ArrayList<>();
        String sql = SELECT_COLS + "ORDER BY p.nombre";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando productos", e);
        }
        return lista;
    }

    // solo productos activos, para el area de clientes
    public List<Producto> findAllActive() {
        List<Producto> lista = new ArrayList<>();
        String sql = SELECT_COLS + "WHERE p.activo = 1 ORDER BY p.nombre";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando productos activos", e);
        }
        return lista;
    }

    public List<Producto> findByCategoria(int idCategoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = SELECT_COLS + "WHERE p.activo = 1 AND p.id_categoria = ? ORDER BY p.nombre";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error filtrando productos por categoría", e);
        }
        return lista;
    }

    public Producto findById(int id) {
        String sql = SELECT_COLS + "WHERE p.id_producto = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando producto", e);
        }
        return null;
    }

    public void save(Producto producto) {
        String sql = "INSERT INTO PRODUCTO (id_categoria, nombre, precio, stock, imagen_url) VALUES (?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, producto.getIdCategoria());
            ps.setString(2, producto.getNombre());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagenUrl());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando producto", e);
        }
    }

    public void update(Producto producto) {
        String sql = "UPDATE PRODUCTO SET id_categoria=?, nombre=?, precio=?, stock=?, imagen_url=? WHERE id_producto=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, producto.getIdCategoria());
            ps.setString(2, producto.getNombre());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagenUrl());
            ps.setInt(6, producto.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando producto", e);
        }
    }

    public void deactivate(int id) {
        String sql = "UPDATE PRODUCTO SET activo = 0 WHERE id_producto = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error desactivando producto", e);
        }
    }

    public void activate(int id) {
        String sql = "UPDATE PRODUCTO SET activo = 1 WHERE id_producto = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error activando producto", e);
        }
    }

    // usado dentro de la transaccion de pedido; recibe conexion externa
    public void updateStock(Connection conn, int idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE PRODUCTO SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new SQLException("Stock insuficiente para producto id=" + idProducto);
        }
    }

    private Producto mapRow(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setCategoriaNombre(rs.getString("cat_nombre"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setImagenUrl(rs.getString("imagen_url"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}
