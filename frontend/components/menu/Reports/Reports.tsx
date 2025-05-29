import { title, subtitle } from "@/components/misc/primitives";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { Card, CardBody, CardHeader } from "@heroui/card";
import { Divider } from "@heroui/divider";
import { useState, useEffect } from "react";
import { CalendarIcon, BarChart3Icon, TrendingUpIcon, PackageIcon } from "lucide-react";
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

export default function Reports() {
  const [activeReport, setActiveReport] = useState<'products' | 'dateRange' | null>(null);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [productReportData, setProductReportData] = useState<ProductSalesReportData[]>([]);
  const [dateRangeReportData, setDateRangeReportData] = useState<DateRangeSalesReportData | null>(null);
  const [loading, setLoading] = useState(false);
  const { selectedStoreString } = useSelectedStore();
  const { alerts, triggerAlert, removeAlert } = useStatusAlerts();


  // Set default dates (last 30 days)
  useEffect(() => {
    const today = new Date();
    const thirtyDaysAgo = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000);
    
    setEndDate(today.toISOString().split('T')[0]);
    setStartDate(thirtyDaysAgo.toISOString().split('T')[0]);
  }, []);

  const fetchProductSalesReport = async () => {
    if (!startDate || !endDate || selectedStoreString === '0') {
      triggerAlert('error', 'Please select a store and date range', 400);
      return;
    }

    setLoading(true);
    try {
      const startTimestamp = new Date(startDate).getTime();
      const endTimestamp = new Date(endDate).getTime();
      
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
        triggerAlert('success', 'Product sales report generated successfully', 200);
      } else {
        triggerAlert('error', 'Failed to generate product sales report', 400);
      }
    } catch (error) {
        triggerAlert('error', 'Error generating product sales report', 400);
    } finally {
      setLoading(false);
    }
  };

  const fetchDateRangeSalesReport = async () => {
    if (!startDate || !endDate || selectedStoreString === '0') {
        triggerAlert('error', 'Please select a store and date range', 400);
      return;
    }

    setLoading(true);
    try {
      const startTimestamp = new Date(startDate).getTime();
      const endTimestamp = new Date(endDate).getTime();
      
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
        triggerAlert('success', 'Date range sales report generated successfully', 200);
      } else {
        triggerAlert('error', 'Failed to generate date range sales report', 400);
      }
    } catch (error) {
        triggerAlert('error', 'Error generating date range sales report', 400);
    } finally {
      setLoading(false);
    }
  };

  const resetReports = () => {
    setActiveReport(null);
    setProductReportData([]);
    setDateRangeReportData(null);
  };

  if (selectedStoreString === '0') {
    return (
      <div className="flex flex-col items-center justify-center h-96">
        <h2 className={title()}>Reports</h2>
        <p className="text-gray-500 mt-4">Please select a store to view reports</p>
      </div>
    );
  }

  return (
    <div className="flex flex-col gap-6 p-6">
      <StatusAlertsStack alerts={alerts} onClose={removeAlert} />
      
      <div className="flex justify-between items-center">
        <h1 className={title()}>Sales Reports</h1>
        {activeReport && (
          <Button 
            color="secondary" 
            variant="flat" 
            onPress={resetReports}
          >
            Back to Reports
          </Button>
        )}
      </div>

      {!activeReport && (
        <>
          {/* Date Range Selection */}
          <Card>
            <CardHeader>
              <h3 className={subtitle()}>Select Date Range</h3>
            </CardHeader>
            <CardBody>
              <div className="flex gap-4 items-end">
                <Input
                  type="date"
                  label="Start Date"
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                  className="max-w-xs"
                />
                <Input
                  type="date"
                  label="End Date"
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                  className="max-w-xs"
                />
              </div>
            </CardBody>
          </Card>

          {/* Report Options */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Card className="hover:shadow-lg transition-shadow cursor-pointer">
              <CardHeader className="flex gap-3">
                <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-primary/10">
                  <PackageIcon className="w-6 h-6 text-primary" />
                </div>
                <div className="flex flex-col">
                  <h4 className="text-lg font-semibold">Product Sales Report</h4>
                  <p className="text-sm text-gray-500">View sales performance by product</p>
                </div>
              </CardHeader>
              <CardBody>
                <p className="text-sm mb-4">
                  Generate a detailed report showing sales data for each product including total quantity sold, revenue, and average price.
                </p>
                <Button 
                  color="primary" 
                  startContent={<BarChart3Icon className="w-4 h-4" />}
                  onPress={fetchProductSalesReport}
                  isLoading={loading}
                  className="w-full"
                >
                  Generate Product Report
                </Button>
              </CardBody>
            </Card>

            <Card className="hover:shadow-lg transition-shadow cursor-pointer">
              <CardHeader className="flex gap-3">
                <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-success/10">
                  <TrendingUpIcon className="w-6 h-6 text-success" />
                </div>
                <div className="flex flex-col">
                  <h4 className="text-lg font-semibold">Date Range Sales Report</h4>
                  <p className="text-sm text-gray-500">View overall sales performance</p>
                </div>
              </CardHeader>
              <CardBody>
                <p className="text-sm mb-4">
                  Generate a comprehensive report showing total sales, revenue, and product breakdown for the selected date range.
                </p>
                <Button 
                  color="success" 
                  startContent={<CalendarIcon className="w-4 h-4" />}
                  onPress={fetchDateRangeSalesReport}
                  isLoading={loading}
                  className="w-full"
                >
                  Generate Date Range Report
                </Button>
              </CardBody>
            </Card>
          </div>
        </>
      )}

      {/* Report Display */}
      {activeReport === 'products' && (
        <ProductSalesReport 
          data={productReportData} 
          startDate={startDate} 
          endDate={endDate} 
        />
      )}
      
      {activeReport === 'dateRange' && dateRangeReportData && (
        <DateRangeSalesReport 
          data={dateRangeReportData} 
          startDate={startDate} 
          endDate={endDate} 
        />
      )}
    </div>
  );
}