import { Card, CardBody } from "@heroui/card";

interface TotalDisplayProps {
  total: number;
}

export const TotalDisplay = ({ total }: TotalDisplayProps) => {
  return (
    <Card className="bg-secondary-500">
      <CardBody className="flex flex-col items-center justify-center py-8">
        <span className="text-white text-xl mb-2">Total a Pagar</span>
        <span className="text-white text-6xl font-bold">
          ${total.toFixed(2)}
        </span>
      </CardBody>
    </Card>
  );
};