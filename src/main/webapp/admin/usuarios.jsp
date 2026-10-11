<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.petshop.model.Usuario" %>
<%
    Usuario usr       = (Usuario) request.getAttribute("usuario");
    boolean esEdicion = (usr != null);
    String accion     = esEdicion ? "actualizar" : "guardar";
    int    usrId      = esEdicion ? usr.getId()     : 0;
    String usrNombre  = esEdicion ? usr.getNombre() : "";
    String usrEmail   = esEdicion ? usr.getEmail()  : "";
    String usrRol     = esEdicion ? usr.getRol()    : "CLIENTE";
    String colorBtn   = esEdicion ? "warning"    : "primary";
    String labelBtn   = esEdicion ? "Actualizar" : "Crear usuario";
    String tituloForm = esEdicion ? "Editar usuario" : "Nuevo usuario";
    String status     = request.getParameter("status");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="../layout/head.jsp" />
<body class="bg-light">
<jsp:include page="../layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-4">Gestión de Usuarios</h2>

    <% if ("ok".equals(status)) { %>
    <div class="alert alert-success alert-dismissible fade show">
        Operación realizada correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <div class="row g-4">

        <%-- formulario --%>
        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h5 class="card-title mb-3"><%= tituloForm %></h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin/usuarios">
                        <input type="hidden" name="accion" value="<%= accion %>">
                        <input type="hidden" name="id"     value="<%= usrId %>">

                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" id="nombre" name="nombre" class="form-control"
                                   value="<%= usrNombre %>" required>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Correo electrónico</label>
                            <input type="email" id="email" name="email" class="form-control"
                                   value="<%= usrEmail %>" required>
                        </div>

                        <% if (!esEdicion) { %>
                        <div class="mb-3">
                            <label for="password" class="form-label">Contraseña inicial</label>
                            <input type="password" id="password" name="password" class="form-control" required>
                        </div>
                        <% } %>

                        <div class="mb-3">
                            <label for="rol" class="form-label">Rol</label>
                            <select id="rol" name="rol" class="form-select" required>
                                <option value="CLIENTE" <%= "CLIENTE".equals(usrRol) ? "selected" : "" %>>Cliente</option>
                                <option value="ADMIN"   <%= "ADMIN".equals(usrRol)   ? "selected" : "" %>>Administrador</option>
                            </select>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-<%= colorBtn %>"><%= labelBtn %></button>
                            <% if (esEdicion) { %>
                            <a href="${pageContext.request.contextPath}/admin/usuarios?accion=listar"
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
                            <tr>
                                <th>#</th><th>Nombre</th><th>Correo</th>
                                <th>Rol</th><th>Estado</th><th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            List<Usuario> lista = (List<Usuario>) request.getAttribute("listaUsuarios");
                            if (lista != null) {
                                for (Usuario u : lista) {
                                    String btnClass   = u.isActivo() ? "danger"       : "success";
                                    String btnIcon    = u.isActivo() ? "slash-circle" : "check-circle";
                                    String btnText    = u.isActivo() ? "Desactivar"   : "Activar";
                                    String confirmMsg = u.isActivo() ? "Desactivar este usuario?" : "Activar este usuario?";
                        %>
                        <tr>
                            <td><%= u.getId() %></td>
                            <td><%= u.getNombre() %></td>
                            <td><%= u.getEmail() %></td>
                            <td>
                                <% if ("ADMIN".equals(u.getRol())) { %>
                                    <span class="badge bg-primary">Admin</span>
                                <% } else { %>
                                    <span class="badge bg-info text-dark">Cliente</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (u.isActivo()) { %>
                                    <span class="badge bg-success">Activo</span>
                                <% } else { %>
                                    <span class="badge bg-secondary">Inactivo</span>
                                <% } %>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/usuarios?accion=editar&id=<%= u.getId() %>"
                                   class="btn btn-sm btn-outline-primary me-1">
                                    <i class="bi bi-pencil"></i> Editar
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/usuarios?accion=toggleActivo&id=<%= u.getId() %>"
                                   class="btn btn-sm btn-outline-<%= btnClass %>"
                                   onclick="return confirm('<%= confirmMsg %>')">
                                    <i class="bi bi-<%= btnIcon %>"></i> <%= btnText %>
                                </a>
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
