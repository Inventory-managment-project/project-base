import { Card, CardBody, CardHeader } from "@heroui/card";
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@heroui/table";
import { Chip } from "@heroui/chip";
import { Button } from "@heroui/button";
import { DownloadIcon, PackageIcon } from "lucide-react";
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

  const exportToCSV = () => {
    const headers = ['Product ID', 'Product Name', 'Quantity Sold', 'Total Revenue', 'Average Price'];
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
    a.download = `product-sales-report-${startDate}-to-${endDate}.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
  };

  return (
    <div className="flex flex-col gap-6">
      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardBody className="flex flex-row items-center gap-3">
            <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-primary/10">
              <PackageIcon className="w-6 h-6 text-primary" />
            </div>
            <div>
              <p className="text-sm text-gray-500">Total Products</p>
              <p className="text-2xl font-bold">{data.length}</p>
            </div>
          </CardBody>
        </Card>
        
        <Card>
          <CardBody className="flex flex-row items-center gap-3">
            <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-success/10">
              <span className="text-success font-bold text-lg">$</span>
            </div>
            <div>
              <p className="text-sm text-gray-500">Total Revenue</p>
              <p className="text-2xl font-bold">${totalRevenue.toFixed(2)}</p>
            </div>
          </CardBody>
        </Card>
        
        <Card>
          <CardBody className="flex flex-row items-center gap-3">
            <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-warning/10">
              <span className="text-warning font-bold text-lg">#</span>
            </div>
            <div>
              <p className="text-sm text-gray-500">Total Quantity</p>
              <p className="text-2xl font-bold">{totalQuantity}</p>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Report Table */}
      <Card>
        <CardHeader className="flex justify-between items-center">
          <div>
            <h3 className="text-lg font-semibold">Product Sales Report</h3>
            <p className="text-sm text-gray-500">
              From {new Date(startDate).toLocaleDateString()} to {new Date(endDate).toLocaleDateString()}
            </p>
          </div>
          <Button
            color="primary"
            variant="flat"
            startContent={<DownloadIcon className="w-4 h-4" />}
            onPress={exportToCSV}
          >
            Export CSV
          </Button>
        </CardHeader>
        <CardBody>
          <Table aria-label="Product sales report table">
            <TableHeader>
              <TableColumn>PRODUCT</TableColumn>
              <TableColumn>QUANTITY SOLD</TableColumn>
              <TableColumn>TOTAL REVENUE</TableColumn>
              <TableColumn>AVERAGE PRICE</TableColumn>
              <TableColumn>PERFORMANCE</TableColumn>
            </TableHeader>
            <TableBody>
              {data.map((item) => (
                <TableRow key={item.productId}>
                  <TableCell>
                    <div>
                      <p className="font-medium">{item.productName}</p>
                      <p className="text-sm text-gray-500">ID: {item.productId}</p>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Chip color="primary" variant="flat">
                      {item.totalQuantitySold} units
                    </Chip>
                  </TableCell>
                  <TableCell>
                    <span className="font-semibold text-success">
                      ${item.totalRevenue.toFixed(2)}
                    </span>
                  </TableCell>
                  <TableCell>
                    ${item.averagePrice.toFixed(2)}
                  </TableCell>
                  <TableCell>
                    <Chip 
                      color={item.totalQuantitySold > 10 ? "success" : item.totalQuantitySold > 5 ? "warning" : "danger"}
                      variant="flat"
                      size="sm"
                    >
                      {item.totalQuantitySold > 10 ? "High" : item.totalQuantitySold > 5 ? "Medium" : "Low"}
                    </Chip>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          
          {data.length === 0 && (
            <div className="text-center py-8">
              <p className="text-gray-500">No sales data found for the selected date range.</p>
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
}