package com.petshop.model;

import java.math.BigDecimal;

public class Producto {

    private int id;
    private int idCategoria;
    private String nombre;
    private BigDecimal precio;
    private int stock;
    private String imagenUrl;
    private boolean activo;

    // campos de apoyo para vistas y reportes (no mapeados a columna)
    private String categoriaNombre;
    private int totalVendido;

    public Producto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    public int getTotalVendido() { return totalVendido; }
    public void setTotalVendido(int totalVendido) { this.totalVendido = totalVendido; }
}
