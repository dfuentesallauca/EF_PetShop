<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.petshop.model.Categoria" %>
<%
    Categoria cat     = (Categoria) request.getAttribute("categoria");
    boolean esEdicion = (cat != null);
    String accion     = esEdicion ? "actualizar" : "guardar";
    int    catId      = esEdicion ? cat.getId()     : 0;
    String catNombre  = esEdicion ? cat.getNombre() : "";
    String colorBtn   = esEdicion ? "warning"   : "primary";
    String labelBtn   = esEdicion ? "Actualizar" : "Guardar";
    String tituloForm = esEdicion ? "Editar categoría" : "Nueva categoría";
    String status     = request.getParameter("status");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="../layout/head.jsp" />
<body class="bg-light">
<jsp:include page="../layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-4">Gestión de Categorías</h2>

    <% if ("ok".equals(status)) { %>
    <div class="alert alert-success alert-dismissible fade show">
        Operación realizada correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } else if ("desactivado".equals(status)) { %>
    <div class="alert alert-success alert-dismissible fade show">
        Categoría desactivada.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <div class="row g-4">

        <%-- formulario --%>
        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h5 class="card-title mb-3"><%= tituloForm %></h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin/categorias">
                        <input type="hidden" name="accion" value="<%= accion %>">
                        <input type="hidden" name="id"     value="<%= catId %>">
                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" id="nombre" name="nombre" class="form-control"
                                   value="<%= catNombre %>" required autofocus>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-<%= colorBtn %>"><%= labelBtn %></button>
                            <% if (esEdicion) { %>
                            <a href="${pageContext.request.contextPath}/admin/categorias?accion=listar"
                               class="btn btn-outline-secondary">Cancelar</a>
                            <% } %>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <%-- tabla (oculta en modo edicion) --%>
        <% if (!esEdicion) { %>
        <div class="col-md-7">
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-hover table-bordered mb-0">
                        <thead class="table-light">
                            <tr><th>#</th><th>Nombre</th><th>Estado</th><th>Acciones</th></tr>
                        </thead>
                        <tbody>
                        <%
                            List<Categoria> lista = (List<Categoria>) request.getAttribute("listaCategorias");
                            if (lista != null) {
                                for (Categoria c : lista) {
                        %>
                        <tr>
                            <td><%= c.getId() %></td>
                            <td><%= c.getNombre() %></td>
                            <td>
                                <% if (c.isActivo()) { %>
                                    <span class="badge bg-success">Activo</span>
                                <% } else { %>
                                    <span class="badge bg-secondary">Inactivo</span>
                                <% } %>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/categorias?accion=editar&id=<%= c.getId() %>"
                                   class="btn btn-sm btn-outline-primary me-1">
                                    <i class="bi bi-pencil"></i> Editar
                                </a>
                                <% if (c.isActivo()) { %>
                                <a href="${pageContext.request.contextPath}/admin/categorias?accion=desactivar&id=<%= c.getId() %>"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Desactivar esta categoria?')">
                                    <i class="bi bi-slash-circle"></i> Desactivar
                                </a>
                                <% } else { %>
                                <a href="${pageContext.request.contextPath}/admin/categorias?accion=activar&id=<%= c.getId() %>"
                                   class="btn btn-sm btn-outline-success"
                                   onclick="return confirm('¿Activar esta categoria?')">
                                    <i class="bi bi-check-circle"></i> Activar
                                </a>
                                <% } %>
                            </td>
                        </tr>
                        <%      }
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <% } %>

    </div>
</main>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>
