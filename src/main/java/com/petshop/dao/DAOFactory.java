package com.petshop.dao;

public class DAOFactory {

    public static UsuarioDAO   getUsuarioDAO()   { return new UsuarioDAO(); }
    public static CategoriaDAO getCategoriaDAO() { return new CategoriaDAO(); }
    public static ProductoDAO  getProductoDAO()  { return new ProductoDAO(); }
    public static PedidoDAO    getPedidoDAO()    { return new PedidoDAO(); }
}
