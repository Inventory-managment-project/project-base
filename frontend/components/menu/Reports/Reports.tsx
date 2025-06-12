import { title, subtitle } from "@/components/misc/primitives";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { Card, CardBody, CardHeader } from "@heroui/card";
import { Divider } from "@heroui/divider";
import { useState, useEffect } from "react";
import { CalendarIcon, BarChart3Icon, TrendingUpIcon, PackageIcon, AlertCircleIcon, InfoIcon } from "lucide-react";
import { useSelectedStore } from "@/context/SelectedStoreContext";
import { useStatusAlerts } from "@/hooks/useStatusAlerts";
import StatusAlertsStack from "@/components/misc/StatusAlertStack";
import { CircularProgress } from "@heroui/progress";
import ProductSalesReport from "./ProductSalesReport";
import DateRangeSalesReport from "./DateRangeSalesReport";

export type ProductSalesReportData = {
  productId: number;
  productName: string;
  totalQuantitySold: number;
  totalRevenue: number;
  averagePrice: number;
};

export type DateRangeSalesReportData = {
  startDate: number;
  endDate: number;
  totalSales: number;
  totalRevenue: number;
  productBreakdown: ProductSalesReportData[];
};

type LogEntry = {
  id: string;
  message: string;
  type: 'info' | 'error' | 'success' | 'warning';
  timestamp: Date;
};

export default function Reports() {
  const [activeReport, setActiveReport] = useState<'products' | 'dateRange' | null>(null);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [productReportData, setProductReportData] = useState<ProductSalesReportData[]>([]);
  const [dateRangeReportData, setDateRangeReportData] = useState<DateRangeSalesReportData | null>(null);
  const [loading, setLoading] = useState(false);
  const [logs, setLogs] = useState<LogEntry[]>([]);
  const [showLogs, setShowLogs] = useState(false);
  const { selectedStoreString } = useSelectedStore();
  const { alerts, triggerAlert, removeAlert } = useStatusAlerts();

  // Función para agregar logs
  const addLog = (message: string, type: LogEntry['type'] = 'info') => {
    const newLog: LogEntry = {
      id: Date.now().toString(),
      message,
      type,
      timestamp: new Date()
    };
    setLogs(prev => [newLog, ...prev.slice(0, 49)]); // Mantener solo los últimos 50 logs
    console.log(`[${type.toUpperCase()}] ${message}`);
  };

  // Configurar fechas por defecto (últimos 30 días)
  useEffect(() => {
    const today = new Date();
    const thirtyDaysAgo = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000);
    
    setEndDate(today.toISOString().split('T')[0]);
    setStartDate(thirtyDaysAgo.toISOString().split('T')[0]);
    
    addLog('Fechas por defecto establecidas: últimos 30 días', 'info');
  }, []);

  const fetchProductSalesReport = async () => {
    if (!startDate || !endDate || selectedStoreString === '0') {
      const errorMsg = 'Por favor selecciona una tienda y rango de fechas';
      triggerAlert('error', errorMsg, 400);
      addLog(errorMsg, 'error');
      return;
    }

    setLoading(true);
    addLog('Iniciando generación de reporte de ventas por producto...', 'info');
    
    try {
      const startTimestamp = new Date(startDate).getTime();
      const endTimestamp = new Date(endDate).getTime();
      
      addLog(`Consultando datos desde ${startDate} hasta ${endDate}`, 'info');
      
      const response = await fetch(
        `/api/stores/${selectedStoreString}/reports/sales/products?startDate=${startTimestamp}&endDate=${endTimestamp}`,
        {
          method: 'GET',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        setProductReportData(data);
        setActiveReport('products');
        const successMsg = `Reporte de productos generado exitosamente (${data.length} productos)`;
        triggerAlert('success', successMsg, 200);
        addLog(successMsg, 'success');
      } else {
        const errorMsg = `Error al generar reporte de productos (Status: ${response.status})`;
        triggerAlert('error', 'Error al generar reporte de ventas por producto', 400);
        addLog(errorMsg, 'error');
      }
    } catch (error) {
      const errorMsg = `Error de conexión al generar reporte de productos: ${error}`;
      triggerAlert('error', 'Error al generar reporte de ventas por producto', 400);
      addLog(errorMsg, 'error');
    } finally {
      setLoading(false);
      addLog('Proceso de generación de reporte de productos finalizado', 'info');
    }
  };

  const fetchDateRangeSalesReport = async () => {
    if (!startDate || !endDate || selectedStoreString === '0') {
      const errorMsg = 'Por favor selecciona una tienda y rango de fechas';
      triggerAlert('error', errorMsg, 400);
      addLog(errorMsg, 'error');
      return;
    }

    setLoading(true);
    addLog('Iniciando generación de reporte de ventas por rango de fechas...', 'info');
    
    try {
      const startTimestamp = new Date(startDate).getTime();
      const endTimestamp = new Date(endDate).getTime();
      
      addLog(`Consultando datos desde ${startDate} hasta ${endDate}`, 'info');
      
      const response = await fetch(
        `/api/stores/${selectedStoreString}/reports/sales/date-range?startDate=${startTimestamp}&endDate=${endTimestamp}`,
        {
          method: 'GET',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        setDateRangeReportData(data);
        setActiveReport('dateRange');
        const successMsg = `Reporte de rango de fechas generado exitosamente (${data.totalSales} ventas)`;
        triggerAlert('success', successMsg, 200);
        addLog(successMsg, 'success');
      } else {
        const errorMsg = `Error al generar reporte de rango de fechas (Status: ${response.status})`;
        triggerAlert('error', 'Error al generar reporte de ventas por rango de fechas', 400);
        addLog(errorMsg, 'error');
      }
    } catch (error) {
      const errorMsg = `Error de conexión al generar reporte de rango de fechas: ${error}`;
      triggerAlert('error', 'Error al generar reporte de ventas por rango de fechas', 400);
      addLog(errorMsg, 'error');
    } finally {
      setLoading(false);
      addLog('Proceso de generación de reporte de rango de fechas finalizado', 'info');
    }
  };

  const resetReports = () => {
    setActiveReport(null);
    setProductReportData([]);
    setDateRangeReportData(null);
    addLog('Reportes reiniciados', 'info');
  };

  const clearLogs = () => {
    setLogs([]);
    addLog('Logs limpiados', 'info');
  };

  const getLogIcon = (type: LogEntry['type']) => {
    switch (type) {
      case 'error': return <AlertCircleIcon className="w-4 h-4 text-danger" />;
      case 'success': return <div className="w-4 h-4 rounded-full bg-success flex items-center justify-center text-white text-xs">✓</div>;
      case 'warning': return <AlertCircleIcon className="w-4 h-4 text-warning" />;
      default: return <InfoIcon className="w-4 h-4 text-primary" />;
    }
  };

  const getLogColor = (type: LogEntry['type']) => {
    switch (type) {
      case 'error': return 'text-danger';
      case 'success': return 'text-success';
      case 'warning': return 'text-warning';
      default: return 'text-default-600';
    }
  };

  if (selectedStoreString === '0') {
    return (
      <div className="flex flex-col items-center justify-center h-96 bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-blue-950 dark:to-indigo-900 rounded-2xl">
        <div className="text-center space-y-4">
          <div className="w-24 h-24 mx-auto bg-gradient-to-br from-blue-500 to-indigo-600 rounded-full flex items-center justify-center">
            <BarChart3Icon className="w-12 h-12 text-white" />
          </div>
          <h2 className={title({ color: 'blue' })}>Reportes de Ventas</h2>
          <p className="text-gray-600 dark:text-gray-300 text-lg">
            Por favor selecciona una tienda para visualizar los reportes
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex flex-col gap-8 p-6 bg-gradient-to-br from-gray-50 to-blue-50 dark:from-gray-900 dark:to-blue-950 min-h-screen">
      <StatusAlertsStack alerts={alerts} onClose={removeAlert} />
      
      {/* Header mejorado */}
      <div className="flex justify-between items-center bg-white dark:bg-gray-800 p-6 rounded-2xl shadow-lg border border-gray-200 dark:border-gray-700">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-xl flex items-center justify-center">
            <BarChart3Icon className="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 className={title({ size: 'lg' })}>Reportes de Ventas</h1>
            <p className="text-gray-600 dark:text-gray-300">Analiza el rendimiento de tu tienda</p>
          </div>
        </div>
        <div className="flex gap-3">
          <Button 
            color="secondary" 
            variant="flat" 
            onPress={() => setShowLogs(!showLogs)}
            startContent={<InfoIcon className="w-4 h-4" />}
          >
            {showLogs ? 'Ocultar Logs' : 'Ver Logs'}
          </Button>
          {activeReport && (
            <Button 
              color="primary" 
              variant="flat" 
              onPress={resetReports}
              className="bg-gradient-to-r from-blue-500 to-indigo-600 text-white hover:from-blue-600 hover:to-indigo-700"
            >
              Volver a Reportes
            </Button>
          )}
        </div>
      </div>

      {/* Panel de Logs */}
      {showLogs && (
        <Card className="bg-white dark:bg-gray-800 shadow-lg border border-gray-200 dark:border-gray-700">
          <CardHeader className="flex justify-between items-center">
            <h3 className={subtitle()}>Registro de Actividad</h3>
            <Button 
              size="sm" 
              color="danger" 
              variant="flat" 
              onPress={clearLogs}
            >
              Limpiar Logs
            </Button>
          </CardHeader>
          <CardBody>
            <div className="max-h-60 overflow-y-auto space-y-2">
              {logs.length === 0 ? (
                <p className="text-gray-500 text-center py-4">No hay registros de actividad</p>
              ) : (
                logs.map((log) => (
                  <div key={log.id} className="flex items-start gap-3 p-3 bg-gray-50 dark:bg-gray-700 rounded-lg">
                    {getLogIcon(log.type)}
                    <div className="flex-1 min-w-0">
                      <p className={`text-sm font-medium ${getLogColor(log.type)}`}>
                        {log.message}
                      </p>
                      <p className="text-xs text-gray-500 mt-1">
                        {log.timestamp.toLocaleTimeString()}
                      </p>
                    </div>
                  </div>
                ))
              )}
            </div>
          </CardBody>
        </Card>
      )}

      {!activeReport && (
        <>
          {/* Selección de Rango de Fechas */}
          <Card className="bg-white dark:bg-gray-800 shadow-lg border border-gray-200 dark:border-gray-700">
            <CardHeader className="bg-gradient-to-r from-blue-500 to-indigo-600 text-white rounded-t-xl">
              <div className="flex items-center gap-3">
                <CalendarIcon className="w-6 h-6" />
                <h3 className={subtitle({ class: 'text-white' })}>Seleccionar Rango de Fechas</h3>
              </div>
            </CardHeader>
            <CardBody className="p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Input
                  type="date"
                  label="Fecha de Inicio"
                  value={startDate}
                  onChange={(e) => {
                    setStartDate(e.target.value);
                    addLog(`Fecha de inicio actualizada: ${e.target.value}`, 'info');
                  }}
                  className="max-w-full"
                  classNames={{
                    input: "text-lg",
                    label: "text-gray-700 dark:text-gray-300 font-medium"
                  }}
                />
                <Input
                  type="date"
                  label="Fecha de Fin"
                  value={endDate}
                  onChange={(e) => {
                    setEndDate(e.target.value);
                    addLog(`Fecha de fin actualizada: ${e.target.value}`, 'info');
                  }}
                  className="max-w-full"
                  classNames={{
                    input: "text-lg",
                    label: "text-gray-700 dark:text-gray-300 font-medium"
                  }}
                />
              </div>
              <div className="mt-4 p-4 bg-blue-50 dark:bg-blue-950 rounded-xl">
                <p className="text-sm text-blue-700 dark:text-blue-300">
                  📅 Rango seleccionado: {startDate ? new Date(startDate).toLocaleDateString('es-ES') : 'No seleccionado'} - {endDate ? new Date(endDate).toLocaleDateString('es-ES') : 'No seleccionado'}
                </p>
              </div>
            </CardBody>
          </Card>

          {/* Opciones de Reportes */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            <Card className="group hover:shadow-2xl transition-all duration-300 cursor-pointer bg-gradient-to-br from-white to-blue-50 dark:from-gray-800 dark:to-blue-950 border border-gray-200 dark:border-gray-700 hover:border-blue-300 dark:hover:border-blue-600">
              <CardHeader className="flex gap-4 p-6">
                <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-br from-blue-500 to-indigo-600 group-hover:from-blue-600 group-hover:to-indigo-700 transition-all duration-300">
                  <PackageIcon className="w-8 h-8 text-white" />
                </div>
                <div className="flex flex-col flex-1">
                  <h4 className="text-xl font-bold text-gray-800 dark:text-white">Reporte de Productos</h4>
                  <p className="text-gray-600 dark:text-gray-300 mt-1">Analiza el rendimiento por producto</p>
                </div>
              </CardHeader>
              <CardBody className="p-6 pt-0">
                <p className="text-gray-700 dark:text-gray-300 mb-6 leading-relaxed">
                  Genera un reporte detallado mostrando datos de ventas para cada producto, incluyendo cantidad total vendida, ingresos y precio promedio.
                </p>
                <div className="grid grid-cols-3 gap-4 mb-6 p-4 bg-blue-50 dark:bg-blue-950 rounded-xl">
                  <div className="text-center">
                    <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">📊</p>
                    <p className="text-xs text-gray-600 dark:text-gray-400">Métricas</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-green-600 dark:text-green-400">💰</p>
                    <p className="text-xs text-gray-600 dark:text-gray-400">Ingresos</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-purple-600 dark:text-purple-400">📈</p>
                    <p className="text-xs text-gray-600 dark:text-gray-400">Análisis</p>
                  </div>
                </div>
                <Button 
                  color="primary" 
                  startContent={<BarChart3Icon className="w-5 h-5" />}
                  onPress={fetchProductSalesReport}
                  isLoading={loading}
                  className="w-full bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white font-medium py-6 text-lg"
                  size="lg"
                >
                  {loading ? 'Generando Reporte...' : 'Generar Reporte de Productos'}
                </Button>
              </CardBody>
            </Card>

            <Card className="group hover:shadow-2xl transition-all duration-300 cursor-pointer bg-gradient-to-br from-white to-green-50 dark:from-gray-800 dark:to-green-950 border border-gray-200 dark:border-gray-700 hover:border-green-300 dark:hover:border-green-600">
              <CardHeader className="flex gap-4 p-6">
                <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-br from-green-500 to-emerald-600 group-hover:from-green-600 group-hover:to-emerald-700 transition-all duration-300">
                  <TrendingUpIcon className="w-8 h-8 text-white" />
                </div>
                <div className="flex flex-col flex-1">
                  <h4 className="text-xl font-bold text-gray-800 dark:text-white">Reporte de Período</h4>
                  <p className="text-gray-600 dark:text-gray-300 mt-1">Rendimiento general de ventas</p>
                </div>
              </CardHeader>
              <CardBody className="p-6 pt-0">
                <p className="text-gray-700 dark:text-gray-300 mb-6 leading-relaxed">
                  Genera un reporte completo mostrando ventas totales, ingresos y desglose de productos para el rango de fechas seleccionado.
                </p>
                <div className="grid grid-cols-3 gap-4 mb-6 p-4 bg-green-50 dark:bg-green-950 rounded-xl">
                  <div className="text-center">
                    <p className="text-2xl font-bold text-green-600 dark:text-green-400">📅</p>
                    <p className="text-xs text-gray-600 dark:text-gray-400">Período</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">🎯</p>
                    <p className="text-xs text-gray-600 dark:text-gray-400">Objetivos</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-orange-600 dark:text-orange-400">🏆</p>
                    <p className="text-xs text-gray-600 dark:text-gray-400">Top 5</p>
                  </div>
                </div>
                <Button 
                  color="success" 
                  startContent={<CalendarIcon className="w-5 h-5" />}
                  onPress={fetchDateRangeSalesReport}
                  isLoading={loading}
                  className="w-full bg-gradient-to-r from-green-500 to-emerald-600 hover:from-green-600 hover:to-emerald-700 text-white font-medium py-6 text-lg"
                  size="lg"
                >
                  {loading ? 'Generando Reporte...' : 'Generar Reporte de Período'}
                </Button>
              </CardBody>
            </Card>
          </div>
        </>
      )}

      {/* Mostrar Reportes */}
      {activeReport === 'products' && (
        <div className="animate-in slide-in-from-bottom-4 duration-500">
          <ProductSalesReport 
            data={productReportData} 
            startDate={startDate} 
            endDate={endDate} 
          />
        </div>
      )}
      
      {activeReport === 'dateRange' && dateRangeReportData && (
        <div className="animate-in slide-in-from-bottom-4 duration-500">
          <DateRangeSalesReport 
            data={dateRangeReportData} 
            startDate={startDate} 
            endDate={endDate} 
          />
        </div>
      )}
    </div>
  );
}