package com.petshop.servlet;

import com.petshop.dao.DAOFactory;
import com.petshop.dao.UsuarioDAO;
import com.petshop.model.Usuario;
import com.petshop.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // si ya hay sesion activa, redirige directamente al area correspondiente
        Usuario usuario = getUsuarioSesion(req);
        if (usuario != null) {
            redirectByRole(req, resp, usuario);
            return;
        }
        forward(req, resp, "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        UsuarioDAO dao = DAOFactory.getUsuarioDAO();
        Usuario usuario = dao.findByEmail(email);

        if (usuario == null || !PasswordUtil.verify(password, usuario.getPasswordHash())) {
            req.setAttribute("error", "Correo o contraseña incorrectos.");
            forward(req, resp, "/login.jsp");
            return;
        }

        if (!usuario.isActivo()) {
            req.setAttribute("error", "Tu cuenta está desactivada. Contacta al administrador.");
            forward(req, resp, "/login.jsp");
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("usuario", usuario);
        redirectByRole(req, resp, usuario);
    }

    private void redirectByRole(HttpServletRequest req, HttpServletResponse resp, Usuario usuario)
            throws IOException {
        if ("ADMIN".equals(usuario.getRol())) {
            redirect(req, resp, "/admin/panel");
        } else {
            redirect(req, resp, "/cliente/productos");
        }
    }
}
