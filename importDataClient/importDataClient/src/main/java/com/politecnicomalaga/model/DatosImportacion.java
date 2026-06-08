package com.politecnicomalaga.model;

import java.util.ArrayList;
import java.util.List;

public class DatosImportacion {
    private List<Cliente> clientes = new ArrayList<>();
    private List<Producto> productos = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();
    private List<LineaPedido> lineas_pedido = new ArrayList<>();

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public List<LineaPedido> getLineas_pedido() {
        return lineas_pedido;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void setLineas_pedido(List<LineaPedido> lineas_pedido) {
        this.lineas_pedido = lineas_pedido;
    }
}
