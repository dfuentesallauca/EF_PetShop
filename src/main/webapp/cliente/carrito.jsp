<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.petshop.model.ItemCarrito, java.math.BigDecimal" %>
<%
    List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
    String errorCarrito = (String) session.getAttribute("errorCarrito");
    if (errorCarrito != null) session.removeAttribute("errorCarrito");
    boolean vacio = (carrito == null || carrito.isEmpty());

    BigDecimal total = BigDecimal.ZERO;
    if (!vacio) {
        for (ItemCarrito item : carrito) {
            total = total.add(item.getSubtotal());
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="../layout/head.jsp" />
<body class="bg-light">
<jsp:include page="../layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-4">Carrito de compras</h2>

    <% if (errorCarrito != null) { %>
    <div class="alert alert-danger"><%= errorCarrito %></div>
    <% } %>

    <% if (vacio) { %>
    <div class="card shadow-sm">
        <div class="card-body text-center py-5 text-muted">
            Tu carrito está vacío.
            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/cliente/productos" class="btn btn-primary">Ver productos</a>
            </div>
        </div>
    </div>
    <% } else { %>
    <div class="card shadow-sm mb-4">
        <div class="card-body p-0">
            <table class="table table-hover table-bordered mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Producto</th>
                        <th class="text-end">Precio unit.</th>
                        <th class="text-center">Cantidad</th>
                        <th class="text-end">Subtotal</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <% for (ItemCarrito item : carrito) { %>
                <tr>
                    <td class="fw-semibold"><%= item.getProducto().getNombre() %></td>
                    <td class="text-end">S/ <%= String.format("%.2f", item.getProducto().getPrecio()) %></td>
                    <td class="text-center"><%= item.getCantidad() %></td>
                    <td class="text-end">S/ <%= String.format("%.2f", item.getSubtotal()) %></td>
                    <td class="text-center">
                        <form method="post" action="${pageContext.request.contextPath}/cliente/carrito">
                            <input type="hidden" name="accion" value="eliminar" />
                            <input type="hidden" name="idProducto" value="<%= item.getProducto().getId() %>" />
                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                <i class="bi bi-trash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
                <% } %>
                </tbody>
                <tfoot>
                    <tr class="table-light fw-bold">
                        <td colspan="3" class="text-end">Total</td>
                        <td class="text-end">S/ <%= String.format("%.2f", total) %></td>
                        <td></td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>

    <div class="d-flex gap-2 justify-content-end">
        <a href="${pageContext.request.contextPath}/cliente/productos" class="btn btn-outline-secondary">Seguir comprando</a>
        <form method="post" action="${pageContext.request.contextPath}/cliente/carrito">
            <input type="hidden" name="accion" value="confirmar" />
            <button type="submit" class="btn btn-primary">Confirmar pedido</button>
        </form>
    </div>
    <% } %>

</main>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>
