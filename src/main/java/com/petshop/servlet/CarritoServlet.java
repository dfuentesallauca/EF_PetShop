package com.petshop.servlet;

import com.petshop.dao.DAOFactory;
import com.petshop.dao.PedidoDAO;
import com.petshop.dao.ProductoDAO;
import com.petshop.model.DetallePedido;
import com.petshop.model.ItemCarrito;
import com.petshop.model.Pedido;
import com.petshop.model.Producto;
import com.petshop.model.Usuario;
import conexion.ConexionMysql;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cliente/carrito")
public class CarritoServlet extends BaseServlet {

    private final ProductoDAO productoDAO = DAOFactory.getProductoDAO();
    private final PedidoDAO   pedidoDAO   = DAOFactory.getPedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        forward(req, resp, "/cliente/carrito.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        switch (accion != null ? accion : "") {
            case "agregar":   agregar(req, resp);   break;
            case "eliminar":  eliminar(req, resp);  break;
            case "confirmar": confirmar(req, resp); break;
            default: redirect(req, resp, "/cliente/carrito");
        }
    }

    @SuppressWarnings("unchecked")
    private List<ItemCarrito> getCarrito(HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    private void agregar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int idProducto = Integer.parseInt(req.getParameter("idProducto"));
        int cantidad   = Integer.parseInt(req.getParameter("cantidad"));
        if (cantidad < 1) cantidad = 1;

        HttpSession session = req.getSession();
        List<ItemCarrito> carrito = getCarrito(session);

        Producto producto = productoDAO.findById(idProducto);
        if (producto != null && producto.isActivo() && producto.getStock() > 0) {
            boolean encontrado = false;
            for (ItemCarrito item : carrito) {
                if (item.getProducto().getId() == idProducto) {
                    item.setCantidad(item.getCantidad() + cantidad);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) carrito.add(new ItemCarrito(producto, cantidad));
        }

        // volver a productos preservando el filtro de categoría
        String idCat = req.getParameter("idCategoria");
        String url = "/cliente/productos" + (idCat != null && !idCat.isEmpty() ? "?idCategoria=" + idCat : "");
        redirect(req, resp, url);
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int idProducto = Integer.parseInt(req.getParameter("idProducto"));
        HttpSession session = req.getSession();
        List<ItemCarrito> carrito = getCarrito(session);
        carrito.removeIf(item -> item.getProducto().getId() == idProducto);
        redirect(req, resp, "/cliente/carrito");
    }

    private void confirmar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        List<ItemCarrito> carrito = getCarrito(session);

        if (carrito.isEmpty()) {
            redirect(req, resp, "/cliente/carrito");
            return;
        }

        Usuario usuario = getUsuarioSesion(req);

        BigDecimal total = carrito.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Connection conn = null;
        try {
            conn = ConexionMysql.getConexion();
            conn.setAutoCommit(false);

            Pedido pedido = new Pedido();
            pedido.setIdUsuario(usuario.getId());
            pedido.setEstado("PENDIENTE");
            pedido.setTotal(total);
            int idPedido = pedidoDAO.save(conn, pedido);

            for (ItemCarrito item : carrito) {
                DetallePedido det = new DetallePedido();
                det.setIdPedido(idPedido);
                det.setIdProducto(item.getProducto().getId());
                det.setCantidad(item.getCantidad());
                det.setPrecioUnitario(item.getProducto().getPrecio());
                pedidoDAO.saveDetalle(conn, det);
                productoDAO.updateStock(conn, item.getProducto().getId(), item.getCantidad());
            }

            conn.commit();

            session.setAttribute("pedidoConfirmadoId", idPedido);
            session.setAttribute("carritoSnapshot", new ArrayList<>(carrito));
            session.setAttribute("pedidoTotal", total);
            session.removeAttribute("carrito");

            redirect(req, resp, "/cliente/pedido-confirmado");

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ignored) {}
            }
            session.setAttribute("errorCarrito", "No se pudo procesar el pedido: " + e.getMessage());
            redirect(req, resp, "/cliente/carrito");
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception ignored) {}
            }
        }
    }
}
