package com.politecnicomalaga.tienda.controller;

import com.google.gson.Gson;
import com.politecnicomalaga.tienda.dataservice.BBDDAccess;
import com.politecnicomalaga.tienda.model.DatosImportacion;

import java.util.*;


public class Controlador implements DataAccess{

    private BBDDAccess miBBDD;

    public Controlador() {
        miBBDD = new BBDDAccess();
    }

    @Override
    public String listAllProductos(){
        try{
            List<Map<String,Object>> lista = miBBDD.listarTodosProductos();
            return new Gson().toJson(lista);
        } catch (Exception e){
            return "{\"error\": \"List Products: " + e.getMessage() + "\"}";
        }
    }

    @Override
    public String findProductoXCodigo(String code) {
        try {
            Map<String, Object> prod = miBBDD.buscarProductoXCodigo(code);
            if (prod == null) return "{\"error\": \"Producto no encontrado\"}";
            return new Gson().toJson(prod);
        } catch (Exception e) {
            return "{\"error\": \"Find Product: " + e.getMessage() + "\"}";
        }
    }

    @Override
    public String findClienteXDNI(String dni){
        try {
            List<Map<String, Object>> lista = miBBDD.buscarClienteXDNI(dni);
            if (lista.isEmpty()) return "{\"error\": \"Cliente no encontrado\"}";
            return new Gson().toJson(lista);
        } catch (Exception e){
            return "{\"error\": \"Find Client: " + e.getMessage() + "\"}";
        }
    }

    @Override
    public String listProductosXPedido(String dni, String pedido){
        try {
            List<Map<String, Object>> lista = miBBDD.listarProductosXPedido(dni, pedido);
            return new Gson().toJson(lista);
        } catch (Exception e) {
            return "{\"error\": \"Find Pedido: " + e.getMessage() + "\"}";
        }
    }

    @Override
    public String importData(String jsonDataFromCSV){
        try{
            DatosImportacion datos = new Gson().fromJson(jsonDataFromCSV, DatosImportacion.class);
            (new BBDDAccess()).procesarImportacion(datos);

            return "{\"resultado\": \"ok\"}";

        } catch (Exception e) {
            return "{\"resultado\": \"error\"}";

        }
    }

    @Override
    public String listAllClientes(){
        try{
            List<Map<String, Object>> lista = miBBDD.listarTodosClientes();
            return new Gson().toJson(lista);
        } catch (Exception e){
            return "{\\\"error\\\": \\\"List Clients: \" + e.getMessage() + \"\\\"}";
        }
    }


}