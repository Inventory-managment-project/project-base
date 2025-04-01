import { useDisclosure } from "@heroui/use-disclosure";
import { ProductList } from "./ProductList";
import { TotalDisplay } from "./TotalDisplay";
import { PaymentModal } from "./PaymentModal";
import { useState } from "react";
import { BarcodeIcon, PlusIcon, CheckCircleIcon, CircleXIcon } from "lucide-react";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { ProductPOS } from "@/types/product";
import { useEffect, useLayoutEffect } from "react";
import { Product } from "../Products/Products";
import { useSelectedStore } from "@/context/SelectedStoreContext";

export function convertToProductPOS(product: Product): ProductPOS {
  return {
    id: product.id,
    name: product.name,
    price: product.retailPrice,
    quantity: product.stock
  };
}

function parseProducts(data: any[]): Product[] {
  return data.map(item => ({
    ...item,
    price: parseFloat(item.price),
    wholesalePrice: parseFloat(item.wholesalePrice),
    retailPrice: parseFloat(item.retailPrice)
  }));
}

export default function POS() {
  const [productList, setProductList] = useState<Product[]>([]);
  const [products, setProducts] = useState<ProductPOS[]>([]);
  const [barcode, setBarcode] = useState("");
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const { selectedStoreString } = useSelectedStore();

  const fetchProducts = async () => {
    try {
      const res = await fetch(`http://localhost:8080/stores/${selectedStoreString}/products`, {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        }
      });
      const data = await res.json();
      setProductList(parseProducts(data));
    } catch (error) {
      console.error("Error al obtener los productos:", error);
    }
  }

  useLayoutEffect(() => {
    fetchProducts();
  }, []);

  useEffect(() => {
    const savedProducts = localStorage.getItem("products") || "";
    if (savedProducts) setProducts(JSON.parse(savedProducts));
  }, []);

  useEffect(() => {
    localStorage.setItem('products', JSON.stringify(products));
  }, [products]);

  const handleScan = () => {
    if (!barcode.trim()) return;
    const foundProduct = productList.find(product => product.barcode === barcode.trim());
  
    if (foundProduct) {
      const existingProductIndex = products.findIndex(p => p.id === foundProduct.id);
  
      if (existingProductIndex !== -1) {
        const updatedProducts = [...products];
        updatedProducts[existingProductIndex].quantity += 1;
        setProducts(updatedProducts);
      } else {
        const newProduct: ProductPOS = convertToProductPOS(foundProduct);
        newProduct.quantity = 1;
        setProducts([...products, newProduct]);
      }
    } else {
      console.warn(`Producto con código de barras ${barcode} no encontrado.`);
    }
  
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