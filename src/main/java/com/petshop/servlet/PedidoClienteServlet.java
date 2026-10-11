package com.petshop.servlet;

import com.petshop.dao.DAOFactory;
import com.petshop.dao.PedidoDAO;
import com.petshop.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cliente/pedidos")
public class PedidoClienteServlet extends BaseServlet {

    private final PedidoDAO dao = DAOFactory.getPedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = getUsuarioSesion(req);
        req.setAttribute("listaPedidos",      dao.findByUsuario(usuario.getId()));
        req.setAttribute("detallesPorPedido", dao.findDetallesByUsuario(usuario.getId()));
        forward(req, resp, "/cliente/pedidos.jsp");
    }
}
