package com.petshop.filter;

import com.petshop.model.Usuario;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// protege todas las rutas bajo /cliente/* y /admin/*
@WebFilter(urlPatterns = {"/cliente/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String uri = req.getRequestURI();
        String rol = usuario.getRol();

        // redirige si el rol no corresponde al area solicitada
        if (uri.contains("/admin/") && !"ADMIN".equals(rol)) {
            resp.sendRedirect(req.getContextPath() + "/cliente/productos");
            return;
        }
        if (uri.contains("/cliente/") && !"CLIENTE".equals(rol)) {
            resp.sendRedirect(req.getContextPath() + "/admin/panel");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig config) throws ServletException {}
    @Override public void destroy() {}
}
