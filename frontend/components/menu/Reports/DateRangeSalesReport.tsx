import { Card, CardBody, CardHeader } from "@heroui/card";
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@heroui/table";
import { Button } from "@heroui/button";
import { Progress } from "@heroui/progress";
import { DownloadIcon, CalendarIcon, TrendingUpIcon, DollarSignIcon, StarIcon, CrownIcon } from "lucide-react";
import { DateRangeSalesReportData } from "./Reports";
import { useMemo } from "react";

interface DateRangeSalesReportProps {
  data: DateRangeSalesReportData;
  startDate: string;
  endDate: string;
}

export default function DateRangeSalesReport({ data, startDate, endDate }: DateRangeSalesReportProps) {
  const averageRevenuePerSale = useMemo(() => {
    return data.totalSales > 0 ? data.totalRevenue / data.totalSales : 0;
  }, [data]);

  const topProducts = useMemo(() => {
    return data.productBreakdown
      .sort((a, b) => b.totalRevenue - a.totalRevenue)
      .slice(0, 5);
  }, [data.productBreakdown]);

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

  const exportToCSV = () => {
    console.log('Iniciando exportación de reporte de período a CSV...');
    
    const summaryData = [
      ['Métrica', 'Valor'],
      ['Rango de Fechas', `${formatDate(startDate)} a ${formatDate(endDate)}`],
      ['Total de Ventas', data.totalSales.toString()],
      ['Ingresos Totales', formatCurrency(data.totalRevenue)],
      ['Ingreso Promedio por Venta', formatCurrency(averageRevenuePerSale)],
      [''],
      ['Desglose por Producto'],
      ['Nombre del Producto', 'Cantidad Vendida', 'Ingresos Totales', 'Precio Promedio'],
      ...data.productBreakdown.map(item => [
        item.productName,
        item.totalQuantitySold.toString(),
        item.totalRevenue.toFixed(2),
        item.averagePrice.toFixed(2)
      ])
    ];

    const csvContent = summaryData.map(row => 
      row.map(cell => `"${cell}"`).join(',')
    ).join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `reporte-ventas-periodo-${startDate}-a-${endDate}.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
    
    console.log(`Reporte CSV exportado: reporte-ventas-periodo-${startDate}-a-${endDate}.csv`);
    console.log(`Total de productos en el reporte: ${data.productBreakdown.length}`);
    console.log(`Ventas totales del período: ${data.totalSales}`);
  };

  const getTrophyIcon = (index: number) => {
    switch (index) {
      case 0: return <CrownIcon className="w-5 h-5 text-yellow-500" />;
      case 1: return <div className="w-5 h-5 rounded-full bg-gray-400 flex items-center justify-center text-white text-xs font-bold">2</div>;
      case 2: return <div className="w-5 h-5 rounded-full bg-orange-400 flex items-center justify-center text-white text-xs font-bold">3</div>;
      default: return <StarIcon className="w-4 h-4 text-blue-500" />;
    }
  };

  const getTrophyColor = (index: number) => {
    switch (index) {
      case 0: return 'border-yellow-300 bg-yellow-50 dark:bg-yellow-950';
      case 1: return 'border-gray-300 bg-gray-50 dark:bg-gray-800';
      case 2: return 'border-orange-300 bg-orange-50 dark:bg-orange-950';
      default: return 'border-blue-300 bg-blue-50 dark:bg-blue-950';
    }
  };

  return (
    <div className="flex flex-col gap-8">
      {/* Tarjetas de Resumen Principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card className="bg-gradient-to-br from-blue-500 to-blue-600 text-white shadow-lg hover:shadow-xl transition-all duration-300">
          <CardBody className="flex flex-row items-center gap-4 p-6">
            <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm">
              <CalendarIcon className="w-8 h-8 text-white" />
            </div>
            <div className="flex-1">
              <p className="text-blue-100 text-sm font-medium">Período Analizado</p>
              <p className="text-sm font-bold leading-tight">
                {formatDate(startDate)}
              </p>
              <p className="text-sm font-bold leading-tight">
                {formatDate(endDate)}
              </p>
            </div>
          </CardBody>
        </Card>
        
        <Card className="bg-gradient-to-br from-green-500 to-green-600 text-white shadow-lg hover:shadow-xl transition-all duration-300">
          <CardBody className="flex flex-row items-center gap-4 p-6">
            <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm">
              <TrendingUpIcon className="w-8 h-8 text-white" />
            </div>
            <div className="flex-1">
              <p className="text-green-100 text-sm font-medium">Total de Ventas</p>
              <p className="text-3xl font-bold">{data.totalSales.toLocaleString()}</p>
              <p className="text-green-200 text-xs mt-1">transacciones</p>
            </div>
          </CardBody>
        </Card>
        
        <Card className="bg-gradient-to-br from-purple-500 to-purple-600 text-white shadow-lg hover:shadow-xl transition-all duration-300">
          <CardBody className="flex flex-row items-center gap-4 p-6">
            <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm">
              <DollarSignIcon className="w-8 h-8 text-white" />
            </div>
            <div className="flex-1">
              <p className="text-purple-100 text-sm font-medium">Ingresos Totales</p>
              <p className="text-2xl font-bold">{formatCurrency(data.totalRevenue)}</p>
              <p className="text-purple-200 text-xs mt-1">en el período</p>
            </div>
          </CardBody>
        </Card>
        
        <Card className="bg-gradient-to-br from-orange-500 to-orange-600 text-white shadow-lg hover:shadow-xl transition-all duration-300">
          <CardBody className="flex flex-row items-center gap-4 p-6">
            <div className="flex items-center justify-center w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm">
              <span className="text-white font-bold text-2xl">Ø</span>
            </div>
            <div className="flex-1">
              <p className="text-orange-100 text-sm font-medium">Promedio por Venta</p>
              <p className="text-2xl font-bold">{formatCurrency(averageRevenuePerSale)}</p>
              <p className="text-orange-200 text-xs mt-1">por transacción</p>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Top 5 Productos Mejorado */}
      <Card className="bg-gradient-to-br from-white to-yellow-50 dark:from-gray-800 dark:to-yellow-950 shadow-xl border border-yellow-200 dark:border-yellow-800">
        <CardHeader className="bg-gradient-to-r from-yellow-500 to-orange-500 text-white rounded-t-xl p-6">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
              <CrownIcon className="w-6 h-6 text-white" />
            </div>
            <div>
              <h3 className="text-xl font-bold">🏆 Top 5 Productos por Ingresos</h3>
              <p className="text-yellow-100 mt-1">Los mejores productos del período</p>
            </div>
          </div>
        </CardHeader>
        <CardBody className="p-6">
          <div className="space-y-6">
            {topProducts.map((product, index) => {
              const percentage = (product.totalRevenue / data.totalRevenue) * 100;
              return (
                <div 
                  key={product.productId} 
                  className={`p-4 rounded-xl border-2 ${getTrophyColor(index)} transition-all duration-300 hover:shadow-md`}
                >
                  <div className="flex justify-between items-start mb-3">
                    <div className="flex items-center gap-3">
                      {getTrophyIcon(index)}
                      <div>
                        <span className="font-bold text-lg text-gray-800 dark:text-white">
                          {product.productName}
                        </span>
                        <p className="text-sm text-gray-600 dark:text-gray-400">
                          ID: {product.productId}
                        </p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-xl text-green-600 dark:text-green-400">
                        {formatCurrency(product.totalRevenue)}
                      </p>
                      <p className="text-sm text-gray-600 dark:text-gray-400">
                        {product.totalQuantitySold.toLocaleString()} unidades
                      </p>
                    </div>
                  </div>
                  <div className="space-y-2">
                    <div className="flex justify-between text-sm">
                      <span className="text-gray-600 dark:text-gray-400">
                        Contribución a ingresos totales
                      </span>
                      <span className="font-semibold text-gray-800 dark:text-white">
                        {percentage.toFixed(1)}%
                      </span>
                    </div>
                    <Progress 
                      value={percentage} 
                      color={index === 0 ? "warning" : index === 1 ? "default" : index === 2 ? "secondary" : "primary"}
                      className="max-w-full"
                      size="md"
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </CardBody>
      </Card>

      {/* Desglose Completo de Productos */}
      <Card className="bg-white dark:bg-gray-800 shadow-xl border border-gray-200 dark:border-gray-700">
        <CardHeader className="bg-gradient-to-r from-indigo-500 to-purple-600 text-white rounded-t-xl p-6">
          <div className="flex justify-between items-center w-full">
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                <TrendingUpIcon className="w-6 h-6 text-white" />
              </div>
              <div>
                <h3 className="text-xl font-bold">Desglose Completo por Producto</h3>
                <p className="text-indigo-100 mt-1">
                  Todos los productos vendidos del {formatDate(startDate)} al {formatDate(endDate)}
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
          {data.productBreakdown.length > 0 ? (
            <div className="overflow-x-auto">
              <Table 
                aria-label="Tabla de desglose de productos"
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
                  <TableColumn className="text-center">% DEL TOTAL</TableColumn>
                </TableHeader>
                <TableBody>
                  {data.productBreakdown.map((item, index) => {
                    const percentage = (item.totalRevenue / data.totalRevenue) * 100;
                    return (
                      <TableRow 
                        key={item.productId}
                        className="hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                      >
                        <TableCell>
                          <div className="flex items-center gap-3">
                            <div className="w-10 h-10 bg-gradient-to-br from-indigo-400 to-purple-500 rounded-lg flex items-center justify-center text-white font-bold text-sm">
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
                            <span className="font-bold text-lg text-blue-600 dark:text-blue-400">
                              {item.totalQuantitySold.toLocaleString()}
                            </span>
                            <p className="text-xs text-gray-500">unidades</p>
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
                          <div className="flex items-center justify-center gap-3">
                            <Progress 
                              value={percentage} 
                              color="primary" 
                              className="max-w-20"
                              size="sm"
                            />
                            <span className="text-sm font-semibold min-w-12 text-gray-700 dark:text-gray-300">
                              {percentage.toFixed(1)}%
                            </span>
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
                <TrendingUpIcon className="w-12 h-12 text-gray-400" />
              </div>
              <h3 className="text-lg font-semibold text-gray-700 dark:text-gray-300 mb-2">
                No hay datos de productos
              </h3>
              <p className="text-gray-500 dark:text-gray-400">
                No se encontraron datos de productos para el rango de fechas seleccionado.
              </p>
            </div>
          )}
        </CardBody>
      </Card>

      {/* Estadísticas Adicionales del Período */}
      {data.productBreakdown.length > 0 && (
        <Card className="bg-gradient-to-br from-gray-50 to-indigo-50 dark:from-gray-800 dark:to-indigo-950 border border-gray-200 dark:border-gray-700">
          <CardHeader>
            <h3 className="text-lg font-semibold text-gray-800 dark:text-white flex items-center gap-2">
              📈 Análisis del Período
            </h3>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
              <div className="text-center p-4 bg-white dark:bg-gray-700 rounded-xl shadow-sm">
                <p className="text-3xl font-bold text-blue-600 dark:text-blue-400">
                  {data.productBreakdown.length}
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-400 mt-1">
                  Productos diferentes vendidos
                </p>
              </div>
              <div className="text-center p-4 bg-white dark:bg-gray-700 rounded-xl shadow-sm">
                <p className="text-3xl font-bold text-green-600 dark:text-green-400">
                  {formatCurrency(Math.max(...data.productBreakdown.map(item => item.totalRevenue)))}
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-400 mt-1">
                  Mayor ingreso individual
                </p>
              </div>
              <div className="text-center p-4 bg-white dark:bg-gray-700 rounded-xl shadow-sm">
                <p className="text-3xl font-bold text-purple-600 dark:text-purple-400">
                  {Math.max(...data.productBreakdown.map(item => item.totalQuantitySold)).toLocaleString()}
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-400 mt-1">
                  Mayor cantidad vendida
                </p>
              </div>
              <div className="text-center p-4 bg-white dark:bg-gray-700 rounded-xl shadow-sm">
                <p className="text-3xl font-bold text-orange-600 dark:text-orange-400">
                  {(data.productBreakdown.reduce((sum, item) => sum + item.totalQuantitySold, 0) / data.productBreakdown.length).toFixed(0)}
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-400 mt-1">
                  Promedio de unidades por producto
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
}