-- Base de datos
CREATE DATABASE IF NOT EXISTS db_petshop
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;
USE db_petshop;

-- Tabla: USUARIO
CREATE TABLE USUARIO (
    id_usuario     INT          AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    rol            VARCHAR(10)  NOT NULL DEFAULT 'CLIENTE',
    activo         TINYINT(1)   NOT NULL DEFAULT 1,
    fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_usuario_rol CHECK (rol IN ('ADMIN', 'CLIENTE'))
);

-- Tabla: CATEGORIA
CREATE TABLE CATEGORIA (
    id_categoria INT         AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(80) NOT NULL UNIQUE,
    activo       TINYINT(1)  NOT NULL DEFAULT 1
);

-- Tabla: PRODUCTO
CREATE TABLE PRODUCTO (
    id_producto  INT           AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT           NOT NULL,
    nombre       VARCHAR(120)  NOT NULL,
    precio       DECIMAL(10,2) NOT NULL,
    stock        INT           NOT NULL DEFAULT 0,
    imagen_url   VARCHAR(255)  NULL,
    activo       TINYINT(1)    NOT NULL DEFAULT 1,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id_categoria),
    CONSTRAINT chk_producto_precio   CHECK (precio > 0),
    CONSTRAINT chk_producto_stock    CHECK (stock >= 0)
);

-- Tabla: PEDIDO
CREATE TABLE PEDIDO (
    id_pedido  INT           AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT           NOT NULL,
    fecha      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado     VARCHAR(15)   NOT NULL DEFAULT 'PENDIENTE',
    total      DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario),
    CONSTRAINT chk_pedido_estado CHECK (estado IN ('PENDIENTE', 'COMPLETADO'))
);

-- Tabla: DETALLE_PEDIDO
CREATE TABLE DETALLE_PEDIDO (
    id_detalle      INT           AUTO_INCREMENT PRIMARY KEY,
    id_pedido       INT           NOT NULL,
    id_producto     INT           NOT NULL,
    cantidad        INT           NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_pedido   FOREIGN KEY (id_pedido)   REFERENCES PEDIDO(id_pedido),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto),
    CONSTRAINT chk_detalle_cantidad CHECK (cantidad > 0)
);

-- Índices para consultas frecuentes
CREATE INDEX IX_PRODUCTO_categoria ON PRODUCTO(id_categoria);
CREATE INDEX IX_PEDIDO_usuario     ON PEDIDO(id_usuario);
CREATE INDEX IX_PEDIDO_fecha       ON PEDIDO(fecha);
CREATE INDEX IX_DETALLE_pedido     ON DETALLE_PEDIDO(id_pedido);

-- Seed: categorías
INSERT INTO CATEGORIA (nombre) VALUES
    ('Alimentos'),
    ('Accesorios'),
    ('Higiene'),
    ('Juguetes');

-- Seed: productos
INSERT INTO PRODUCTO (id_categoria, nombre, precio, stock) VALUES
    (1, 'Croquetas premium 5kg',     45.90, 20),
    (1, 'Alimento húmedo x12 latas', 38.50, 15),
    (2, 'Correa retráctil',          22.00, 30),
    (2, 'Cama mediana acolchada',    55.00, 10),
    (3, 'Shampoo antipulgas 500ml',  18.00, 25),
    (4, 'Pelota de goma',             8.50, 40);

-- Seed: usuario administrador inicial
-- Contraseña: admin123 (SHA-256 hex)
INSERT INTO USUARIO (nombre, email, password_hash, rol)
VALUES ('Admin', 'admin@petshop.com',
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN');
```
