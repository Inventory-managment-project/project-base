# 🏪 Sistema de Analytics para Punto de Venta (POS)

## 📋 Descripción General

Este sistema de analytics está específicamente diseñado para un **punto de venta (POS)** y proporciona métricas esenciales para el negocio de retail, incluyendo análisis de ventas, inventario y datos en tiempo real.

## 🎯 Características Principales

### 📊 Analytics de Ventas
- **Ventas totales** y **ingresos totales** por período
- **Ticket promedio** de ventas
- **Análisis por método de pago** (Efectivo vs Tarjeta)
- **Productos más vendidos** con métricas detalladas
- **Ventas por hora** para identificar picos de actividad
- **Ventas por día** con tendencias históricas
- **Crecimiento de ventas** con porcentajes de cambio

### 📦 Analytics de Inventario
- **Stock total** y **valor del inventario**
- **Productos con stock bajo** con niveles de alerta
- **Productos agotados** (stock = 0)
- **Productos de mayor valor** en inventario
- **Movimiento de stock** con tasas de rotación
- **Alertas automáticas** por niveles de stock

### 🔴 Métricas en Tiempo Real
- **Ventas del día actual**
- **Ingresos del día**
- **Ventas de la hora actual**
- **Ticket promedio del día**
- **Producto estrella del día**
- **Ventas recientes** con detalles

## 🏗️ Arquitectura del Sistema

### Backend (Kotlin + Ktor)

#### Modelos de Datos
```kotlin
// Principales estructuras de datos
- SalesAnalytics: Métricas generales de ventas
- ProductSalesMetric: Métricas por producto
- InventoryAnalytics: Análisis de inventario
- RealtimeMetrics: Datos en tiempo real
- LowStockProduct: Productos con stock bajo
```

#### Endpoints de API
```
GET /analytics/sales - Métricas de ventas
GET /analytics/inventory - Análisis de inventario
GET /analytics/realtime - Datos en tiempo real
GET /analytics/low-stock - Productos con stock bajo
GET /analytics/top-products - Productos más vendidos
GET /analytics/sales-by-hour - Ventas por hora
GET /analytics/sales-by-day - Ventas por día
GET /analytics/sales-growth - Crecimiento de ventas
GET /analytics/dashboard - Dashboard completo
```

#### Filtros Disponibles
- `startDate` / `endDate`: Rango de fechas
- `storeId`: Filtrar por tienda específica
- `paymentMethod`: CASH o CARD
- `productId`: Producto específico
- `limit`: Límite de resultados

### Frontend (Next.js + TypeScript)

#### Componentes Principales
- **Analytics.tsx**: Dashboard principal
- **Tabs de navegación**: Ventas, Inventario, Tiempo Real
- **Métricas en tiempo real**: Actualización cada 30 segundos
- **Tablas interactivas**: Productos, ventas, stock
- **Filtros de período**: 1d, 7d, 30d, 90d

## 📈 Métricas Específicas del Negocio

### 💰 Ventas
1. **Total de Ventas**: Número de transacciones
2. **Ingresos Totales**: Suma de todas las ventas
3. **Ticket Promedio**: Ingreso promedio por transacción
4. **Distribución por Método de Pago**: Efectivo vs Tarjeta
5. **Productos Más Vendidos**: Top 5 con cantidad y ingresos
6. **Patrones Horarios**: Identificar horas pico
7. **Tendencias Diarias**: Análisis de crecimiento

### 📦 Inventario
1. **Valor Total del Inventario**: Suma del valor de todos los productos
2. **Productos con Stock Bajo**: Alertas automáticas
3. **Niveles de Stock**:
   - 🔴 **CRÍTICO**: Stock ≤ 0
   - 🟡 **BAJO**: Stock ≤ mínimo permitido
   - 🟠 **ADVERTENCIA**: Stock ≤ mínimo × 1.5
   - 🟢 **NORMAL**: Stock > mínimo × 1.5
4. **Rotación de Inventario**: Tasa de movimiento de productos
5. **Productos de Alto Valor**: Mayor impacto económico

### 🔴 Tiempo Real
1. **Ventas del Día**: Contador en vivo
2. **Ingresos Actuales**: Suma del día
3. **Actividad por Hora**: Ventas de la hora actual
4. **Producto Estrella**: Más vendido del día
5. **Últimas Transacciones**: Feed en tiempo real

## 🚀 Instalación y Configuración

### Backend
1. El servicio `RetailAnalyticsService` ya está integrado
2. Las rutas están configuradas en `/analytics/*`
3. Los datos de demostración están incluidos
4. Para datos reales, conectar con la base de datos existente

### Frontend
1. El componente `Analytics.tsx` está listo
2. Se integra automáticamente con la sidebar
3. Actualización automática cada 30 segundos
4. Responsive design incluido

## 📊 Datos de Demostración

El sistema incluye datos realistas de demostración:

### Productos de Ejemplo
- Coca Cola 600ml: 456 unidades vendidas, $8,208 ingresos
- Pan Bimbo Integral: 234 unidades, $7,020 ingresos
- Leche Lala 1L: 189 unidades, $4,914 ingresos

### Métricas de Ejemplo
- **Total Ventas**: 1,247 transacciones
- **Ingresos Totales**: $45,678.50
- **Ticket Promedio**: $36.65
- **Efectivo**: 756 ventas ($27,890.30)
- **Tarjeta**: 491 ventas ($17,788.20)

### Alertas de Stock
- Aceite Capullo 1L: 2 unidades (CRÍTICO)
- Azúcar Estándar 1kg: 5 unidades (BAJO)
- Papel Higiénico: 8 unidades (BAJO)

## 🔧 Personalización

### Agregar Nuevas Métricas
1. Extender las interfaces en `Analytics.kt`
2. Implementar lógica en `RetailAnalyticsService.kt`
3. Agregar endpoint en `AnalyticsRoutes.kt`
4. Actualizar componente frontend

### Configurar Alertas
```kotlin
// Niveles de stock personalizables
enum class StockLevel {
    CRITICAL,    // Stock <= 0
    LOW,         // Stock <= minAllowStock
    WARNING,     // Stock <= minAllowStock * 1.5
    NORMAL       // Stock > minAllowStock * 1.5
}
```

### Filtros Adicionales
- Por categoría de producto
- Por vendedor/cajero
- Por descuentos aplicados
- Por horarios específicos

## 📱 Interfaz de Usuario

### Dashboard Principal
- **Header**: Título y filtros de período
- **Métricas en Tiempo Real**: 4 tarjetas principales
- **KPIs Principales**: 4 métricas clave
- **Tabs de Navegación**: 3 secciones principales

### Sección de Ventas
- Tabla de productos más vendidos
- Gráfico de ventas por método de pago
- Métricas de rendimiento

### Sección de Inventario
- Tabla de productos con stock bajo
- Lista de productos de mayor valor
- Alertas de reabastecimiento

### Sección de Tiempo Real
- Feed de ventas recientes
- Producto estrella del día
- Métricas actualizadas automáticamente

## 🎨 Características de UX

### Indicadores Visuales
- **Chips de colores** para niveles de stock
- **Iconos descriptivos** para cada sección
- **Formato de moneda** mexicana (MXN)
- **Números formateados** (1.2K, 1.5M)

### Responsive Design
- **Mobile-first**: Optimizado para tablets y móviles
- **Grid adaptativo**: Se ajusta a diferentes pantallas
- **Navegación por tabs**: Fácil acceso en dispositivos táctiles

### Actualizaciones Automáticas
- **Tiempo real**: Cada 30 segundos
- **Indicadores de carga**: Skeletons durante la carga
- **Manejo de errores**: Fallback a datos de demostración

## 🔮 Próximas Funcionalidades

### Reportes Avanzados
- Exportación a PDF/Excel
- Reportes programados
- Análisis predictivo

### Integración Avanzada
- Conexión con sistema de facturación
- Integración con proveedores
- API para sistemas externos

### Analytics Avanzados
- Análisis de tendencias estacionales
- Predicción de demanda
- Optimización de inventario
- Análisis de rentabilidad por producto

## 📞 Soporte

Para dudas o mejoras del sistema de analytics:
1. Revisar la documentación técnica
2. Consultar los endpoints de API
3. Verificar los datos de demostración
4. Contactar al equipo de desarrollo

---

**Nota**: Este sistema está específicamente diseñado para negocios de retail y punto de venta, proporcionando las métricas más relevantes para la toma de decisiones comerciales. 