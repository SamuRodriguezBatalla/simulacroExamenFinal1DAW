package com.politecnicomalaga.tienda.dataservice;

import com.politecnicomalaga.tienda.model.*;

import java.sql.*;
import java.util.*;

public class BBDDAccess {

    // 1 Listar todos los productos
    public List<Map<String, Object>> listarTodosProductos() throws SQLException, ClassNotFoundException {
        Connection conn = ConexionBD.getConnection();
        List<Map<String,Object>> lista = new ArrayList<>();

        String sql = "SELECT id_producto, nombre, precio FROM productos";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Map<String, Object> prod = new HashMap<>();
            prod.put("id_producto", rs.getInt("id_producto"));
            prod.put("nombre", rs.getString("nombre"));
            prod.put("precio", rs.getDouble("precio"));
            lista.add(prod);
        }
        rs.close();
        stmt.close();
        conn.close();
        return lista;
    }

    // 2 Buscar producto por Codigo
    public Map<String, Object> buscarProductoXCodigo(String codigo) throws SQLException, ClassNotFoundException{
        Connection conn = ConexionBD.getConnection();
        Map<String, Object> prod = null;
        String sql = "SELECT id_producto, nombre, precio from productos WHERE id_producto = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,Integer.parseInt(codigo));
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            prod = new HashMap<>();
            prod.put("id_producto", rs.getInt("id_producto"));
            prod.put("nombre", rs.getString("nombre"));
            prod.put("precio", rs.getDouble("precio"));
        }

        rs.close(); pstmt.close(); conn.close();
        return prod;
    }

    // 3 Buscar cliente por DNI
    public List<Map<String, Object>> buscarClienteXDNI(String dni) throws SQLException, ClassNotFoundException{
        Connection conn = ConexionBD.getConnection();
        List<Map<String,Object>> lista = new ArrayList<>();

        String sql = "SELECT dni, nombre, email from clientes where dni LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,"%"+ dni+"%");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Map<String, Object> cliente = new HashMap<>();
            cliente.put("dni", rs.getString("dni"));
            cliente.put("nombre", rs.getString("nombre"));
            cliente.put("email", rs.getString("email"));
            lista.add(cliente);
        }
        rs.close(); pstmt.close(); conn.close();
        return lista;
    }

    // 4 Buscar producto de un pedido concreto de un cliente
    public List<Map<String,Object>> listarProductosXPedido(String dni, String pedido) throws SQLException, ClassNotFoundException{
        Connection conn = ConexionBD.getConnection();
        List<Map<String,Object>> lista = new ArrayList<>();
        // Unimos pedidos, lineas_pedido y productos
        String sql = "SELECT pr.id_producto, pr.nombre, lp.cantidad " +
                "FROM productos pr " +
                "JOIN lineas_pedido lp ON pr.id_producto = lp.id_producto " +
                "JOIN pedidos pd ON lp.id_pedido = pd.id_pedido " +
                "WHERE pd.dni_cliente = ? AND pd.id_pedido = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, dni);
        pstmt.setInt(2,Integer.parseInt(pedido));
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()){
            Map<String,Object> item = new HashMap<>();
            item.put("id_producto", rs.getInt("id_producto"));
            item.put("nombre", rs.getString("nombre"));
            item.put("cantidad", rs.getInt("cantidad"));
            lista.add(item);
        }

        rs.close(); pstmt.close(); conn.close();
        return lista;
    }

    public void procesarImportacion(DatosImportacion datos) throws Exception{
        Connection conn = ConexionBD.getConnection();
        conn.setAutoCommit(false);

        try {
            // 1 Importar Clientes
            if (datos.getClientes() != null){
                String sql = "insert ignore into clientes (dni, nombre, email) values (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                for (Cliente c: datos.getClientes()){
                    pstmt.setString(1, c.getDni());
                    pstmt.setString(2, c.getNombre());
                    pstmt.setString(3,c.getEmail());
                    pstmt.executeUpdate();
                }
                pstmt.close();
            }

            // 2 Importar Productos
            if (datos.getProductos() != null){
                String sql = "INSERT IGNORE INTO productos (id_producto, nombre, precio) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                for (Producto p : datos.getProductos()) {
                    pstmt.setInt(1, p.getId_producto());
                    pstmt.setString(2, p.getNombre());
                    pstmt.setDouble(3, p.getPrecio());
                    pstmt.executeUpdate();
                }
                pstmt.close();
            }

            // 3. Importar pedidos
            // (Debe ir después de Clientes porque la BBDD exige que el DNI ya exista)
            if (datos.getPedidos() != null) {
                String sql = "INSERT IGNORE INTO pedidos (id_pedido, dni_cliente, fecha, total) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                for (Pedido p : datos.getPedidos()) {
                    pstmt.setInt(1, p.getId_pedido());
                    pstmt.setString(2, p.getDni_cliente());
                    pstmt.setString(3, p.getFecha());
                    pstmt.setDouble(4,p.getTotal());
                    pstmt.executeUpdate();
                }
                pstmt.close();
            }

            // 4 Importar lineas del pedido
            // (Debe ir al final porque exige que el Producto y el Pedido ya existan)
            if (datos.getLineas_pedido() != null) {
                // No insertamos id_linea ni subtotal porque son campos autogenerados en la BBDD
                String sql = "INSERT IGNORE INTO lineas_pedido (id_pedido, id_producto, cantidad) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                for (LineaPedido l : datos.getLineas_pedido()) {
                    pstmt.setInt(1, l.getId_pedido());
                    pstmt.setInt(2, l.getId_producto());
                    pstmt.setInt(3, l.getCantidad());
                    pstmt.executeUpdate();
                }
                pstmt.close();
            }

            // Si llegamos hasta aqui sin que ningun PreparedStatement explote, confirmamos todos los cambios
            conn.commit();

        } catch (Exception e) {
            // Si salta un error en cualquier tabla, damos marcha atra para dejar la BBDD intacta
            conn.rollback();
            throw new Exception("Error procesando la importación: " + e.getMessage());
        } finally {
            // Siempre devolvemos la conexion a su estado original antes de cerrarla
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public List<Map<String, Object>> listarTodosClientes() throws SQLException, ClassNotFoundException{
        Connection conn = ConexionBD.getConnection();
        List<Map<String, Object>> lista = new ArrayList<>();

        String sql = "SELECT dni, nombre, email FROM clientes";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()){
            Map<String, Object> cliente = new HashMap<>();
            cliente.put("dni", rs.getString("dni"));
            cliente.put("nombre",rs.getString("nombre"));
            cliente.put("email", rs.getString("email"));
            lista.add(cliente);
        }
        rs.close();
        stmt.close();
        conn.close();
        return lista;
    }
}