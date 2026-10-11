<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.petshop.model.Usuario" %>
<%
    Usuario admin = (Usuario) session.getAttribute("usuario");
%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="../layout/head.jsp" />
<body class="bg-light">
<jsp:include page="../layout/navBar.jsp" />
<main class="container py-4">

    <h2 class="fw-bold mb-1">Panel de Administración</h2>
    <p class="text-muted mb-4">Bienvenido, <%= admin != null ? admin.getNombre() : "" %></p>

    <div class="row g-4">
        <div class="col-md-4">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-tags fs-1" style="color:#2C7A7B;"></i>
                    <h5 class="card-title mt-2">Categorías</h5>
                    <p class="card-text text-muted small">Gestionar categorías de productos.</p>
                    <a href="${pageContext.request.contextPath}/admin/categorias?accion=listar" class="btn btn-primary">Ir al módulo</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-box-seam fs-1" style="color:#2C7A7B;"></i>
                    <h5 class="card-title mt-2">Productos</h5>
                    <p class="card-text text-muted small">Gestionar catálogo y stock.</p>
                    <a href="${pageContext.request.contextPath}/admin/productos?accion=listar" class="btn btn-primary">Ir al módulo</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-people fs-1" style="color:#2C7A7B;"></i>
                    <h5 class="card-title mt-2">Usuarios</h5>
                    <p class="card-text text-muted small">Gestionar cuentas y roles.</p>
                    <a href="${pageContext.request.contextPath}/admin/usuarios?accion=listar" class="btn btn-primary">Ir al módulo</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-calendar-range fs-1" style="color:#2C7A7B;"></i>
                    <h5 class="card-title mt-2">Ventas por fecha</h5>
                    <p class="card-text text-muted small">Reporte de pedidos por rango de fechas.</p>
                    <a href="${pageContext.request.contextPath}/admin/reportes/ventas" class="btn btn-primary">Ver reporte</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-bar-chart-line fs-1" style="color:#2C7A7B;"></i>
                    <h5 class="card-title mt-2">Productos m&aacute;s vendidos</h5>
                    <p class="card-text text-muted small">Ranking de productos por unidades vendidas.</p>
                    <a href="${pageContext.request.contextPath}/admin/reportes/top-productos" class="btn btn-primary">Ver reporte</a>
                </div>
            </div>
        </div>
    </div>

</main>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>
