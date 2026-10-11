package com.petshop.servlet;

import com.petshop.dao.DAOFactory;
import com.petshop.dao.PedidoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/reportes/top-productos")
public class ReporteTopProductosServlet extends BaseServlet {

    private final PedidoDAO dao = DAOFactory.getPedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("listaTop", dao.findTopProductos());
        forward(req, resp, "/admin/reportes/top-productos.jsp");
    }
}
