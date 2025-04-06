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

function parseProducts(data: any): Product[] {
  const productsArray = Array.isArray(data) ? data : data.products || data.data || [];
  
  return productsArray.map((item: any) => ({
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
      
      console.log("API Response:", data);
      
      setProductList(parseProducts(data));
    } catch (error) {
      console.error("Error al obtener los productos:", error);
      setProductList([]);
    }
  }

  useLayoutEffect(() => {
    fetchProducts();
  }, []);

  useEffect(() => {
    const savedProducts = localStorage.getItem("products");
    if (savedProducts) {
      try {
        setProducts(JSON.parse(savedProducts));
      } catch (error) {
        console.error("Error parsing saved products:", error);
        localStorage.removeItem("products");
      }
    }
  }, []);

  useEffect(() => {
    localStorage.setItem('products', JSON.stringify(products));
  }, [products]);

  const handleScan = () => {
    if (!barcode.trim()) return;
    const foundProduct = productList.find(product => product.barcode === barcode.trim());
    console.log(products)
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
    <div className="w-full h-full mx-auto bg-gray-50">
      <div className="p-5 bg-white shadow-sm border-b">
        <h1 className="text-2xl font-bold text-gray-800">Terminal de Venta</h1>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-3 h-[calc(100%-80px)] gap-0">
        <div className="md:col-span-2 p-5 border-r">
          <div className="flex gap-2 bg-white p-4 shadow-sm rounded-lg">
            <Input
              placeholder="Escanear código de barras"
              value={barcode}
              onValueChange={setBarcode}
              onKeyPress={(e) => e.key === "Enter" && handleScan()}
              startContent={<BarcodeIcon className="text-default-400" />}
              size="lg"
              className="flex-1"
            />
            <Button color="secondary" size="lg" onPress={handleScan}>
              <PlusIcon className="h-5 w-5" />
            </Button>
          </div>
          
          <div className="mt-6 bg-white rounded-lg shadow-sm p-4 overflow-y-auto max-h-[calc(100vh-280px)]">
            <h2 className="text-lg font-semibold mb-3 text-gray-700">Productos en carrito</h2>
            <ProductList products={products} setProducts={setProducts} onRemoveProduct={handleRemoveProduct} />
          </div>
        </div>
        
        <div className="bg-gray-100 p-6 flex flex-col">
          <div className="bg-white rounded-lg shadow-sm p-5 mb-4">
            <TotalDisplay total={total} />
          </div>
          
          <div className="mt-auto flex flex-col gap-3">
            <Button 
              color="primary" 
              size="lg"
              onPress={onOpen}
              isDisabled={products.length === 0}
              startContent={<CheckCircleIcon className="h-5 w-5" />}
              className="h-16 text-lg font-medium"
            >
              Finalizar Venta
            </Button>
            
            <Button 
              color="danger" 
              variant="flat"
              size="lg"
              onPress={() => setProducts([])}
              isDisabled={products.length === 0}
              startContent={<CircleXIcon className="h-5 w-5" />}
            >
              Cancelar Venta
            </Button>
          </div>
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