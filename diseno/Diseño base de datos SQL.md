# Usuarios Root - Administradores de Tienda
```SQL
CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contrasenaSaltada VARCHAR(255) NOT NULL,
    sal VARCHAR(10) NOT NULL,
    creadoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tienda (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuarioId INT NOT NULL, 
    nombre VARCHAR(255) NOT NULL,
    dominio VARCHAR(255) UNIQUE, 
    nombreBaseDatos VARCHAR(100) UNIQUE NOT NULL, 
    creadoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuarioId) REFERENCES usuario(id) ON DELETE CASCADE
);
```

# Estructura de Tienda
## Productos
``` SQL
CREATE TABLE producto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigoBarras VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    categoriaId INT,
    proveedorId INT,
    creadoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoriaId) REFERENCES categoria(id),
    FOREIGN KEY (proveedorId) REFERENCES proveedor(id)
);

CREATE TABLE inventario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    productoId INT NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    stockMinimo INT DEFAULT 0,
    precio DECIMAL(10,2) NOT NULL,
    precioMayoreo DECIMAL(10,2),
    precioDescuento DECIMAL(10,2),
    actualizadoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (productoId) REFERENCES producto(id) ON DELETE CASCADE
);
```

## Categorías
``` SQL
CREATE TABLE categoria (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT
);
```

## Proveedores
``` SQL
CREATE TABLE proveedor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    contacto VARCHAR(255),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion TEXT
);

```
## Clientes
``` SQL
CREATE TABLE cliente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100) UNIQUE,
    direccion TEXT,
    registradoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Ventas
``` SQL
CREATE TABLE venta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    clienteId INT NULL,
    total DECIMAL(10,2) NOT NULL,
    metodoPago ENUM('Efectivo', 'Tarjeta', 'Transferencia') NOT NULL,
    creadoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (clienteId) REFERENCES cliente(id)
);
```

## Detalles de Venta
``` SQL
CREATE TABLE detalleVenta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ventaId INT NOT NULL,
    productoId INT NOT NULL,
    cantidad INT NOT NULL,
    precioUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (ventaId) REFERENCES venta(id),
    FOREIGN KEY (productoId) REFERENCES producto(id)
);
```

## Compras
 ``` SQL
CREATE TABLE compra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    proveedorId INT NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    creadoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proveedorId) REFERENCES proveedor(id)
);
```

## Detalles de Compra
``` SQL
CREATE TABLE detalleCompra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    compraId INT NOT NULL,
    productoId INT NOT NULL,
    cantidad INT NOT NULL,
    costoUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (compraId) REFERENCES compra(id),
    FOREIGN KEY (productoId) REFERENCES producto(id)
);
```

## Usuarios Tienda 
``` SQL
CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    usuario VARCHAR(100) UNIQUE NOT NULL,
    contrasenaHash VARCHAR(255) NOT NULL
);
```

## Permisos
``` SQL
CREATE TABLE usuario_permiso (
    usuarioId INT NOT NULL,
    permisoId INT NOT NULL,
    PRIMARY KEY (usuarioId, permisoId),
    FOREIGN KEY (usuarioId) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (permisoId) REFERENCES permiso(id) ON DELETE CASCADE
);

CREATE TABLE permiso (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT
);
```

## Puntos de Venta
```sql
CREATE TABLE cajaRegistradora (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    usuarioId INT NOT NULL,
    estado ENUM('Abierta', 'Cerrada') NOT NULL DEFAULT 'Abierta',
    fechaApertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fechaCierre TIMESTAMP NULL,
    saldoInicial DECIMAL(10,2) NOT NULL,
    saldoFinal DECIMAL(10,2) NULL,
    FOREIGN KEY (usuarioId) REFERENCES usuario(id)
);
```

## Movimientos de Caja
```sql
CREATE TABLE movimientoCaja (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cajaRegistradoraId INT NOT NULL,
    tipoMovimiento ENUM('Entrada', 'Salida') NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    razon TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cajaRegistradoraId) REFERENCES cajaRegistradora(id)
);
```

## Cancelaciones de Ventas
```sql
CREATE TABLE cancelacionVenta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ventaId INT NOT NULL,
    usuarioId INT NOT NULL,
    razon TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ventaId) REFERENCES venta(id),
    FOREIGN KEY (usuarioId) REFERENCES usuario(id)
);
```

## Cancelaciones de Productos en Venta
```sql
CREATE TABLE cancelacionProducto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ventaId INT NOT NULL,
    productoId INT NOT NULL,
    cantidad INT NOT NULL,
    razon TEXT NOT NULL,
    usuarioId INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ventaId) REFERENCES venta(id),
    FOREIGN KEY (productoId) REFERENCES producto(id),
    FOREIGN KEY (usuarioId) REFERENCES usuario(id)
);
```

## Cierre de Turno de Punto de Venta
```sql
CREATE TABLE cierreCaja (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cajaRegistradoraId INT NOT NULL,
    usuarioId INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    totalVentas DECIMAL(10,2) NOT NULL,
    totalCancelaciones DECIMAL(10,2) NOT NULL,
    saldoEsperado DECIMAL(10,2) NOT NULL,
    saldoReal DECIMAL(10,2) NOT NULL,
    diferencia DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (cajaRegistradoraId) REFERENCES cajaRegistradora(id),
    FOREIGN KEY (usuarioId) REFERENCES usuario(id)
);
```

## Movimientos de Inventario
```SQL
CREATE TABLE movimientoInventario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    productoId INT NOT NULL,
    cantidad INT NOT NULL,
    tipo ENUM('Entrada', 'Salida') NOT NULL,
    razon TEXT NOT NULL,
    idRelacionado INT NULL,
    tablaRelacionada ENUM('compra', 'venta', 'ajusteManual') NOT NULL,
    usuarioId INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (productoId) REFERENCES producto(id),
    FOREIGN KEY (usuarioId) REFERENCES usuario(id)
);
```
## Notificaciones
```SQL
CREATE TABLE notificacion (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuarioId INT NOT NULL, 
    tipo ENUM('info', 'advertencia', 'error', 'confirmacion') NOT NULL,  
    mensaje TEXT NOT NULL, 
    leida BOOLEAN DEFAULT FALSE, 
    creadoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    actualizadoEn TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuarioId) REFERENCES usuario(id) ON DELETE CASCADE
);
```

[Diagrama ER - Draw.io (editable)](https://drive.google.com/file/d/1mX4jET-YW6a_DZrPAKH9VwyrSIyabmEm/view?usp=sharing)