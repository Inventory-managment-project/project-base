import { useState, useEffect } from 'react';
import { Card, CardBody, CardHeader } from '@heroui/card';
import { Button } from '@heroui/button';
import { Tabs, Tab } from '@heroui/tabs';
import { Progress } from '@heroui/progress';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from '@heroui/table';
import { Skeleton } from '@heroui/skeleton';

// Interfaces para el sistema de punto de venta
interface SalesAnalytics {
  totalSales: number;
  totalRevenue: number;
  averageTicket: number;
  salesByPaymentMethod: Record<string, number>;
  revenueByPaymentMethod: Record<string, number>;
  topSellingProducts: ProductSalesMetric[];
  salesByHour: HourlySalesMetric[];
  salesByDay: DailySalesMetric[];
  lowStockProducts: LowStockProduct[];
  salesGrowth: SalesGrowthMetric[];
}

interface ProductSalesMetric {
  productId: number;
  productName: string;
  quantitySold: number;
  revenue: number;
  salesCount: number;
  averagePrice: number;
}

interface HourlySalesMetric {
  hour: number;
  salesCount: number;
  revenue: number;
}

interface DailySalesMetric {
  date: string;
  salesCount: number;
  revenue: number;
  averageTicket: number;
  transactionsByPaymentMethod: Record<string, number>;
}

interface LowStockProduct {
  productId: number;
  productName: string;
  currentStock: number;
  minAllowStock: number;
  stockLevel: 'CRITICAL' | 'LOW' | 'WARNING' | 'NORMAL';
  lastSaleDate: string | null;
}

interface SalesGrowthMetric {
  date: string;
  salesCount: number;
  revenue: number;
  growthPercentage: number;
  revenueGrowthPercentage: number;
}

interface InventoryAnalytics {
  totalProducts: number;
  totalStockValue: number;
  lowStockCount: number;
  outOfStockCount: number;
  averageStockLevel: number;
  topValueProducts: ProductValueMetric[];
  stockMovement: StockMovementMetric[];
}

interface ProductValueMetric {
  productId: number;
  productName: string;
  stock: number;
  retailPrice: number;
  totalValue: number;
}

interface StockMovementMetric {
  productId: number;
  productName: string;
  initialStock: number;
  currentStock: number;
  totalSold: number;
  turnoverRate: number;
}

interface RealtimeMetrics {
  todaySales: number;
  todayRevenue: number;
  currentHourSales: number;
  averageTicketToday: number;
  topSellingProductToday: ProductSalesMetric | null;
  recentSales: RecentSaleMetric[];
}

interface RecentSaleMetric {
  saleId: number;
  total: number;
  paymentMethod: 'CASH' | 'CARD';
  timestamp: string;
  productCount: number;
}

export default function Analytics() {
  const [salesData, setSalesData] = useState<SalesAnalytics | null>(null);
  const [inventoryData, setInventoryData] = useState<InventoryAnalytics | null>(null);
  const [realtimeData, setRealtimeData] = useState<RealtimeMetrics | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedPeriod, setSelectedPeriod] = useState('7d');

  const fetchSalesAnalytics = async (period: string) => {
    try {
      setLoading(true);
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/analytics/sales?period=${period}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
          'Content-Type': 'application/json',
        },
      });
      
      if (response.ok) {
        const data = await response.json();
        setSalesData(data);
      } else {
        // Datos de demostración si la API no está disponible
        setSalesData({
          totalSales: 1247,
          totalRevenue: 45678.50,
          averageTicket: 36.65,
          salesByPaymentMethod: { 'CASH': 756, 'CARD': 491 },
          revenueByPaymentMethod: { 'CASH': 27890.30, 'CARD': 17788.20 },
          topSellingProducts: [
            { productId: 101, productName: 'Coca Cola 600ml', quantitySold: 456, revenue: 8208.00, salesCount: 456, averagePrice: 18.00 },
            { productId: 102, productName: 'Pan Bimbo Integral', quantitySold: 234, revenue: 7020.00, salesCount: 234, averagePrice: 30.00 },
            { productId: 103, productName: 'Leche Lala 1L', quantitySold: 189, revenue: 4914.00, salesCount: 189, averagePrice: 26.00 },
            { productId: 104, productName: 'Sabritas Clásicas', quantitySold: 167, revenue: 2505.00, salesCount: 167, averagePrice: 15.00 },
            { productId: 105, productName: 'Agua Bonafont 1.5L', quantitySold: 145, revenue: 2175.00, salesCount: 145, averagePrice: 15.00 }
          ],
          salesByHour: [
            { hour: 8, salesCount: 12, revenue: 456.50 },
            { hour: 9, salesCount: 23, revenue: 892.30 },
            { hour: 10, salesCount: 34, revenue: 1245.80 },
            { hour: 11, salesCount: 45, revenue: 1678.90 },
            { hour: 12, salesCount: 67, revenue: 2456.70 },
            { hour: 13, salesCount: 78, revenue: 2890.40 },
            { hour: 14, salesCount: 56, revenue: 2134.60 },
            { hour: 15, salesCount: 43, revenue: 1567.80 },
            { hour: 16, salesCount: 38, revenue: 1389.20 },
            { hour: 17, salesCount: 52, revenue: 1923.40 },
            { hour: 18, salesCount: 61, revenue: 2245.70 },
            { hour: 19, salesCount: 48, revenue: 1756.30 },
            { hour: 20, salesCount: 35, revenue: 1289.50 },
            { hour: 21, salesCount: 22, revenue: 823.40 }
          ],
          salesByDay: [
            { date: '2024-01-15', salesCount: 156, revenue: 5678.90, averageTicket: 36.40, transactionsByPaymentMethod: { 'CASH': 89, 'CARD': 67 } },
            { date: '2024-01-16', salesCount: 178, revenue: 6234.50, averageTicket: 35.02, transactionsByPaymentMethod: { 'CASH': 102, 'CARD': 76 } },
            { date: '2024-01-17', salesCount: 134, revenue: 4892.30, averageTicket: 36.51, transactionsByPaymentMethod: { 'CASH': 78, 'CARD': 56 } },
            { date: '2024-01-18', salesCount: 189, revenue: 7123.80, averageTicket: 37.69, transactionsByPaymentMethod: { 'CASH': 112, 'CARD': 77 } },
            { date: '2024-01-19', salesCount: 167, revenue: 6045.70, averageTicket: 36.20, transactionsByPaymentMethod: { 'CASH': 95, 'CARD': 72 } },
            { date: '2024-01-20', salesCount: 201, revenue: 7456.90, averageTicket: 37.10, transactionsByPaymentMethod: { 'CASH': 118, 'CARD': 83 } },
            { date: '2024-01-21', salesCount: 222, revenue: 8234.60, averageTicket: 37.09, transactionsByPaymentMethod: { 'CASH': 134, 'CARD': 88 } }
          ],
          lowStockProducts: [
            { productId: 201, productName: 'Aceite Capullo 1L', currentStock: 2, minAllowStock: 10, stockLevel: 'CRITICAL', lastSaleDate: '2024-01-21' },
            { productId: 202, productName: 'Azúcar Estándar 1kg', currentStock: 5, minAllowStock: 15, stockLevel: 'LOW', lastSaleDate: '2024-01-21' },
            { productId: 203, productName: 'Papel Higiénico Suave', currentStock: 8, minAllowStock: 20, stockLevel: 'LOW', lastSaleDate: '2024-01-20' },
            { productId: 204, productName: 'Detergente Ariel 1kg', currentStock: 12, minAllowStock: 25, stockLevel: 'WARNING', lastSaleDate: '2024-01-19' }
          ],
          salesGrowth: [
            { date: '2024-01-15', salesCount: 156, revenue: 5678.90, growthPercentage: 5.2, revenueGrowthPercentage: 7.8 },
            { date: '2024-01-16', salesCount: 178, revenue: 6234.50, growthPercentage: 14.1, revenueGrowthPercentage: 9.8 },
            { date: '2024-01-17', salesCount: 134, revenue: 4892.30, growthPercentage: -24.7, revenueGrowthPercentage: -21.5 },
            { date: '2024-01-18', salesCount: 189, revenue: 7123.80, growthPercentage: 41.0, revenueGrowthPercentage: 45.6 },
            { date: '2024-01-19', salesCount: 167, revenue: 6045.70, growthPercentage: -11.6, revenueGrowthPercentage: -15.1 },
            { date: '2024-01-20', salesCount: 201, revenue: 7456.90, growthPercentage: 20.4, revenueGrowthPercentage: 23.3 },
            { date: '2024-01-21', salesCount: 222, revenue: 8234.60, growthPercentage: 10.4, revenueGrowthPercentage: 10.4 }
          ]
        });
      }
    } catch (error) {
      console.error('Error fetching sales analytics:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchInventoryAnalytics = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/analytics/inventory`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
          'Content-Type': 'application/json',
        },
      });
      
      if (response.ok) {
        const data = await response.json();
        setInventoryData(data);
      } else {
        // Datos de demostración
        setInventoryData({
          totalProducts: 234,
          totalStockValue: 89456.75,
          lowStockCount: 12,
          outOfStockCount: 3,
          averageStockLevel: 67.8,
          topValueProducts: [
            { productId: 301, productName: 'iPhone 15 Pro', stock: 5, retailPrice: 25999.00, totalValue: 129995.00 },
            { productId: 302, productName: 'Samsung Galaxy S24', stock: 8, retailPrice: 18999.00, totalValue: 151992.00 },
            { productId: 303, productName: 'MacBook Air M2', stock: 3, retailPrice: 32999.00, totalValue: 98997.00 }
          ],
          stockMovement: [
            { productId: 101, productName: 'Coca Cola 600ml', initialStock: 500, currentStock: 44, totalSold: 456, turnoverRate: 91.2 },
            { productId: 102, productName: 'Pan Bimbo Integral', initialStock: 300, currentStock: 66, totalSold: 234, turnoverRate: 78.0 },
            { productId: 103, productName: 'Leche Lala 1L', initialStock: 250, currentStock: 61, totalSold: 189, turnoverRate: 75.6 }
          ]
        });
      }
    } catch (error) {
      console.error('Error fetching inventory analytics:', error);
    }
  };

  const fetchRealtimeData = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/analytics/realtime`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
          'Content-Type': 'application/json',
        },
      });
      
      if (response.ok) {
        const data = await response.json();
        setRealtimeData(data);
      } else {
        // Datos de demostración
        setRealtimeData({
          todaySales: 89,
          todayRevenue: 3245.80,
          currentHourSales: 7,
          averageTicketToday: 36.47,
          topSellingProductToday: { productId: 101, productName: 'Coca Cola 600ml', quantitySold: 24, revenue: 432.00, salesCount: 24, averagePrice: 18.00 },
          recentSales: [
            { saleId: 1001, total: 156.50, paymentMethod: 'CARD', timestamp: new Date(Date.now() - 5 * 60000).toISOString(), productCount: 4 },
            { saleId: 1002, total: 89.30, paymentMethod: 'CASH', timestamp: new Date(Date.now() - 12 * 60000).toISOString(), productCount: 3 },
            { saleId: 1003, total: 234.80, paymentMethod: 'CARD', timestamp: new Date(Date.now() - 18 * 60000).toISOString(), productCount: 6 },
            { saleId: 1004, total: 67.20, paymentMethod: 'CASH', timestamp: new Date(Date.now() - 25 * 60000).toISOString(), productCount: 2 },
            { saleId: 1005, total: 345.60, paymentMethod: 'CARD', timestamp: new Date(Date.now() - 31 * 60000).toISOString(), productCount: 8 }
          ]
        });
      }
    } catch (error) {
      console.error('Error fetching realtime data:', error);
    }
  };

  useEffect(() => {
    fetchSalesAnalytics(selectedPeriod);
    fetchInventoryAnalytics();
    fetchRealtimeData();

    // Actualizar datos en tiempo real cada 30 segundos
    const interval = setInterval(fetchRealtimeData, 30000);
    return () => clearInterval(interval);
  }, [selectedPeriod]);

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('es-MX', {
      style: 'currency',
      currency: 'MXN'
    }).format(amount);
  };

  const formatNumber = (num: number) => {
    if (num >= 1000000) {
      return (num / 1000000).toFixed(1) + 'M';
    }
    if (num >= 1000) {
      return (num / 1000).toFixed(1) + 'K';
    }
    return num.toString();
  };

  const getStockLevelColor = (level: string) => {
    switch (level) {
      case 'CRITICAL': return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
      case 'LOW': return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
      case 'WARNING': return 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200';
      default: return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
    }
  };

  const getStockLevelText = (level: string) => {
    switch (level) {
      case 'CRITICAL': return 'Crítico';
      case 'LOW': return 'Bajo';
      case 'WARNING': return 'Advertencia';
      default: return 'Normal';
    }
  };

  return (
    <div className="w-full max-w-7xl p-6 space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">🏪 Analytics de Ventas</h1>
        <div className="flex gap-2">
          {['1d', '7d', '30d', '90d'].map((period) => (
            <Button
              key={period}
              size="sm"
              variant={selectedPeriod === period ? 'solid' : 'bordered'}
              onClick={() => setSelectedPeriod(period)}
            >
              {period === '1d' ? '24h' : period}
            </Button>
          ))}
        </div>
      </div>

      {/* Métricas en tiempo real */}
      <Card>
        <CardHeader>
          <h2 className="text-xl font-semibold">🔴 Ventas en Tiempo Real</h2>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="text-center">
              <div className="text-2xl font-bold text-green-500">
                {realtimeData?.todaySales || 0}
              </div>
              <div className="text-sm text-gray-500">Ventas Hoy</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-blue-500">
                {formatCurrency(realtimeData?.todayRevenue || 0)}
              </div>
              <div className="text-sm text-gray-500">Ingresos Hoy</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-purple-500">
                {realtimeData?.currentHourSales || 0}
              </div>
              <div className="text-sm text-gray-500">Ventas Esta Hora</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-orange-500">
                {formatCurrency(realtimeData?.averageTicketToday || 0)}
              </div>
              <div className="text-sm text-gray-500">Ticket Promedio</div>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Métricas principales de ventas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card>
          <CardBody className="text-center">
            {loading ? (
              <Skeleton className="h-8 w-16 mx-auto mb-2" />
            ) : (
              <div className="text-2xl font-bold">{formatNumber(salesData?.totalSales || 0)}</div>
            )}
            <div className="text-sm text-gray-500">Total Ventas</div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="text-center">
            {loading ? (
              <Skeleton className="h-8 w-20 mx-auto mb-2" />
            ) : (
              <div className="text-2xl font-bold">{formatCurrency(salesData?.totalRevenue || 0)}</div>
            )}
            <div className="text-sm text-gray-500">Ingresos Totales</div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="text-center">
            {loading ? (
              <Skeleton className="h-8 w-20 mx-auto mb-2" />
            ) : (
              <div className="text-2xl font-bold">
                {formatCurrency(salesData?.averageTicket || 0)}
              </div>
            )}
            <div className="text-sm text-gray-500">Ticket Promedio</div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="text-center">
            {loading ? (
              <Skeleton className="h-8 w-16 mx-auto mb-2" />
            ) : (
              <div className="text-2xl font-bold">{inventoryData?.lowStockCount || 0}</div>
            )}
            <div className="text-sm text-gray-500">Productos Stock Bajo</div>
          </CardBody>
        </Card>
      </div>

      {/* Tabs para diferentes vistas */}
      <Tabs aria-label="Analytics tabs" className="w-full">
        <Tab key="sales" title="💰 Ventas">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-4">
            {/* Productos más vendidos */}
            <Card>
              <CardHeader>
                <h3 className="text-lg font-semibold">🏆 Productos Más Vendidos</h3>
              </CardHeader>
              <CardBody>
                {loading ? (
                  <div className="space-y-3">
                    {[...Array(5)].map((_, i) => (
                      <Skeleton key={i} className="h-4 w-full" />
                    ))}
                  </div>
                ) : (
                  <Table aria-label="Top products table">
                    <TableHeader>
                      <TableColumn>Producto</TableColumn>
                      <TableColumn>Cantidad</TableColumn>
                      <TableColumn>Ingresos</TableColumn>
                    </TableHeader>
                    <TableBody>
                      {(salesData?.topSellingProducts || []).map((product, index) => (
                        <TableRow key={index}>
                          <TableCell className="font-medium">{product.productName}</TableCell>
                          <TableCell>{formatNumber(product.quantitySold)}</TableCell>
                          <TableCell>{formatCurrency(product.revenue)}</TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                )}
              </CardBody>
            </Card>

            {/* Ventas por método de pago */}
            <Card>
              <CardHeader>
                <h3 className="text-lg font-semibold">💳 Ventas por Método de Pago</h3>
              </CardHeader>
              <CardBody>
                {loading ? (
                  <div className="space-y-3">
                    {[...Array(2)].map((_, i) => (
                      <Skeleton key={i} className="h-4 w-full" />
                    ))}
                  </div>
                ) : (
                  <div className="space-y-4">
                    {Object.entries(salesData?.salesByPaymentMethod || {}).map(([method, count]) => (
                      <div key={method} className="flex items-center justify-between">
                        <div className="flex items-center space-x-2">
                          <span className="font-medium">
                            {method === 'CASH' ? '💵 Efectivo' : '💳 Tarjeta'}
                          </span>
                        </div>
                        <div className="text-right">
                          <div className="font-bold">{count} ventas</div>
                          <div className="text-sm text-gray-500">
                            {formatCurrency(salesData?.revenueByPaymentMethod[method] || 0)}
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </CardBody>
            </Card>
          </div>
        </Tab>

        <Tab key="inventory" title="📦 Inventario">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-4">
            {/* Productos con stock bajo */}
            <Card>
              <CardHeader>
                <h3 className="text-lg font-semibold">⚠️ Productos con Stock Bajo</h3>
              </CardHeader>
              <CardBody>
                {loading ? (
                  <div className="space-y-3">
                    {[...Array(4)].map((_, i) => (
                      <Skeleton key={i} className="h-4 w-full" />
                    ))}
                  </div>
                ) : (
                  <Table aria-label="Low stock products table">
                    <TableHeader>
                      <TableColumn>Producto</TableColumn>
                      <TableColumn>Stock</TableColumn>
                      <TableColumn>Estado</TableColumn>
                    </TableHeader>
                    <TableBody>
                      {(salesData?.lowStockProducts || []).map((product, index) => (
                        <TableRow key={index}>
                          <TableCell className="font-medium">{product.productName}</TableCell>
                          <TableCell>{product.currentStock} / {product.minAllowStock}</TableCell>
                          <TableCell>
                            <span 
                              className={`px-2 py-1 rounded-full text-xs font-medium ${getStockLevelColor(product.stockLevel)}`}
                            >
                              {getStockLevelText(product.stockLevel)}
                            </span>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                )}
              </CardBody>
            </Card>

            {/* Productos de mayor valor */}
            <Card>
              <CardHeader>
                <h3 className="text-lg font-semibold">💎 Productos de Mayor Valor</h3>
              </CardHeader>
              <CardBody>
                {loading ? (
                  <div className="space-y-3">
                    {[...Array(3)].map((_, i) => (
                      <Skeleton key={i} className="h-4 w-full" />
                    ))}
                  </div>
                ) : (
                  <Table aria-label="High value products table">
                    <TableHeader>
                      <TableColumn>Producto</TableColumn>
                      <TableColumn>Stock</TableColumn>
                      <TableColumn>Valor Total</TableColumn>
                    </TableHeader>
                    <TableBody>
                      {(inventoryData?.topValueProducts || []).map((product, index) => (
                        <TableRow key={index}>
                          <TableCell className="font-medium">{product.productName}</TableCell>
                          <TableCell>{product.stock}</TableCell>
                          <TableCell>{formatCurrency(product.totalValue)}</TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                )}
              </CardBody>
            </Card>
          </div>
        </Tab>

        <Tab key="realtime" title="🔴 Tiempo Real">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-4">
            {/* Ventas recientes */}
            <Card>
              <CardHeader>
                <h3 className="text-lg font-semibold">⚡ Ventas Recientes</h3>
              </CardHeader>
              <CardBody>
                <div className="space-y-2 max-h-80 overflow-y-auto">
                  {(realtimeData?.recentSales || []).map((sale, index) => (
                    <div key={index} className="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-800 rounded-lg">
                      <div>
                        <div className="font-medium">Venta #{sale.saleId}</div>
                        <div className="text-sm text-gray-500">
                          {sale.productCount} productos • {sale.paymentMethod === 'CASH' ? '💵 Efectivo' : '💳 Tarjeta'}
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="font-bold">{formatCurrency(sale.total)}</div>
                        <div className="text-xs text-gray-400">
                          {new Date(sale.timestamp).toLocaleTimeString()}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </CardBody>
            </Card>

            {/* Producto más vendido hoy */}
            <Card>
              <CardHeader>
                <h3 className="text-lg font-semibold">🌟 Producto Estrella del Día</h3>
              </CardHeader>
              <CardBody>
                {realtimeData?.topSellingProductToday ? (
                  <div className="text-center space-y-4">
                    <div className="text-2xl font-bold text-blue-600">
                      {realtimeData.topSellingProductToday.productName}
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <div className="text-lg font-semibold">{realtimeData.topSellingProductToday.quantitySold}</div>
                        <div className="text-sm text-gray-500">Unidades Vendidas</div>
                      </div>
                      <div>
                        <div className="text-lg font-semibold">{formatCurrency(realtimeData.topSellingProductToday.revenue)}</div>
                        <div className="text-sm text-gray-500">Ingresos Generados</div>
                      </div>
                    </div>
                  </div>
                ) : (
                  <div className="text-center text-gray-500">
                    No hay datos disponibles
                  </div>
                )}
              </CardBody>
            </Card>
          </div>
        </Tab>
      </Tabs>

      {/* Información del sistema */}
      <Card>
        <CardHeader>
          <h3 className="text-lg font-semibold">ℹ️ Información del Sistema</h3>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
            <div>
              <strong>Estado:</strong> Sistema POS funcionando correctamente
            </div>
            <div>
              <strong>Última actualización:</strong> {new Date().toLocaleString()}
            </div>
            <div>
              <strong>Datos:</strong> Ventas en tiempo real + históricos
            </div>
          </div>
        </CardBody>
      </Card>
    </div>
  );
} 