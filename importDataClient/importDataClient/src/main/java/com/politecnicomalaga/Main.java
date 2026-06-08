package com.politecnicomalaga;

import com.politecnicomalaga.model.*;
import com.google.gson.Gson;
import okhttp3.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatosImportacion datos = new DatosImportacion();

        System.out.println("Leyendo el archivo data.csv...");
        try{
            File archivo = new File("data.csv");
            Scanner lector = new Scanner(archivo);

            int idPedidoActual = -1;

            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();

                if (linea.isEmpty()) continue;
                String[] partes = linea.split("#");

                if (partes.length >= 10){
                    // Cliente
                    Cliente c = new Cliente();
                    c.setNombre(partes[0]);
                    c.setApellidos(partes[1]);
                    c.setDni(partes[2]);
                    c.setEmail(partes[3]);
                    c.setTelefono(partes[4]);
                    c.setDireccion(partes[5]);
                    datos.getClientes().add(c);

                    // Pedido
                    Pedido p = new Pedido();
                    p.setId_pedido(Integer.parseInt(partes[6]));
                    p.setDni_cliente(partes[2]);
                    p.setFecha_pedido(partes[7]);
                    p.setNum_lineas(Integer.parseInt(partes[8]));
                    p.setTotal_pedido(Double.parseDouble(partes[9]));
                    datos.getPedidos().add(p);

                    // Guardamos ID del pedido, pa ponerselo a los productos
                    idPedidoActual = p.getId_pedido();
                } else if (partes.length >= 5) {
                    // Producto
                    Producto prod = new Producto();
                    prod.setId_producto(Integer.parseInt(partes[1]));
                    prod.setDescripcion(partes[2]);
                    prod.setPrecio_unitario(Double.parseDouble(partes[4]));
                    datos.getProductos().add(prod);

                    // Linea pedido
                    LineaPedido lp = new LineaPedido();
                    lp.setId_pedido(idPedidoActual);
                    lp.setId_producto(prod.getId_producto());
                    lp.setCantidad(Integer.parseInt(partes[3]));
                    lp.setPrecio_unitario(prod.getPrecio_unitario());
                    datos.getLineas_pedido().add(lp);
                }
            }
            lector.close();
            System.out.println("Lectura completada. Enviando al servidor...");
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encuentra el archivo: "+e.getMessage());
            return;
        }
        // Convertir a JSON con GSON
        Gson gson = new Gson();
        String json = gson.toJson(datos);

        // Enviar al servidor
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("http://localhost:8888/importar")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("RESPUESTA DEL SERVIDOR: "+response.body().string());
        } catch (IOException e) {
            System.out.println("ERROR DE CONEXIÓN: "+e.getMessage());
        }

    }
}