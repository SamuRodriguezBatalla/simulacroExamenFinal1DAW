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
            File archivo = new File("burguer_data.csv");
            Scanner lector = new Scanner(archivo);

            int idPedidoActual = -1;

            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();

                if (linea.isEmpty()) continue;
                String[] partes = linea.split("#");

                if (partes[0].equals("")){
                    Producto p = new Producto();
                    p.setId_producto(Integer.parseInt(partes[1]));
                    p.setNombre(partes[2]);
                    p.setPrecio(Double.parseDouble(partes[3]));
                    datos.getProductos().add(p);
                } else if (partes.length == 6) {
                    Cliente c = new Cliente();
                    c.setDni(partes[0]);
                    c.setNombre(partes[1]);
                    c.setEmail(partes[2]);
                    datos.getClientes().add(c);

                    Pedido pedido = new Pedido();
                    pedido.setDni_cliente(partes[0]);
                    pedido.setId_pedido(Integer.parseInt(partes[3]));
                    pedido.setFecha(partes[4]);
                    pedido.setTotal(Double.parseDouble(partes[5]));
                    datos.getPedidos().add(pedido);
                } else if (partes.length == 4){
                    LineaPedido lp = new LineaPedido();
                    lp.setId_pedido(Integer.parseInt(partes[1]));
                    lp.setId_producto(Integer.parseInt(partes[2]));
                    lp.setCantidad(Integer.parseInt(partes[3]));
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