SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;

-- =====================================================
-- Base de datos tienda informática
-- Script MySQL 8.x
-- =====================================================

DROP DATABASE IF EXISTS tienda;
CREATE DATABASE tienda
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE tienda;

-- =====================================================
-- TABLA CLIENTES
-- =====================================================
CREATE TABLE clientes (
    dni               VARCHAR(20) PRIMARY KEY,
    nombre            VARCHAR(80) NOT NULL,
    apellidos         VARCHAR(120) NOT NULL,
    email             VARCHAR(150) NOT NULL,
    telefono          VARCHAR(30),
    direccion         VARCHAR(255)
);

-- =====================================================
-- TABLA PRODUCTOS
-- Catálogo general de productos
-- El código viene del CSV (10001, 40002, etc.)
-- =====================================================
CREATE TABLE productos (
    id_producto       INT PRIMARY KEY,
    descripcion       VARCHAR(200) NOT NULL,
    precio_unitario   DECIMAL(10,2) NOT NULL
);

-- =====================================================
-- TABLA PEDIDOS
-- =====================================================
CREATE TABLE pedidos (
    id_pedido         INT PRIMARY KEY,
    dni_cliente        VARCHAR(20) NOT NULL,
    fecha_pedido      DATE NOT NULL,
    num_lineas        INT NOT NULL,
    total_pedido      DECIMAL(12,2) NOT NULL,
    
    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (dni_cliente)
        REFERENCES clientes(dni)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =====================================================
-- TABLA LINEAS_PEDIDO
-- Relación pedido-producto
-- =====================================================
CREATE TABLE lineas_pedido (
    id_linea          INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido         INT NOT NULL,
    id_producto       INT NOT NULL,
    cantidad          INT NOT NULL,
    precio_unitario   DECIMAL(10,2) NOT NULL,
    subtotal          DECIMAL(12,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,

    CONSTRAINT fk_linea_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedidos(id_pedido)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_linea_producto
        FOREIGN KEY (id_producto)
        REFERENCES productos(id_producto)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- =====================================================
-- FIN SCRIPT
-- =====================================================

COMMIT;
