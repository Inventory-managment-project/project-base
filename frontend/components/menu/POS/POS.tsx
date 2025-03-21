import { useDisclosure } from "@heroui/use-disclosure";
import { ProductList } from "./ProductList";
import { TotalDisplay } from "./TotalDisplay";
import { PaymentModal } from "./PaymentModal";
import { useState } from "react";
import { BarcodeIcon, PlusIcon, CheckCircleIcon, CircleXIcon } from "lucide-react";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { ProductPOS } from "@/types/product";
import { useEffect } from "react";

export default function POS() {
  const [products, setProducts] = useState<ProductPOS[]>([]);
  const [barcode, setBarcode] = useState("");
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  useEffect(() => {
    const savedProducts = localStorage.getItem("products") || "";
    if (savedProducts) setProducts(JSON.parse(savedProducts));
  }, []);

  useEffect(() => {
    localStorage.setItem('products', JSON.stringify(products));
  }, [products]);

  const handleScan = () => {
    if (!barcode.trim()) return;
    
    const newProduct: ProductPOS = {
      id: Math.floor(Math.random() * 100) + 1,
      name: `Producto ${products.length + 1}`,
      price: Math.floor(Math.random() * 100) + 1,
      quantity: 1,
    };

    setProducts([...products, newProduct]);
    setBarcode("");
  };

  const handleRemoveProduct = (id: number) => {
    setProducts(products.filter(product => product.id !== id));
  };

  const handleFinishSale = () => {
    setProducts([]);
    onOpenChange();
  };

  const total = products.reduce((sum, product) => sum + (product.price * product.quantity), 0);

  return (
    <div className="w-full h-full mx-auto p-4">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="md:col-span-2">
          <div className="flex gap-2">
            <Input
              placeholder="Escanear código de barras"
              value={barcode}
              onValueChange={setBarcode}
              onKeyPress={(e) => e.key === "Enter" && handleScan()}
              startContent={<BarcodeIcon className="text-default-400" />}
              className="flex-1"
            />
            <Button color="secondary" onPress={handleScan}>
              <PlusIcon className="h-5 w-5" />
            </Button>
          </div>
          <div className="mt-8">
            <ProductList  products={products} onRemoveProduct={handleRemoveProduct} />
          </div>
        </div>
        <div className="flex flex-col gap-4">
          <TotalDisplay total={total} />
          <Button 
            color="success" 
            size="lg"
            onPress={onOpen}
            isDisabled={products.length === 0}
            startContent={<CheckCircleIcon className="h-5 w-5" />}
          >
            Finalizar Venta
          </Button>
          {/*  
          <Button 
            color="danger" 
            size="lg"
            onPress={onOpen}
            isDisabled={products.length === 0}
            startContent={<CircleXIcon className="h-5 w-5" />}
          >
            Cancelar Venta
          </Button>
          */}
        </div>
      </div>

      <PaymentModal 
        isOpen={isOpen} 
        onOpenChange={onOpenChange} 
        total={total}
        onFinishSale={handleFinishSale}
      />
    </div>
  );
}