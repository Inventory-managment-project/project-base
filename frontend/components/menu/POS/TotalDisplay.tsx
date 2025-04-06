interface TotalDisplayProps {
  total: number;
}

export function TotalDisplay({ total }: TotalDisplayProps) {
  return (
    <div className="text-center">
      <h3 className="text-lg font-medium text-gray-600 dark:text-gray-100">Total a Pagar</h3>
      <p className="text-4xl font-bold text-gray-800 dark:text-white mt-2">${total.toFixed(2)}</p>
    </div>
  );
}