<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.petshop.model.Producto, com.petshop.model.Categoria" %>
<%
    Producto prod        = (Producto)  request.getAttribute("producto");
    List<Categoria> cats = (List<Categoria>) request.getAttribute("listaCategorias");
    boolean esEdicion    = (prod != null);
    String accion        = esEdicion ? "actualizar" : "guardar";
    int    prodId        = esEdicion ? prod.getId()           : 0;
    String prodNombre    = esEdicion ? prod.getNombre()        : "";
    int    prodCatId     = esEdicion ? prod.getIdCategoria()   : 0;
    String prodPrecio    = esEdicion ? prod.getPrecio().toPlainString() : "";
    int    prodStock     = esEdicion ? prod.getStock()         : 0;
    String prodImg       = (esEdicion && prod.getImagenUrl() != null) ? prod.getImagenUrl() : "";
    String colorBtn      = esEdicion ? "warning"    : "primary";
    String labelBtn      = esEdicion ? "Actualizar" : "Guardar";
    String tituloForm    = esEdicion ? "Editar producto" : "Nuevo producto";
    String status        = request.getParameter("status");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="../layout/head.jsp" />
<body class="bg-light">
<jsp:include page="../layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-4">Gestión de Productos</h2>

    <% if ("ok".equals(status)) { %>
    <div class="alert alert-success alert-dismissible fade show">
        Operación realizada correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } else if ("desactivado".equals(status)) { %>
    <div class="alert alert-success alert-dismissible fade show">
        Producto desactivado.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <div class="row g-4">

        <%-- formulario --%>
        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h5 class="card-title mb-3"><%= tituloForm %></h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin/productos">
                        <input type="hidden" name="accion" value="<%= accion %>">
                        <input type="hidden" name="id"     value="<%= prodId %>">

                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" id="nombre" name="nombre" class="form-control"
                                   value="<%= prodNombre %>" required>
                        </div>

                        <div class="mb-3">
                            <label for="idCategoria" class="form-label">Categoría</label>
                            <select id="idCategoria" name="idCategoria" class="form-select" required>
                                <option value="">-- Seleccionar --</option>
                                <%
                                    if (cats != null) {
                                        for (Categoria c : cats) {
                                            if (!c.isActivo()) continue;
                                            String sel = (c.getId() == prodCatId) ? "selected" : "";
                                %>
                                <option value="<%= c.getId() %>" <%= sel %>><%= c.getNombre() %></option>
                                <%      }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="precio" class="form-label">Precio (S/.)</label>
                            <input type="number" id="precio" name="precio" class="form-control"
                                   step="0.01" min="0.01" value="<%= prodPrecio %>" required>
                        </div>

                        <div class="mb-3">
                            <label for="stock" class="form-label">Stock</label>
                            <input type="number" id="stock" name="stock" class="form-control"
                                   min="0" value="<%= prodStock %>" required>
                        </div>

                        <div class="mb-3">
                            <label for="imagenUrl" class="form-label">
                                URL de imagen <span class="text-muted">(opcional)</span>
                            </label>
                            <input type="text" id="imagenUrl" name="imagenUrl" class="form-control"
                                   value="<%= prodImg %>">
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-<%= colorBtn %>"><%= labelBtn %></button>
                            <% if (esEdicion) { %>
                            <a href="${pageContext.request.contextPath}/admin/productos?accion=listar"
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
                                <th>#</th><th>Nombre</th><th>Categoría</th>
                                <th>Precio</th><th>Stock</th><th>Estado</th><th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            List<Producto> lista = (List<Producto>) request.getAttribute("listaProductos");
                            int fila = 0;
                            if (lista != null) {
                                for (Producto p : lista) {
                                    fila++;
                        %>
                        <tr>
                            <td><%= fila %></td>
                            <td><%= p.getNombre() %></td>
                            <td><%= p.getCategoriaNombre() %></td>
                            <td>S/. <%= p.getPrecio() %></td>
                            <td><%= p.getStock() %></td>
                            <td>
                                <% if (p.isActivo()) { %>
                                    <span class="badge bg-success">Activo</span>
                                <% } else { %>
                                    <span class="badge bg-secondary">Inactivo</span>
                                <% } %>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/productos?accion=editar&id=<%= p.getId() %>"
                                   class="btn btn-sm btn-outline-primary me-1">
                                    <i class="bi bi-pencil"></i> Editar
                                </a>
                                <% if (p.isActivo()) { %>
                                <a href="${pageContext.request.contextPath}/admin/productos?accion=desactivar&id=<%= p.getId() %>"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Desactivar este producto?')">
                                    <i class="bi bi-slash-circle"></i> Desactivar
                                </a>
                                <% } else { %>
                                <a href="${pageContext.request.contextPath}/admin/productos?accion=activar&id=<%= p.getId() %>"
                                   class="btn btn-sm btn-outline-success"
                                   onclick="return confirm('¿Activar este producto?')">
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
