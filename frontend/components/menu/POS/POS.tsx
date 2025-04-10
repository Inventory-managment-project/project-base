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
import { Divider } from "@heroui/divider";
import StatusAlert from "@/components/misc/StatusAlert";

import figlet from "figlet";
import bulbhead from "figlet/importable-fonts/Bulbhead.js";
figlet.parseFont("Standard", bulbhead);

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

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertDescription, setAlertDescription] = useState("");
  const [alertStatusCode, setAlertStatusCode] = useState(0);
  const handleShowAlert = (title : string, description : string, statusCode : number) => {
    setAlertTitle(title);
    setAlertDescription(description);
    setAlertStatusCode(statusCode);
    setShowAlert(true);
    setTimeout(() => {
      setShowAlert(false);
    }, 3000);
  };

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
      setProductList([]);
    }
  }

  const postSale = async (paymentMethod: string) => {
    if (products.length === 0) return;

    const saleData = {
      id: 0,
      total: 0,
      paymentmethod: paymentMethod,
      created: 1712244000000,
      products: products.map(product => ({
        first: product.id,
        second: product.quantity
      })),
      subtotal: 0,
    };

    try {
      const res = await fetch(`http://localhost:8080/stores/${selectedStoreString}/sales`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(saleData),
      });
      return res;
    } catch (error) {
      console.error("Error al registrar la venta:", error);
      return new Response(null, { status: 500 });
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

  const printTicket = async (id: number, discount: number, paymentMethod: "CASH" | "CARD" | "TRANSFER", cashReceived : string) => {
    const MAX_LENGTH = 40;

    const getStoreDetails = async () => {
      const res = await fetch(`http://localhost:8080/stores/${selectedStoreString}`, {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        }
      });
      const data = await res.json();
      return data;
    };
    const storeDetails = await getStoreDetails();

    const centralizeText = (text: string) => {
      const padding = Math.floor((MAX_LENGTH - text.length) / 2);
      return " ".repeat(padding) + text + " ".repeat(MAX_LENGTH - text.length - padding);
    };
    const rightAlignText = (text: string) => {
      const padding = MAX_LENGTH - text.length;
      return " ".repeat(padding) + text;
    };

    const subtotal = products.reduce((sum, product) => sum + (product.price * product.quantity), 0);
    const total = subtotal - discount;

    const logo = figlet.textSync(storeDetails.name, { width: 40 });
    const ticket = 
      "=".repeat(MAX_LENGTH) + "\n" +
      logo.split("\n").map((line: string) => centralizeText(line)).join("\n") + "\n\n" +
      centralizeText(storeDetails.address) + "\n" +
      centralizeText("Tel: (000) 000-0000") + "\n" +
      centralizeText(`www.${storeDetails.name}.com`) + "\n\n" +
      "=".repeat(MAX_LENGTH) + "\n" +
      rightAlignText(`Ticket N°: ${id}`) + "\n\n" + 
      "Descripción".padEnd(14) + "Cant".padEnd(6) + "Precio".padEnd(10) + "Total\n" +
      "-".repeat(MAX_LENGTH) + "\n" +
      products.map(product => {
        const total = (product.price * product.quantity).toFixed(2);
        return `${product.name.length > 13 ? product.name.slice(0, 13).padEnd(15) : product.name.padEnd(15)}${product.quantity.toString().padEnd(5)}$${product.price.toFixed(2).padEnd(10)}$${total}`;
      }).join("\n") + "\n" +
      "-".repeat(MAX_LENGTH) + "\n" +
      rightAlignText(`Subtotal: ${subtotal.toFixed(2)}`) + "\n" +
      rightAlignText(`Descuento: $0.00`) + "\n" +
      rightAlignText(`Total: $${total.toFixed(2)}`) + "\n" +
      " \n" +
      "Método de pago: " + (paymentMethod == "CASH" ? "Efectivo" : "Tarjeta") + "\n" +
      (paymentMethod === "CASH" ? rightAlignText(`Efectivo: $${cashReceived}`) + "\n" : "") +
      (paymentMethod === "CASH" ? rightAlignText(`Cambio: $${(parseFloat(cashReceived) - total).toFixed(2)}`) + "\n" : "") +
      `${products.reduce((quantity, product) => quantity + product.quantity, 0)} producto(s)` + "\n" +
      "=".repeat(MAX_LENGTH) + "\n" +
      centralizeText("Gracias por su compra") + "\n" +
      centralizeText(`${new Date().toLocaleDateString()} - ${new Date().toLocaleTimeString()}`);
    console.log(ticket);
  };

  const handleFinishSale = async (paymentMethod: "CASH" | "CARD" | "TRANSFER", cashReceived: string) => {
    const res = await postSale(paymentMethod) ?? new Response(null, { status: 500 });
    const data = await res.json();
    if (res.status === 201) {
      handleShowAlert("Venta exitosa", "La venta se ha registrado correctamente.", 200);
      printTicket(data, 0, paymentMethod, cashReceived);
      setProducts([]);
    } else {
      handleShowAlert("Error", "No se pudo registrar la venta.", 500);
    }
    setBarcode("");
    onOpenChange();
  };

  const total = products.reduce((sum, product) => sum + (product.price * product.quantity), 0);

  return (
    <div className="w-full h-full mx-auto">
      <div className="p-4 shadow-sm">
        <h1 className="text-2xl font-bold">Terminal de Venta</h1>
      </div>
      <Divider/>
      <div className="grid grid-cols-1 md:grid-cols-3 h-[calc(100%-80px)] gap-0">
        <div className="md:col-span-2 p-5">
          <div className="flex gap-2 p-4 shadow-md rounded-lg">
            <Input
              placeholder="Escanear código de barras"
              value={barcode}
              onValueChange={setBarcode}
              onKeyPress={(e) => e.key === "Enter" && handleScan()}
              startContent={<BarcodeIcon className="text-default-400" />}
              size="lg"
              className="flex-1"
            />
            {/* handleScan */}
            <Button color="secondary" size="lg" onPress={() => printTicket(1, 0, "CASH", "0")}>
              <PlusIcon className="h-5 w-5" />
            </Button>
          </div>
          
          <div className="mt-4 rounded-lg shadow-md p-4 max-h-[calc(100vh-280px)]">
            <h2 className="text-lg font-semibold mb-2 text-right">Productos en carrito</h2>
            <ProductList products={products} setProducts={setProducts} onRemoveProduct={handleRemoveProduct} />
          </div>
        </div>
        <div className="bg-gray-100 dark:bg-zinc-900 p-6 flex flex-col border-l border-divider">
          <div className="rounded-lg bg-white dark:bg-black shadow-md p-5 mb-4">
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
      <StatusAlert
        show={showAlert}
        title={alertTitle}
        description={alertDescription}
        statusCode={alertStatusCode}
      />
    </div>
  );
}