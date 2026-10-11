package com.petshop.servlet;

import com.petshop.dao.CategoriaDAO;
import com.petshop.dao.DAOFactory;
import com.petshop.model.Categoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/categorias")
public class CategoriaServlet extends BaseServlet {

    private final CategoriaDAO dao = DAOFactory.getCategoriaDAO();

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
        req.setAttribute("listaCategorias", dao.findAll());
        forward(req, resp, "/admin/categorias.jsp");
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("categoria", dao.findById(id));
        forward(req, resp, "/admin/categorias.jsp");
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Categoria c = new Categoria();
        c.setNombre(req.getParameter("nombre").trim());
        dao.save(c);
        redirect(req, resp, "/admin/categorias?accion=listar&status=ok");
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Categoria c = new Categoria();
        c.setId(Integer.parseInt(req.getParameter("id")));
        c.setNombre(req.getParameter("nombre").trim());
        dao.update(c);
        redirect(req, resp, "/admin/categorias?accion=listar&status=ok");
    }

    private void desactivar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        dao.deactivate(Integer.parseInt(req.getParameter("id")));
        redirect(req, resp, "/admin/categorias?accion=listar&status=desactivado");
    }

    private void activar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        dao.activate(Integer.parseInt(req.getParameter("id")));
        redirect(req, resp, "/admin/categorias?accion=listar&status=ok");
    }
}
