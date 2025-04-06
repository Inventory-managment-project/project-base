import { Card, CardBody } from "@heroui/card";

interface TotalDisplayProps {
  total: number;
}

export function TotalDisplay({ total }: TotalDisplayProps) {
  return (
    <div className="text-center">
      <h3 className="text-lg font-medium text-gray-600">Total a Pagar</h3>
      <p className="text-4xl font-bold text-gray-800 mt-2">${total.toFixed(2)}</p>
    </div>
  );
}