package com.politecnicomalaga.model;

public class Pedido {
    private int id_pedido;
    private String dni_cliente;
    private String fecha_pedido;
    private int num_lineas;
    private double total_pedido;

    public int getId_pedido() {
        return id_pedido;
    }

    public String getDni_cliente() {
        return dni_cliente;
    }

    public String getFecha_pedido() {
        return fecha_pedido;
    }

    public int getNum_lineas() {
        return num_lineas;
    }

    public double getTotal_pedido() {
        return total_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public void setDni_cliente(String dni_cliente) {
        this.dni_cliente = dni_cliente;
    }

    public void setFecha_pedido(String fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public void setNum_lineas(int num_lineas) {
        this.num_lineas = num_lineas;
    }

    public void setTotal_pedido(double total_pedido) {
        this.total_pedido = total_pedido;
    }
}
