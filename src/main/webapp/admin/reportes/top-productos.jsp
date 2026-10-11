<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.petshop.model.Producto" %>
<%
    List<Producto> listaTop = (List<Producto>) request.getAttribute("listaTop");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="/layout/head.jsp" />
<body class="bg-light">
<jsp:include page="/layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-4">Reporte: Productos m&aacute;s vendidos</h2>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-hover table-bordered mb-0">
                <thead class="table-light">
                    <tr>
                        <th>#</th>
                        <th>Producto</th>
                        <th>Categor&iacute;a</th>
                        <th class="text-end">Precio</th>
                        <th class="text-center">Unidades vendidas</th>
                    </tr>
                </thead>
                <tbody>
                <% if (listaTop == null || listaTop.isEmpty()) { %>
                <tr><td colspan="5" class="text-center text-muted py-4">No hay datos de ventas aún.</td></tr>
                <% } else {
                       int pos = 1;
                       for (Producto p : listaTop) { %>
                <tr>
                    <td class="text-muted"><%= pos++ %></td>
                    <td class="fw-semibold"><%= p.getNombre() %></td>
                    <td><%= p.getCategoriaNombre() %></td>
                    <td class="text-end">S/ <%= String.format("%.2f", p.getPrecio()) %></td>
                    <td class="text-center">
                        <span class="badge rounded-pill" style="background-color:#F4A261;color:#212529;">
                            <%= p.getTotalVendido() %>
                        </span>
                    </td>
                </tr>
                <%     }
                   } %>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-3">
        <a href="${pageContext.request.contextPath}/admin/panel" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left me-1"></i>Volver al panel
        </a>
    </div>

</main>
<jsp:include page="/layout/footer.jsp" />
</body>
</html>
