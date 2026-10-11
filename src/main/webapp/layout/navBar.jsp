<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.petshop.model.Usuario, com.petshop.model.ItemCarrito, java.util.List" %>
<%
    Usuario navUsuario = (Usuario) session.getAttribute("usuario");
    boolean esAdmin = navUsuario != null && "ADMIN".equals(navUsuario.getRol());
    List<ItemCarrito> navCarrito = (List<ItemCarrito>) session.getAttribute("carrito");
    int navTotalItems = 0;
    if (navCarrito != null) for (ItemCarrito ic : navCarrito) navTotalItems += ic.getCantidad();
%>
<nav class="navbar navbar-expand-md navbar-dark mb-4" style="background-color:#2C7A7B;">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">🐾 Patitas Felices</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMain">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navMain">
            <ul class="navbar-nav ms-auto align-items-center gap-1">
                <% if (esAdmin) { %>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/panel">Panel</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/categorias?accion=listar">Categor&iacute;as</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/productos?accion=listar">Productos</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/usuarios?accion=listar">Usuarios</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">Reportes</a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/reportes/ventas">Ventas por fecha</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/reportes/top-productos">Productos m&aacute;s vendidos</a></li>
                        </ul>
                    </li>
                <% } else { %>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/cliente/productos">Productos</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/cliente/carrito"><i class="bi bi-cart3 me-1"></i>Carrito<% if (navTotalItems > 0) { %> <span class="badge rounded-pill" style="background-color:#F4A261; color:#212529;"><%= navTotalItems %></span><% } %></a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/cliente/pedidos">Mis pedidos</a></li>
                <% } %>
                <li class="nav-item ms-2">
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                        <i class="bi bi-box-arrow-right me-1"></i><%= navUsuario != null ? navUsuario.getNombre() : "" %>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
