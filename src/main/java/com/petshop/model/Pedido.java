package com.petshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido {

    private int id;
    private int idUsuario;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;

    // campo de apoyo para reportes (no mapeado a columna)
    private String clienteNombre;

    public Pedido() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
}
