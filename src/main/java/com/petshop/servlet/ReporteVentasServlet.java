package com.petshop.servlet;

import com.petshop.dao.DAOFactory;
import com.petshop.dao.PedidoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/admin/reportes/ventas")
public class ReporteVentasServlet extends BaseServlet {

    private final PedidoDAO dao = DAOFactory.getPedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String desde = req.getParameter("desde");
        String hasta = req.getParameter("hasta");

        if (desde != null && !desde.isEmpty() && hasta != null && !hasta.isEmpty()) {
            LocalDate d = LocalDate.parse(desde);
            LocalDate h = LocalDate.parse(hasta);
            req.setAttribute("listaPedidos",      dao.findByDateRange(d, h));
            req.setAttribute("detallesPorPedido", dao.findDetallesByDateRange(d, h));
            req.setAttribute("desde", desde);
            req.setAttribute("hasta", hasta);
        }

        forward(req, resp, "/admin/reportes/ventas.jsp");
    }
}
