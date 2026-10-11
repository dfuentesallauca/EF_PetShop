<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.petshop.model.Producto, com.petshop.model.Categoria" %>
<%
    List<Producto>  listaProductos       = (List<Producto>)  request.getAttribute("listaProductos");
    List<Categoria> listaCategorias      = (List<Categoria>) request.getAttribute("listaCategorias");
    int             categoriaSeleccionada = (Integer)        request.getAttribute("categoriaSeleccionada");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="../layout/head.jsp" />
<body class="bg-light">
<jsp:include page="../layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-4">Cat&aacute;logo de Productos</h2>

    <%-- filtro por categoria --%>
    <form method="get" action="${pageContext.request.contextPath}/cliente/productos" class="row g-2 mb-4">
        <div class="col-auto">
            <select name="idCategoria" class="form-select">
                <option value="">Todas las categor&iacute;as</option>
                <% if (listaCategorias != null) {
                       for (Categoria cat : listaCategorias) {
                           if (!cat.isActivo()) continue;
                           String sel = (categoriaSeleccionada == cat.getId()) ? "selected" : "";
                %>
                <option value="<%= cat.getId() %>" <%= sel %>><%= cat.getNombre() %></option>
                <%     }
                   } %>
            </select>
        </div>
        <div class="col-auto">
            <button type="submit" class="btn btn-primary">Filtrar</button>
        </div>
        <% if (categoriaSeleccionada != 0) { %>
        <div class="col-auto">
            <a href="${pageContext.request.contextPath}/cliente/productos" class="btn btn-outline-secondary">Ver todos</a>
        </div>
        <% } %>
    </form>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-hover table-bordered mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Producto</th>
                        <th>Categor&iacute;a</th>
                        <th>Precio</th>
                        <th>Stock disponible</th>                        <th></th>                    </tr>
                </thead>
                <tbody>
                <% if (listaProductos == null || listaProductos.isEmpty()) { %>
                <tr><td colspan="4" class="text-center text-muted py-4">No hay productos disponibles.</td></tr>
                <% } else {
                       for (Producto p : listaProductos) { %>
                <tr>
                    <td class="fw-semibold"><%= p.getNombre() %></td>
                    <td><%= p.getCategoriaNombre() %></td>
                    <td>S/ <%= String.format("%.2f", p.getPrecio()) %></td>
                    <td>
                        <% if (p.getStock() > 0) { %>
                            <span class="badge bg-success"><%= p.getStock() %> disponibles</span>
                        <% } else { %>
                            <span class="badge bg-secondary">Sin stock</span>
                        <% } %>
                    </td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/cliente/carrito" class="d-flex gap-1 align-items-center">
                            <input type="hidden" name="accion" value="agregar" />
                            <input type="hidden" name="idProducto" value="<%= p.getId() %>" />
                            <% if (categoriaSeleccionada != 0) { %>
                            <input type="hidden" name="idCategoria" value="<%= categoriaSeleccionada %>" />
                            <% } %>
                            <input type="number" name="cantidad" value="1" min="1" max="<%= p.getStock() %>"
                                   class="form-control form-control-sm" style="width:65px;"
                                   <%= p.getStock() == 0 ? "disabled" : "" %>>
                            <button type="submit" class="btn btn-sm btn-primary" <%= p.getStock() == 0 ? "disabled" : "" %>>
                                <i class="bi bi-cart-plus"></i>
                            </button>
                        </form>
                    </td>
                </tr>
                <%     }
                   } %>
                </tbody>
            </table>
        </div>
    </div>

</main>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>
