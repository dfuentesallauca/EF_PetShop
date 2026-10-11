package com.petshop.servlet;

import com.petshop.dao.CategoriaDAO;
import com.petshop.dao.DAOFactory;
import com.petshop.dao.ProductoDAO;
import com.petshop.model.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/productos")
public class ProductoServlet extends BaseServlet {

    private final ProductoDAO  dao    = DAOFactory.getProductoDAO();
    private final CategoriaDAO catDao = DAOFactory.getCategoriaDAO();

    protected void manejarSolicitud(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";
        switch (accion) {
            case "listar":     listar(req, resp);     break;
            case "editar":     editar(req, resp);     break;
            case "guardar":    guardar(req, resp);    break;
            case "actualizar": actualizar(req, resp); break;
            case "desactivar": desactivar(req, resp); break;
            case "activar":    activar(req, resp);    break;
            default:           listar(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        manejarSolicitud(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        manejarSolicitud(req, resp);
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("listaProductos",  dao.findAll());
        req.setAttribute("listaCategorias", catDao.findAll());
        forward(req, resp, "/admin/productos.jsp");
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("producto",        dao.findById(id));
        req.setAttribute("listaCategorias", catDao.findAll());
        forward(req, resp, "/admin/productos.jsp");
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        dao.save(fromRequest(req));
        redirect(req, resp, "/admin/productos?accion=listar&status=ok");
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Producto p = fromRequest(req);
        p.setId(Integer.parseInt(req.getParameter("id")));
        dao.update(p);
        redirect(req, resp, "/admin/productos?accion=listar&status=ok");
    }

    private void desactivar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        dao.deactivate(Integer.parseInt(req.getParameter("id")));
        redirect(req, resp, "/admin/productos?accion=listar&status=desactivado");
    }

    private void activar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        dao.activate(Integer.parseInt(req.getParameter("id")));
        redirect(req, resp, "/admin/productos?accion=listar&status=ok");
    }

    // extrae campos comunes del formulario
    private Producto fromRequest(HttpServletRequest req) {
        Producto p = new Producto();
        p.setIdCategoria(Integer.parseInt(req.getParameter("idCategoria")));
        p.setNombre(req.getParameter("nombre").trim());
        p.setPrecio(new BigDecimal(req.getParameter("precio")));
        p.setStock(Integer.parseInt(req.getParameter("stock")));
        String img = req.getParameter("imagenUrl");
        p.setImagenUrl((img != null && !img.isBlank()) ? img.trim() : null);
        return p;
    }
}
