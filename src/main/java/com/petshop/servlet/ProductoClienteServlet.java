package com.petshop.servlet;

import com.petshop.dao.CategoriaDAO;
import com.petshop.dao.DAOFactory;
import com.petshop.dao.ProductoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cliente/productos")
public class ProductoClienteServlet extends BaseServlet {

    private final ProductoDAO  productoDAO  = DAOFactory.getProductoDAO();
    private final CategoriaDAO categoriaDAO = DAOFactory.getCategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("listaCategorias", categoriaDAO.findAll());

        String param = req.getParameter("idCategoria");
        if (param != null && !param.isEmpty()) {
            int idCat = Integer.parseInt(param);
            req.setAttribute("listaProductos", productoDAO.findByCategoria(idCat));
            req.setAttribute("categoriaSeleccionada", idCat);
        } else {
            req.setAttribute("listaProductos", productoDAO.findAllActive());
            req.setAttribute("categoriaSeleccionada", 0);
        }

        forward(req, resp, "/cliente/productos.jsp");
    }
}
