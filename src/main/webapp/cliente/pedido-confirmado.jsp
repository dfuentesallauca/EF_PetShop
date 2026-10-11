<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.petshop.model.ItemCarrito, java.math.BigDecimal" %>
<%
    Integer pedidoId = (Integer) request.getAttribute("pedidoId");
    List<ItemCarrito> snapshot = (List<ItemCarrito>) request.getAttribute("carritoSnapshot");
    BigDecimal total = (BigDecimal) request.getAttribute("pedidoTotal");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="../layout/head.jsp" />
<body class="bg-light">
<jsp:include page="../layout/navBar.jsp" />
<main class="container py-4">

    <div class="card shadow-sm col-md-8 mx-auto">
        <div class="card-body">
            <div class="text-center mb-4">
                <i class="bi bi-check-circle-fill text-success" style="font-size:3rem;"></i>
                <h3 class="fw-bold mt-2">¡Pedido confirmado!</h3>
                <p class="text-muted">Pedido <strong>#<%= pedidoId %></strong> &mdash; Pago contra entrega</p>
            </div>

            <table class="table table-hover table-bordered mb-3">
                <thead class="table-light">
                    <tr>
                        <th>Producto</th>
                        <th class="text-center">Cantidad</th>
                        <th class="text-end">Precio unit.</th>
                        <th class="text-end">Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                <% if (snapshot != null) {
                       for (ItemCarrito item : snapshot) { %>
                <tr>
                    <td><%= item.getProducto().getNombre() %></td>
                    <td class="text-center"><%= item.getCantidad() %></td>
                    <td class="text-end">S/ <%= String.format("%.2f", item.getProducto().getPrecio()) %></td>
                    <td class="text-end">S/ <%= String.format("%.2f", item.getSubtotal()) %></td>
                </tr>
                <%     }
                   } %>
                </tbody>
                <tfoot>
                    <tr class="table-light fw-bold">
                        <td colspan="3" class="text-end">Total</td>
                        <td class="text-end">S/ <%= total != null ? String.format("%.2f", total) : "0.00" %></td>
                    </tr>
                </tfoot>
            </table>

            <div class="d-flex gap-2 justify-content-end">
                <a href="${pageContext.request.contextPath}/cliente/pedidos" class="btn btn-outline-secondary">Ver mis pedidos</a>
                <a href="${pageContext.request.contextPath}/cliente/productos" class="btn btn-primary">Seguir comprando</a>
            </div>
        </div>
    </div>

</main>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>
