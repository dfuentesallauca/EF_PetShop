<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map, com.petshop.model.Pedido, com.petshop.model.DetallePedido, java.time.format.DateTimeFormatter" %>
<%
    List<Pedido> listaPedidos = (List<Pedido>) request.getAttribute("listaPedidos");
    @SuppressWarnings("unchecked")
    Map<Integer, List<DetallePedido>> detallesPorPedido =
        (Map<Integer, List<DetallePedido>>) request.getAttribute("detallesPorPedido");
    String  desde  = (String) request.getAttribute("desde");
    String  hasta  = (String) request.getAttribute("hasta");
    boolean buscado = (listaPedidos != null);
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="/layout/head.jsp" />
<body class="bg-light">
<jsp:include page="/layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-4">Reporte: Ventas por rango de fechas</h2>

    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/admin/reportes/ventas"
                  class="row g-3 align-items-end">
                <div class="col-md-4">
                    <label for="desde" class="form-label">Desde</label>
                    <input type="date" id="desde" name="desde" class="form-control"
                           value="<%= desde != null ? desde : "" %>" required>
                </div>
                <div class="col-md-4">
                    <label for="hasta" class="form-label">Hasta</label>
                    <input type="date" id="hasta" name="hasta" class="form-control"
                           value="<%= hasta != null ? hasta : "" %>" required>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100">Consultar</button>
                </div>
            </form>
        </div>
    </div>

    <% if (buscado) { %>
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-hover table-bordered mb-0">
                <thead class="table-light">
                    <tr>
                        <th>#Pedido</th>
                        <th>Cliente</th>
                        <th>Fecha</th>
                        <th>Estado</th>
                        <th class="text-end">Total</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <% if (listaPedidos.isEmpty()) { %>
                <tr><td colspan="6" class="text-center text-muted py-4">No se encontraron ventas en ese período.</td></tr>
                <% } else {
                       for (Pedido p : listaPedidos) {
                           String badgeColor = "COMPLETADO".equals(p.getEstado()) ? "success" : "warning text-dark";
                           List<DetallePedido> detalles = detallesPorPedido != null
                               ? detallesPorPedido.get(p.getId()) : null;
                %>
                <tr>
                    <td>#<%= p.getId() %></td>
                    <td><%= p.getClienteNombre() %></td>
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
                    <td colspan="6" class="p-0 border-0">
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
    <% } %>

    <div class="mt-3">
        <a href="${pageContext.request.contextPath}/admin/panel" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left me-1"></i>Volver al panel
        </a>
    </div>

</main>
<jsp:include page="/layout/footer.jsp" />
</body>
</html>
