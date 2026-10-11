package com.petshop.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/cliente/pedido-confirmado")
public class PedidoConfirmadoServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        // sin snapshot: el usuario llegó directamente, no tras confirmar
        if (session == null || session.getAttribute("pedidoConfirmadoId") == null) {
            redirect(req, resp, "/cliente/productos");
            return;
        }

        req.setAttribute("pedidoId",       session.getAttribute("pedidoConfirmadoId"));
        req.setAttribute("carritoSnapshot", session.getAttribute("carritoSnapshot"));
        req.setAttribute("pedidoTotal",    session.getAttribute("pedidoTotal"));

        session.removeAttribute("pedidoConfirmadoId");
        session.removeAttribute("carritoSnapshot");
        session.removeAttribute("pedidoTotal");

        forward(req, resp, "/cliente/pedido-confirmado.jsp");
    }
}
