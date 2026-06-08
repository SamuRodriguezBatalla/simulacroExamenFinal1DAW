package com.politecnicomalaga.tienda.model;

public class Pedido {
    private int id_pedido;
    private String dni_cliente;
    private String fecha;
    private double total;

    public int getId_pedido() {
        return id_pedido;
    }

    public String getDni_cliente() {
        return dni_cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public void setDni_cliente(String dni_cliente) {
        this.dni_cliente = dni_cliente;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}