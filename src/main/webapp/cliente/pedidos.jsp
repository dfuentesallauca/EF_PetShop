<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map, com.petshop.model.Pedido, com.petshop.model.DetallePedido, java.time.format.DateTimeFormatter" %>
<%
    List<Pedido> listaPedidos = (List<Pedido>) request.getAttribute("listaPedidos");
    @SuppressWarnings("unchecked")
    Map<Integer, List<DetallePedido>> detallesPorPedido =
        (Map<Integer, List<DetallePedido>>) request.getAttribute("detallesPorPedido");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="../layout/head.jsp" />
<body class="bg-light">
<jsp:include page="../layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-4">Mis Pedidos</h2>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-hover table-bordered mb-0">
                <thead class="table-light">
                    <tr>
                        <th>#Pedido</th>
                        <th>Fecha</th>
                        <th>Estado</th>
                        <th class="text-end">Total</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <% if (listaPedidos == null || listaPedidos.isEmpty()) { %>
                <tr><td colspan="5" class="text-center text-muted py-4">No tienes pedidos registrados.</td></tr>
                <% } else {
                       for (Pedido p : listaPedidos) {
                           String badgeColor = "COMPLETADO".equals(p.getEstado()) ? "success" : "warning text-dark";
                           List<DetallePedido> detalles = detallesPorPedido != null
                               ? detallesPorPedido.get(p.getId()) : null;
                %>
                <tr>
                    <td>#<%= p.getId() %></td>
                    <td><%= p.getFecha() != null ? p.getFecha().format(fmt) : "-" %></td>
                    <td><span class="badge bg-<%= badgeColor %>"><%= p.getEstado() %></span></td>
                    <td class="text-end">S/ <%= String.format("%.2f", p.getTotal()) %></td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-outline-secondary"
                                data-bs-toggle="collapse"
                                data-bs-target="#detalle-<%= p.getId() %>"
                                aria-expanded="false">
                            <i class="bi bi-chevron-down"></i>
                        </button>
                    </td>
                </tr>
                <tr class="collapse" id="detalle-<%= p.getId() %>">
                    <td colspan="5" class="p-0 border-0">
                        <table class="table table-sm mb-0 bg-white">
                            <thead class="table-secondary">
                                <tr>
                                    <th class="ps-4">Producto</th>
                                    <th class="text-center">Cantidad</th>
                                    <th class="text-end">Precio unit.</th>
                                    <th class="text-end pe-4">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                            <% if (detalles == null || detalles.isEmpty()) { %>
                            <tr><td colspan="4" class="text-center text-muted ps-4">Sin detalle disponible.</td></tr>
                            <% } else {
                                   for (DetallePedido d : detalles) { %>
                            <tr>
                                <td class="ps-4"><%= d.getProductoNombre() %></td>
                                <td class="text-center"><%= d.getCantidad() %></td>
                                <td class="text-end">S/ <%= String.format("%.2f", d.getPrecioUnitario()) %></td>
                                <td class="text-end pe-4">S/ <%= String.format("%.2f", d.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(d.getCantidad()))) %></td>
                            </tr>
                            <%     }
                               } %>
                            </tbody>
                        </table>
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
