# DFC | Manejador de Inventarios

Sistema de Gestión para Tiendas – POS e Inventario  Este repositorio contiene una solución integral para la administración de ventas e inventarios en tiendas como supermercados, abarrotes y minimercados. El sistema centraliza las operaciones de punto de venta, control de stock y generación de reportes en una plataforma intuitiva y escalable.

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/Inventory-managment-project/project-base.git
cd project-base
```

### 2. Inicializar el contenedor del Backend + Postgres (Base de datos)

```bash
docker compose up -d
```

### 3. Inicializar el contenedor del Frontend

```bash
cd frontend
docker compose up -d
```

### 4. Acceder a la página [principal](http://localhost:3000/)

## Finalizar los contenedores

### 1. Para finalizar la ejecución del Backend + Postgres: Ir a la carpeta raíz

```bash
cd project-base
docker compose down
```

### 2. Finalizar la ejecución del frontend

```bash
cd frontend
docker compose down
```

### 3. Verificar que ya no se encuentren activos los contenedores

```bash
docker ps
```

### 4. De ser así, finalizar manualmente

```bash
docker stop <nombre_del_contenedor>
```


