import { useState } from 'react';
import { Card, CardBody, CardHeader } from '@heroui/card';
import { Button } from '@heroui/button';
import { Tabs, Tab } from '@heroui/tabs';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from '@heroui/table';
import { title, subtitle } from '@/components/misc/primitives';
import { IconWrapper } from '@/components/misc/IconWrapper';
import { FileStack, Download, Filter } from 'lucide-react';

// Interfaces para el sistema de reportes
interface SalesReport {
  id: number;
  name: string;
  date: string;
  totalSales: number;
  totalRevenue: number;
  type: 'Diario' | 'Semanal' | 'Mensual';
}

interface InventoryReport {
  id: number;
  name: string;
  date: string;
  totalProducts: number;
  totalValue: number;
  type: 'Actual' | 'Histórico';
}

const Reports = () => {
  const [activeTab, setActiveTab] = useState('sales');
  const [salesReports, setSalesReports] = useState<SalesReport[]>([
    { id: 1, name: 'Ventas Diarias', date: '2024-06-01', totalSales: 156, totalRevenue: 5678.90, type: 'Diario' },
    { id: 2, name: 'Reporte Semanal', date: '2024-05-26', totalSales: 892, totalRevenue: 32568.40, type: 'Semanal' },
    { id: 3, name: 'Ventas Mensuales', date: '2024-05-01', totalSales: 3567, totalRevenue: 129876.50, type: 'Mensual' },
    { id: 4, name: 'Ventas Diarias', date: '2024-05-31', totalSales: 178, totalRevenue: 6234.50, type: 'Diario' },
    { id: 5, name: 'Ventas Diarias', date: '2024-05-30', totalSales: 167, totalRevenue: 6045.70, type: 'Diario' },
  ]);
  
  const [inventoryReports, setInventoryReports] = useState<InventoryReport[]>([
    { id: 1, name: 'Inventario Actual', date: '2024-06-01', totalProducts: 234, totalValue: 89456.75, type: 'Actual' },
    { id: 2, name: 'Inventario Histórico', date: '2024-05-15', totalProducts: 245, totalValue: 92345.80, type: 'Histórico' },
    { id: 3, name: 'Inventario Histórico', date: '2024-05-01', totalProducts: 230, totalValue: 87123.45, type: 'Histórico' },
  ]);

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('es-MX', {
      style: 'currency',
      currency: 'MXN',
      minimumFractionDigits: 2
    }).format(amount);
  };

  const handleDownload = (reportId: number, reportType: string) => {
    alert(`Descargando reporte ${reportId} de tipo ${reportType}`);
    // Aquí iría la lógica para descargar el reporte
  };

  const generateReport = (type: string) => {
    alert(`Generando nuevo reporte de ${type}`);
    // Aquí iría la lógica para generar un nuevo reporte
  };

  return (
    <div className="flex flex-col w-full select-none h-full gap-6 p-4">
      <h1 className={title()}>Reportes</h1>
      
      <Tabs 
        aria-label="Opciones de reportes" 
        selectedKey={activeTab}
        onSelectionChange={(key) => setActiveTab(key as string)}
        className="w-full"
      >
        <Tab
          key="sales"
          title={
            <div className="flex items-center gap-2">
              <IconWrapper className="bg-secondary/10 text-secondary">
                <FileStack size={18} />
              </IconWrapper>
              <span>Ventas</span>
            </div>
          }
        >
          <Card className="mt-4">
            <CardHeader className="flex justify-between items-center">
              <h3 className={subtitle()}>Reportes de Ventas</h3>
              <div className="flex gap-2">
                <Button color="secondary" startContent={<Filter size={16} />}>
                  Filtrar
                </Button>
                <Button color="primary" onClick={() => generateReport('ventas')}>
                  Generar Reporte
                </Button>
              </div>
            </CardHeader>
            <CardBody>
              <Table aria-label="Reportes de ventas">
                <TableHeader>
                  <TableColumn>NOMBRE</TableColumn>
                  <TableColumn>FECHA</TableColumn>
                  <TableColumn>VENTAS</TableColumn>
                  <TableColumn>INGRESOS</TableColumn>
                  <TableColumn>TIPO</TableColumn>
                  <TableColumn>ACCIONES</TableColumn>
                </TableHeader>
                <TableBody>
                  {salesReports.map((report) => (
                    <TableRow key={report.id}>
                      <TableCell>{report.name}</TableCell>
                      <TableCell>{new Date(report.date).toLocaleDateString('es-MX')}</TableCell>
                      <TableCell>{report.totalSales}</TableCell>
                      <TableCell>{formatCurrency(report.totalRevenue)}</TableCell>
                      <TableCell>{report.type}</TableCell>
                      <TableCell>
                        <Button 
                          isIconOnly 
                          color="secondary" 
                          variant="light" 
                          onClick={() => handleDownload(report.id, 'ventas')}
                        >
                          <Download size={18} />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardBody>
          </Card>
        </Tab>
        <Tab
          key="inventory"
          title={
            <div className="flex items-center gap-2">
              <IconWrapper className="bg-primary/10 text-primary">
                <FileStack size={18} />
              </IconWrapper>
              <span>Inventario</span>
            </div>
          }
        >
          <Card className="mt-4">
            <CardHeader className="flex justify-between items-center">
              <h3 className={subtitle()}>Reportes de Inventario</h3>
              <div className="flex gap-2">
                <Button color="secondary" startContent={<Filter size={16} />}>
                  Filtrar
                </Button>
                <Button color="primary" onClick={() => generateReport('inventario')}>
                  Generar Reporte
                </Button>
              </div>
            </CardHeader>
            <CardBody>
              <Table aria-label="Reportes de inventario">
                <TableHeader>
                  <TableColumn>NOMBRE</TableColumn>
                  <TableColumn>FECHA</TableColumn>
                  <TableColumn>PRODUCTOS</TableColumn>
                  <TableColumn>VALOR TOTAL</TableColumn>
                  <TableColumn>TIPO</TableColumn>
                  <TableColumn>ACCIONES</TableColumn>
                </TableHeader>
                <TableBody>
                  {inventoryReports.map((report) => (
                    <TableRow key={report.id}>
                      <TableCell>{report.name}</TableCell>
                      <TableCell>{new Date(report.date).toLocaleDateString('es-MX')}</TableCell>
                      <TableCell>{report.totalProducts}</TableCell>
                      <TableCell>{formatCurrency(report.totalValue)}</TableCell>
                      <TableCell>{report.type}</TableCell>
                      <TableCell>
                        <Button 
                          isIconOnly 
                          color="secondary" 
                          variant="light" 
                          onClick={() => handleDownload(report.id, 'inventario')}
                        >
                          <Download size={18} />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardBody>
          </Card>
        </Tab>
      </Tabs>
    </div>
  );
}

export default Reports; 