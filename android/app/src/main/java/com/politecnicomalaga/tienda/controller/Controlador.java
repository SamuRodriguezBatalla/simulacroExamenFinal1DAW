package com.politecnicomalaga.tienda.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.politecnicomalaga.tienda.Reaccionable;
import com.politecnicomalaga.tienda.dataservice.BBDDAccess;
import com.politecnicomalaga.tienda.model.*;

import java.lang.reflect.Type;
import java.util.*;


public class Controlador {
    // instance variables
    private Reaccionable miPantalla;
    private static Controlador singleton;

    public static final int MODO_PRODUCTOS = 1;
    public static final int MODO_CLIENTES = 2;
    public static final int MODO_BUSCAR_CLIENTE = 3;
    private int modoActual;

    private List<Producto> listaProductos;
    private List<Cliente> listaClientes;
    private List<Cliente> listaClientesBusqueda;

    private Controlador(Reaccionable miPantalla) {
        this.miPantalla = miPantalla;
        this.listaProductos = new ArrayList<>();
        this.listaClientes = new ArrayList<>();
        this.listaClientesBusqueda = new ArrayList<>();
        this.modoActual = MODO_PRODUCTOS;
    }
    public static Controlador getSingleton(Reaccionable miPantalla) {
        // put your code here
        if (singleton == null){
            singleton = new Controlador(miPantalla);
        } else if (miPantalla != null) {
            singleton.setPantalla(miPantalla);
        }
        return singleton;
    }

    public void pedirProductos(){
        modoActual = MODO_PRODUCTOS;
        BBDDAccess miBBDD = new BBDDAccess(this);
        miBBDD.peticionGet("http://10.0.2.2:8888/listarProductos");
    }

    public void pedirClientes(){
        modoActual = MODO_CLIENTES;
        BBDDAccess miBBDD = new BBDDAccess(this);
        miBBDD.peticionGet("http://10.0.2.2:8888/listarClientes");
    }

    public void pedirClienteXDNI(String dni){
        modoActual = MODO_BUSCAR_CLIENTE;
        BBDDAccess miBBDD = new BBDDAccess(this);
        miBBDD.peticionGet("http://10.0.2.2:8888/buscarCliente?dni="+dni);
    }


    public List<String> getDatosPantalla() {

        List<String> resultado = new ArrayList<>();
        if (modoActual == MODO_PRODUCTOS){
            for (Producto p: listaProductos){
                resultado.add(p.getId_producto()+" - "+p.getNombre()+" - "+p.getPrecio()+"€");
            }
        } else if (modoActual == MODO_CLIENTES){
            for (Cliente c: listaClientes){
                resultado.add(c.getDni()+" - "+c.getNombre()+" - "+c.getEmail());
            }
        } else if (modoActual == MODO_BUSCAR_CLIENTE) {
            for (Cliente c : listaClientesBusqueda) {
                resultado.add(c.getDni()+" - "+c.getNombre()+ " - "+c.getEmail());
            }
        }
        return resultado;
    }

    //Este método es llamado por OKhttp cuando se produce la respuesta a la
    // petición de datos a nuestro backend
    public void setData(String jsonData, boolean error) {
        if (error){
            miPantalla.reaccionar("Error de conexión con Tomcat.");
            return;
        }
        try {
            Gson gson = new Gson();
            Type tipoLista;
            if (modoActual == MODO_PRODUCTOS) {
                tipoLista = new TypeToken<List<Producto>>(){}.getType();
                listaProductos = gson.fromJson(jsonData, tipoLista);
            } else if (modoActual == MODO_CLIENTES) {
                tipoLista = new TypeToken<List<Cliente>>(){}.getType();
                listaClientes = gson.fromJson(jsonData, tipoLista);
            } else if (modoActual == MODO_BUSCAR_CLIENTE) {
                if (jsonData.contains("\"error\"")){
                    miPantalla.reaccionar("No se han encontrado resultados...");
                    return;
                } else {
                    tipoLista = new TypeToken<List<Cliente>>(){}.getType();
                    listaClientesBusqueda = gson.fromJson(jsonData, tipoLista);
                }
            }
            // Decimos al MainActivity que ya puede pintar la pantalla
            miPantalla.reaccionar("");
        } catch (JsonSyntaxException e) {
            miPantalla.reaccionar("Error al parsear el JSON.");
        }
    }

    public void setPantalla(Reaccionable pantalla){
        this.miPantalla = pantalla;
    }

}