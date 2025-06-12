import { Card, CardBody, CardHeader } from "@heroui/card";
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@heroui/table";
import { Chip } from "@heroui/chip";
import { Button } from "@heroui/button";
import { DownloadIcon, PackageIcon, TrendingUpIcon, DollarSignIcon, HashIcon } from "lucide-react";
import { ProductSalesReportData } from "./Reports";
import { useMemo } from "react";

interface ProductSalesReportProps {
  data: ProductSalesReportData[];
  startDate: string;
  endDate: string;
}

export default function ProductSalesReport({ data, startDate, endDate }: ProductSalesReportProps) {
  const totalRevenue = useMemo(() => {
    return data.reduce((sum, item) => sum + item.totalRevenue, 0);
  }, [data]);

  const totalQuantity = useMemo(() => {
    return data.reduce((sum, item) => sum + item.totalQuantitySold, 0);
  }, [data]);

  const averageRevenue = useMemo(() => {
    return data.length > 0 ? totalRevenue / data.length : 0;
  }, [totalRevenue, data.length]);

  const exportToCSV = () => {
    console.log('Iniciando exportación de reporte de productos a CSV...');
    
    const headers = ['ID Producto', 'Nombre del Producto', 'Cantidad Vendida', 'Ingresos Totales', 'Precio Promedio'];
    const csvContent = [
      headers.join(','),
      ...data.map(item => [
        item.productId,
        `"${item.productName}"`,
        item.totalQuantitySold,
        item.totalRevenue.toFixed(2),
        item.averagePrice.toFixed(2)
      ].join(','))
    ].join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `reporte-ventas-productos-${startDate}-a-${endDate}.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
    
    console.log(`Reporte CSV exportado: reporte-ventas-productos-${startDate}-a-${endDate}.csv`);
    console.log(`Total de productos exportados: ${data.length}`);
  };

  const getPerformanceLevel = (quantity: number) => {
    if (quantity > 20) return { level: 'Excelente', color: 'success' as const };
    if (quantity > 10) return { level: 'Bueno', color: 'primary' as const };
    if (quantity > 5) return { level: 'Regular', color: 'warning' as const };
    return { level: 'Bajo', color: 'danger' as const };
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('es-MX', {
      style: 'currency',
      currency: 'MXN'
    }).format(amount);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  return (
    <div className="flex flex-col gap-8">
      {/* Tarjetas de Resumen Mejoradas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card className="bg-gradient-to-br from-blue-500 to-blue-600 text-white shadow-lg hover:shadow-xl transition-shadow">
          <CardBody className="flex flex-row items-center gap-4 p-6">
            <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm">
              <PackageIcon className="w-8 h-8 text-white" />
            </div>
            <div className="flex-1">
              <p className="text-blue-100 text-sm font-medium">Total de Productos</p>
              <p className="text-3xl font-bold">{data.length}</p>
              <p className="text-blue-200 text-xs mt-1">productos analizados</p>
            </div>
          </CardBody>
        </Card>
        
        <Card className="bg-gradient-to-br from-green-500 to-green-600 text-white shadow-lg hover:shadow-xl transition-shadow">
          <CardBody className="flex flex-row items-center gap-4 p-6">
            <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm">
              <DollarSignIcon className="w-8 h-8 text-white" />
            </div>
            <div className="flex-1">
              <p className="text-green-100 text-sm font-medium">Ingresos Totales</p>
              <p className="text-2xl font-bold">{formatCurrency(totalRevenue)}</p>
              <p className="text-green-200 text-xs mt-1">en el período</p>
            </div>
          </CardBody>
        </Card>
        
        <Card className="bg-gradient-to-br from-purple-500 to-purple-600 text-white shadow-lg hover:shadow-xl transition-shadow">
          <CardBody className="flex flex-row items-center gap-4 p-6">
            <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm">
              <HashIcon className="w-8 h-8 text-white" />
            </div>
            <div className="flex-1">
              <p className="text-purple-100 text-sm font-medium">Cantidad Total</p>
              <p className="text-3xl font-bold">{totalQuantity.toLocaleString()}</p>
              <p className="text-purple-200 text-xs mt-1">unidades vendidas</p>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-gradient-to-br from-orange-500 to-orange-600 text-white shadow-lg hover:shadow-xl transition-shadow">
          <CardBody className="flex flex-row items-center gap-4 p-6">
            <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm">
              <TrendingUpIcon className="w-8 h-8 text-white" />
            </div>
            <div className="flex-1">
              <p className="text-orange-100 text-sm font-medium">Ingreso Promedio</p>
              <p className="text-2xl font-bold">{formatCurrency(averageRevenue)}</p>
              <p className="text-orange-200 text-xs mt-1">por producto</p>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Tabla de Reporte Mejorada */}
      <Card className="bg-white dark:bg-gray-800 shadow-xl border border-gray-200 dark:border-gray-700">
        <CardHeader className="bg-gradient-to-r from-blue-500 to-indigo-600 text-white rounded-t-xl p-6">
          <div className="flex justify-between items-center w-full">
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                <PackageIcon className="w-6 h-6 text-white" />
              </div>
              <div>
                <h3 className="text-xl font-bold text-white">Reporte de Ventas por Producto</h3>
                <p className="text-blue-100 mt-1">
                  Del {formatDate(startDate)} al {formatDate(endDate)}
                </p>
              </div>
            </div>
            <Button
              color="default"
              variant="flat"
              startContent={<DownloadIcon className="w-4 h-4" />}
              onPress={exportToCSV}
              className="bg-white/10 text-white border-white/20 hover:bg-white/20"
            >
              Exportar CSV
            </Button>
          </div>
        </CardHeader>
        <CardBody className="p-0">
          {data.length > 0 ? (
            <div className="overflow-x-auto">
              <Table 
                aria-label="Tabla de reporte de ventas por producto"
                classNames={{
                  wrapper: "shadow-none",
                  th: "bg-gray-50 dark:bg-gray-700 text-gray-700 dark:text-gray-300 font-semibold",
                  td: "py-4"
                }}
              >
                <TableHeader>
                  <TableColumn className="text-left">PRODUCTO</TableColumn>
                  <TableColumn className="text-center">CANTIDAD VENDIDA</TableColumn>
                  <TableColumn className="text-center">INGRESOS TOTALES</TableColumn>
                  <TableColumn className="text-center">PRECIO PROMEDIO</TableColumn>
                  <TableColumn className="text-center">RENDIMIENTO</TableColumn>
                </TableHeader>
                <TableBody>
                  {data.map((item, index) => {
                    const performance = getPerformanceLevel(item.totalQuantitySold);
                    return (
                      <TableRow 
                        key={item.productId}
                        className="hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                      >
                        <TableCell>
                          <div className="flex items-center gap-3">
                            <div className="w-10 h-10 bg-gradient-to-br from-blue-400 to-indigo-500 rounded-lg flex items-center justify-center text-white font-bold text-sm">
                              {index + 1}
                            </div>
                            <div>
                              <p className="font-semibold text-gray-900 dark:text-white">
                                {item.productName}
                              </p>
                              <p className="text-sm text-gray-500 dark:text-gray-400">
                                ID: {item.productId}
                              </p>
                            </div>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="text-center">
                            <Chip 
                              color="primary" 
                              variant="flat"
                              className="font-semibold"
                            >
                              {item.totalQuantitySold.toLocaleString()} unidades
                            </Chip>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="text-center">
                            <span className="font-bold text-lg text-green-600 dark:text-green-400">
                              {formatCurrency(item.totalRevenue)}
                            </span>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="text-center">
                            <span className="font-semibold text-gray-700 dark:text-gray-300">
                              {formatCurrency(item.averagePrice)}
                            </span>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="text-center">
                            <Chip 
                              color={performance.color}
                              variant="flat"
                              size="sm"
                              className="font-medium"
                            >
                              {performance.level}
                            </Chip>
                          </div>
                        </TableCell>
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          ) : (
            <div className="text-center py-16">
              <div className="w-24 h-24 mx-auto bg-gray-100 dark:bg-gray-700 rounded-full flex items-center justify-center mb-4">
                <PackageIcon className="w-12 h-12 text-gray-400" />
              </div>
              <h3 className="text-lg font-semibold text-gray-700 dark:text-gray-300 mb-2">
                No hay datos de ventas
              </h3>
              <p className="text-gray-500 dark:text-gray-400">
                No se encontraron datos de ventas para el rango de fechas seleccionado.
              </p>
            </div>
          )}
        </CardBody>
      </Card>

      {/* Estadísticas Adicionales */}
      {data.length > 0 && (
        <Card className="bg-gradient-to-br from-gray-50 to-blue-50 dark:from-gray-800 dark:to-blue-950 border border-gray-200 dark:border-gray-700">
          <CardHeader>
            <h3 className="text-lg font-semibold text-gray-800 dark:text-white">
              📊 Estadísticas del Período
            </h3>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="text-center p-4 bg-white dark:bg-gray-700 rounded-xl">
                <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">
                  {Math.max(...data.map(item => item.totalQuantitySold))}
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-400 mt-1">
                  Mayor cantidad vendida
                </p>
              </div>
              <div className="text-center p-4 bg-white dark:bg-gray-700 rounded-xl">
                <p className="text-2xl font-bold text-green-600 dark:text-green-400">
                  {formatCurrency(Math.max(...data.map(item => item.totalRevenue)))}
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-400 mt-1">
                  Mayor ingreso por producto
                </p>
              </div>
              <div className="text-center p-4 bg-white dark:bg-gray-700 rounded-xl">
                <p className="text-2xl font-bold text-purple-600 dark:text-purple-400">
                  {data.filter(item => item.totalQuantitySold > 10).length}
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-400 mt-1">
                  Productos con buen rendimiento
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
}