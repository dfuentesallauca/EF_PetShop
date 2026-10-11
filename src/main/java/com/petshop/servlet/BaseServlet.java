package com.petshop.servlet;

import com.petshop.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    // devuelve el usuario de sesion o null si no hay sesion activa
    protected Usuario getUsuarioSesion(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (Usuario) session.getAttribute("usuario") : null;
    }

    protected void forward(HttpServletRequest req, HttpServletResponse resp, String jspPath)
            throws ServletException, IOException {
        req.getRequestDispatcher(jspPath).forward(req, resp);
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String path)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }
}
