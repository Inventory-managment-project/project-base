# Instrucciones para trabajar con el frontend en modo de desarrollo (Se refresca automáticamente al modificar cualquier archivo)

## 1. Instalar NVM (Node Version Manager)

Instala NVM ejecutando:

```bash
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.2/install.sh | bash
```
> **Nota:** Es posible que necesites cerrar y volver a abrir tu terminal

## 2. Instalar la última versión LTS de Node.js

Con NVM instalado, ejecuta:

```bash
nvm install --lts
```

## 3. Instalar el manejador de dependencias YARN


```bash
npm install -g yarn
```

## 4. Instalar dependencias del frontend

Navega a la carpeta de frontend y ejecuta:

```bash
yarn install
```

Esto descargará todas las dependencias necesarias definidas en `package.json`.

## 5. Ejecutar el servidor de desarrollo

Para iniciar el servidor de desarrollo local, usa:

```bash
yarn dev
```

## 6. Cambiar la Ip del middleware

La validación de token no funcionará con la misma IP si se corre en el docker o si se corre local, entonces, en el archivo middleware.ts al comienzo se encuentra API_AUTH_URL, comentar o descomentar según corresponda. Para evitar problemas en presentaciones, evitar no commitear cambios de este archivo.

```javascript
//production - docker
//const API_AUTH_URL = "http://172.18.0.4:8080/validate";

//dev 
const API_AUTH_URL = "http://localhost:8080/validate";
```
> Para desarrollo en local.

```javascript
//production - docker
const API_AUTH_URL = "http://172.18.0.4:8080/validate";

//dev 
//const API_AUTH_URL = "http://localhost:8080/validate";
```
> Para utilizar en el docker.

> [!CAUTION]
> No se deben subir cambios de este archivo, sólo modificar para uso local.


## 7. Ejecutar el servidor de desarrollo

Inicializar el Backend y la base de datos

```bash
docker compose up api db (-d -> para que se ejecute en segundo plano)
```

