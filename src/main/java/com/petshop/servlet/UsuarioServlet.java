package com.petshop.servlet;

import com.petshop.dao.DAOFactory;
import com.petshop.dao.UsuarioDAO;
import com.petshop.model.Usuario;
import com.petshop.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/usuarios")
public class UsuarioServlet extends BaseServlet {

    private final UsuarioDAO dao = DAOFactory.getUsuarioDAO();

    protected void manejarSolicitud(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";
        switch (accion) {
            case "listar":       listar(req, resp);       break;
            case "editar":       editar(req, resp);       break;
            case "guardar":      guardar(req, resp);      break;
            case "actualizar":   actualizar(req, resp);   break;
            case "toggleActivo": toggleActivo(req, resp); break;
            default:             listar(req, resp);
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
        req.setAttribute("listaUsuarios", dao.findAll());
        forward(req, resp, "/admin/usuarios.jsp");
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("usuario", dao.findById(id));
        forward(req, resp, "/admin/usuarios.jsp");
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Usuario u = new Usuario();
        u.setNombre(req.getParameter("nombre").trim());
        u.setEmail(req.getParameter("email").trim());
        u.setPasswordHash(PasswordUtil.hash(req.getParameter("password")));
        u.setRol(req.getParameter("rol"));
        dao.save(u);
        redirect(req, resp, "/admin/usuarios?accion=listar&status=ok");
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Usuario u = new Usuario();
        u.setId(Integer.parseInt(req.getParameter("id")));
        u.setNombre(req.getParameter("nombre").trim());
        u.setEmail(req.getParameter("email").trim());
        u.setRol(req.getParameter("rol"));
        dao.update(u);
        redirect(req, resp, "/admin/usuarios?accion=listar&status=ok");
    }

    private void toggleActivo(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        dao.toggleActivo(Integer.parseInt(req.getParameter("id")));
        redirect(req, resp, "/admin/usuarios?accion=listar&status=ok");
    }
}
