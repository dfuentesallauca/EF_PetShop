package com.petshop.dao;

import com.petshop.model.DetallePedido;
import com.petshop.model.Pedido;
import com.petshop.model.Producto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoDAO extends BaseDAO {

    // --- Phase 6: transaccionales (reciben conexion externa) ---

    public int save(Connection conn, Pedido pedido) throws SQLException {
        String sql = "INSERT INTO PEDIDO (id_usuario, fecha, estado, total) VALUES (?, NOW(), ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getIdUsuario());
            ps.setString(2, pedido.getEstado());
            ps.setBigDecimal(3, pedido.getTotal());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se generó ID para el pedido");
    }

    public void saveDetalle(Connection conn, DetallePedido detalle) throws SQLException {
        String sql = "INSERT INTO DETALLE_PEDIDO (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecioUnitario());
            ps.setBigDecimal(5, detalle.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(detalle.getCantidad())));
            ps.executeUpdate();
        }
    }

    // carga todos los detalles del usuario en una sola consulta
    public Map<Integer, List<DetallePedido>> findDetallesByUsuario(int idUsuario) {
        Map<Integer, List<DetallePedido>> map = new LinkedHashMap<>();
        String sql = "SELECT dp.id_detalle, dp.id_pedido, dp.id_producto, dp.cantidad, dp.precio_unitario, " +
                     "pr.nombre AS producto_nombre " +
                     "FROM DETALLE_PEDIDO dp " +
                     "JOIN PEDIDO p  ON dp.id_pedido  = p.id_pedido " +
                     "JOIN PRODUCTO pr ON dp.id_producto = pr.id_producto " +
                     "WHERE p.id_usuario = ? ORDER BY dp.id_pedido";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = mapDetalleRow(rs);
                    d.setProductoNombre(rs.getString("producto_nombre"));
                    map.computeIfAbsent(d.getIdPedido(), k -> new ArrayList<>()).add(d);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error cargando detalles de pedidos", e);
        }
        return map;
    }

    // --- consultas y reportes ---

    public List<DetallePedido> findDetallesByPedido(int idPedido) {
        List<DetallePedido> lista = new ArrayList<>();
        String sql = "SELECT id_detalle, id_pedido, id_producto, cantidad, precio_unitario " +
                     "FROM DETALLE_PEDIDO WHERE id_pedido = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapDetalleRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando detalles de pedido", e);
        }
        return lista;
    }

    public Pedido findById(int id) {
        String sql = "SELECT id_pedido, id_usuario, fecha, estado, total FROM PEDIDO WHERE id_pedido = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando pedido", e);
        }
        return null;
    }

    // historial de pedidos del cliente autenticado
    public List<Pedido> findByUsuario(int idUsuario) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id_pedido, id_usuario, fecha, estado, total " +
                     "FROM PEDIDO WHERE id_usuario = ? ORDER BY fecha DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando pedidos del usuario", e);
        }
        return lista;
    }

    // listado completo para el admin
    public List<Pedido> findAll() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id_pedido, p.id_usuario, p.fecha, p.estado, p.total, " +
                     "u.nombre AS cliente_nombre " +
                     "FROM PEDIDO p JOIN USUARIO u ON p.id_usuario = u.id_usuario " +
                     "ORDER BY p.fecha DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido ped = mapRow(rs);
                ped.setClienteNombre(rs.getString("cliente_nombre"));
                lista.add(ped);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando pedidos", e);
        }
        return lista;
    }

    // reporte: ventas por rango de fechas; CAST a DATE para ignorar la hora
    // carga todos los detalles del rango de fechas en una sola consulta
    public Map<Integer, List<DetallePedido>> findDetallesByDateRange(LocalDate desde, LocalDate hasta) {
        Map<Integer, List<DetallePedido>> map = new LinkedHashMap<>();
        String sql = "SELECT dp.id_detalle, dp.id_pedido, dp.id_producto, dp.cantidad, dp.precio_unitario, " +
                     "pr.nombre AS producto_nombre " +
                     "FROM DETALLE_PEDIDO dp " +
                     "JOIN PEDIDO p  ON dp.id_pedido  = p.id_pedido " +
                     "JOIN PRODUCTO pr ON dp.id_producto = pr.id_producto " +
                     "WHERE CAST(p.fecha AS DATE) BETWEEN ? AND ? ORDER BY dp.id_pedido";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = mapDetalleRow(rs);
                    d.setProductoNombre(rs.getString("producto_nombre"));
                    map.computeIfAbsent(d.getIdPedido(), k -> new ArrayList<>()).add(d);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error cargando detalles de ventas", e);
        }
        return map;
    }

    public List<Pedido> findByDateRange(LocalDate desde, LocalDate hasta) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id_pedido, p.id_usuario, p.fecha, p.estado, p.total, " +
                     "u.nombre AS cliente_nombre " +
                     "FROM PEDIDO p JOIN USUARIO u ON p.id_usuario = u.id_usuario " +
                     "WHERE CAST(p.fecha AS DATE) BETWEEN ? AND ? " +
                     "ORDER BY p.fecha DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido ped = mapRow(rs);
                    ped.setClienteNombre(rs.getString("cliente_nombre"));
                    lista.add(ped);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en reporte de ventas", e);
        }
        return lista;
    }

    // reporte: productos mas vendidos; totalVendido seteado en cada Producto
    public List<Producto> findTopProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT pr.id_producto, pr.id_categoria, c.nombre AS cat_nombre, " +
                     "pr.nombre, pr.precio, pr.stock, pr.imagen_url, pr.activo, " +
                     "SUM(dp.cantidad) AS total_vendido " +
                     "FROM DETALLE_PEDIDO dp " +
                     "JOIN PRODUCTO pr ON dp.id_producto = pr.id_producto " +
                     "JOIN CATEGORIA c  ON pr.id_categoria = c.id_categoria " +
                     "GROUP BY pr.id_producto, pr.id_categoria, c.nombre, pr.nombre, " +
                     "         pr.precio, pr.stock, pr.imagen_url, pr.activo " +
                     "ORDER BY total_vendido DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id_producto"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setCategoriaNombre(rs.getString("cat_nombre"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getInt("stock"));
                p.setImagenUrl(rs.getString("imagen_url"));
                p.setActivo(rs.getBoolean("activo"));
                p.setTotalVendido(rs.getInt("total_vendido"));
                lista.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en reporte de top productos", e);
        }
        return lista;
    }

    private Pedido mapRow(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id_pedido"));
        p.setIdUsuario(rs.getInt("id_usuario"));
        Timestamp ts = rs.getTimestamp("fecha");
        if (ts != null) p.setFecha(ts.toLocalDateTime());
        p.setEstado(rs.getString("estado"));
        p.setTotal(rs.getBigDecimal("total"));
        return p;
    }

    private DetallePedido mapDetalleRow(ResultSet rs) throws SQLException {
        DetallePedido d = new DetallePedido();
        d.setId(rs.getInt("id_detalle"));
        d.setIdPedido(rs.getInt("id_pedido"));
        d.setIdProducto(rs.getInt("id_producto"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        return d;
    }
}
