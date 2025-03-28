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

## 4. Ejecutar el servidor de desarrollo

Para iniciar el servidor de desarrollo local, usa:

```bash
yarn dev
```
