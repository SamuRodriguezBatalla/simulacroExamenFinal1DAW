CREATE DATABASE IF NOT EXISTS burger_db;
USE burger_db;

CREATE TABLE clientes (
                          dni VARCHAR(15) PRIMARY KEY,
                          nombre VARCHAR(50),
                          email VARCHAR(100)
);

CREATE TABLE productos (
                           id_producto INT PRIMARY KEY,
                           nombre VARCHAR(100),
                           precio DOUBLE
);

CREATE TABLE pedidos (
                         id_pedido INT PRIMARY KEY,
                         dni_cliente VARCHAR(15),
                         fecha VARCHAR(20),
                         total DOUBLE,
                         FOREIGN KEY (dni_cliente) REFERENCES clientes(dni)
);

CREATE TABLE lineas_pedido (
                               id_linea INT AUTO_INCREMENT PRIMARY KEY,
                               id_pedido INT,
                               id_producto INT,
                               cantidad INT,
                               FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido),
                               FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);