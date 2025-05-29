import { Card, CardBody, CardHeader } from "@heroui/card";
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@heroui/table";
import { Button } from "@heroui/button";
import { Progress } from "@heroui/progress";
import { DownloadIcon, CalendarIcon, TrendingUpIcon, DollarSignIcon } from "lucide-react";
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

  const exportToCSV = () => {
    const summaryData = [
      ['Metric', 'Value'],
      ['Date Range', `${startDate} to ${endDate}`],
      ['Total Sales', data.totalSales.toString()],
      ['Total Revenue', data.totalRevenue.toFixed(2)],
      ['Average Revenue per Sale', averageRevenuePerSale.toFixed(2)],
      [''],
      ['Product Breakdown'],
      ['Product Name', 'Quantity Sold', 'Total Revenue', 'Average Price'],
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
    a.download = `date-range-sales-report-${startDate}-to-${endDate}.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
  };

  return (
    <div className="flex flex-col gap-6">
      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardBody className="flex flex-row items-center gap-3">
            <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-primary/10">
              <CalendarIcon className="w-6 h-6 text-primary" />
            </div>
            <div>
              <p className="text-sm text-gray-500">Date Range</p>
              <p className="text-sm font-medium">
                {new Date(startDate).toLocaleDateString()} - {new Date(endDate).toLocaleDateString()}
              </p>
            </div>
          </CardBody>
        </Card>
        
        <Card>
          <CardBody className="flex flex-row items-center gap-3">
            <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-success/10">
              <TrendingUpIcon className="w-6 h-6 text-success" />
            </div>
            <div>
              <p className="text-sm text-gray-500">Total Sales</p>
              <p className="text-2xl font-bold">{data.totalSales}</p>
            </div>
          </CardBody>
        </Card>
        
        <Card>
          <CardBody className="flex flex-row items-center gap-3">
            <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-warning/10">
              <DollarSignIcon className="w-6 h-6 text-warning" />
            </div>
            <div>
              <p className="text-sm text-gray-500">Total Revenue</p>
              <p className="text-2xl font-bold">${data.totalRevenue.toFixed(2)}</p>
            </div>
          </CardBody>
        </Card>
        
        <Card>
          <CardBody className="flex flex-row items-center gap-3">
            <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-secondary/10">
              <span className="text-secondary font-bold text-lg">Ø</span>
            </div>
            <div>
              <p className="text-sm text-gray-500">Avg per Sale</p>
              <p className="text-2xl font-bold">${averageRevenuePerSale.toFixed(2)}</p>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Top Products */}
      <Card>
        <CardHeader>
          <h3 className="text-lg font-semibold">Top 5 Products by Revenue</h3>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            {topProducts.map((product, index) => {
              const percentage = (product.totalRevenue / data.totalRevenue) * 100;
              return (
                <div key={product.productId} className="space-y-2">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center gap-3">
                      <span className="flex items-center justify-center w-6 h-6 rounded-full bg-primary text-white text-xs font-bold">
                        {index + 1}
                      </span>
                      <span className="font-medium">{product.productName}</span>
                    </div>
                    <div className="text-right">
                      <p className="font-semibold">${product.totalRevenue.toFixed(2)}</p>
                      <p className="text-sm text-gray-500">{product.totalQuantitySold} units</p>
                    </div>
                  </div>
                  <Progress 
                    value={percentage} 
                    color="primary" 
                    className="max-w-full"
                    label={`${percentage.toFixed(1)}% of total revenue`}
                  />
                </div>
              );
            })}
          </div>
        </CardBody>
      </Card>

      {/* Detailed Product Breakdown */}
      <Card>
        <CardHeader className="flex justify-between items-center">
          <div>
            <h3 className="text-lg font-semibold">Complete Product Breakdown</h3>
            <p className="text-sm text-gray-500">
              All products sold from {new Date(startDate).toLocaleDateString()} to {new Date(endDate).toLocaleDateString()}
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
          <Table aria-label="Product breakdown table">
            <TableHeader>
              <TableColumn>PRODUCT</TableColumn>
              <TableColumn>QUANTITY SOLD</TableColumn>
              <TableColumn>TOTAL REVENUE</TableColumn>
              <TableColumn>AVERAGE PRICE</TableColumn>
              <TableColumn>% OF TOTAL REVENUE</TableColumn>
            </TableHeader>
            <TableBody>
              {data.productBreakdown.map((item) => {
                const percentage = (item.totalRevenue / data.totalRevenue) * 100;
                return (
                  <TableRow key={item.productId}>
                    <TableCell>
                      <div>
                        <p className="font-medium">{item.productName}</p>
                        <p className="text-sm text-gray-500">ID: {item.productId}</p>
                      </div>
                    </TableCell>
                    <TableCell>
                      <span className="font-medium">{item.totalQuantitySold} units</span>
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
                      <div className="flex items-center gap-2">
                        <Progress 
                          value={percentage} 
                          color="primary" 
                          className="max-w-20"
                          size="sm"
                        />
                        <span className="text-sm">{percentage.toFixed(1)}%</span>
                      </div>
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
          
          {data.productBreakdown.length === 0 && (
            <div className="text-center py-8">
              <p className="text-gray-500">No product sales data found for the selected date range.</p>
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
}