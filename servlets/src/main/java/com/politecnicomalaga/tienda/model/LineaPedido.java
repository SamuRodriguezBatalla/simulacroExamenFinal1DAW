package com.politecnicomalaga.tienda.model;
public class LineaPedido {
    private int id_pedido;
    private int id_producto;
    private int cantidad;

    public int getId_pedido() {
        return id_pedido;
    }

    public int getId_producto() {
        return id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
