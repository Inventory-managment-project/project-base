"use client";

import { title } from "@/components/misc/primitives";
import {
  Table,
  TableHeader,
  TableColumn,
  TableBody,
  TableRow,
  TableCell
} from "@heroui/table";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { Dropdown, DropdownTrigger, DropdownMenu, DropdownItem } from "@heroui/dropdown";
import { useState, useMemo, useCallback } from "react";
import { PlusIcon, Trash2Icon, ShoppingCartIcon, ChevronDownIcon } from "lucide-react";
import { Product, products } from "./Products"; // Importamos el tipo Product y los productos de ejemplo

// Definimos el tipo CartItem para los elementos en el carrito
type CartItem = {
  id: number;
  productId: number;
  name: string;
  price: number;
  quantity: number;
  total: number;
};

// Definimos el tipo de pago
type PaymentMethod = "CASH" | "CARD";

// Definimos la estructura para el envío al backend
type SalePayload = {
  id: number;
  total: string;
  paymentmethod: PaymentMethod;
  created: number;
  products: Array<{
    first: number;  // id de producto
    second: string; // cantidad vendida
  }>;
  subtotal: string;
};

const SaleForm = () => {
  const [productId, setProductId] = useState("");
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>("CASH");
  const [isProcessing, setIsProcessing] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  // Calcular subtotal y total
  const { subtotal, total } = useMemo(() => {
    const subtotalValue = cartItems.reduce((sum, item) => sum + item.total, 0);
    return {
      subtotal: subtotalValue,
      total: subtotalValue // En este caso son iguales, pero podrías añadir impuestos si es necesario
    };
  }, [cartItems]);

  // Función para agregar un producto al carrito
  const addProductToCart = useCallback(() => {
    if (!productId.trim()) {
      setErrorMessage("Por favor ingrese un ID de producto");
      return;
    }

    const product = products.find((p) => p.id === parseInt(productId));
    
    if (!product) {
      setErrorMessage("Producto no encontrado");
      return;
    }

    setErrorMessage("");
    
    // Verificar si el producto ya está en el carrito
    const existingItemIndex = cartItems.findIndex(item => item.productId === product.id);
    
    if (existingItemIndex >= 0) {
      // Si ya existe, actualizamos la cantidad
      const updatedItems = [...cartItems];
      updatedItems[existingItemIndex].quantity += 1;
      updatedItems[existingItemIndex].total = updatedItems[existingItemIndex].quantity * updatedItems[existingItemIndex].price;
      setCartItems(updatedItems);
    } else {
      // Si no existe, agregamos un nuevo item
      const newItem: CartItem = {
        id: cartItems.length + 1,
        productId: product.id,
        name: product.name,
        price: product.retail, // Usamos el precio de menudeo
        quantity: 1,
        total: product.retail
      };
      setCartItems([...cartItems, newItem]);
    }
    
    // Limpiar el campo de entrada
    setProductId("");
  }, [productId, cartItems]);

  // Función para eliminar un producto del carrito
  const removeCartItem = useCallback((itemId: number) => {
    setCartItems(cartItems.filter(item => item.id !== itemId));
  }, [cartItems]);

  // Función para manejar cambios en la cantidad de un producto
  const handleQuantityChange = useCallback((itemId: number, newQuantity: number) => {
    if (newQuantity <= 0) return;
    
    setCartItems(cartItems.map(item => {
      if (item.id === itemId) {
        return {
          ...item,
          quantity: newQuantity,
          total: newQuantity * item.price
        };
      }
      return item;
    }));
  }, [cartItems]);

  // Función para procesar la venta
  const processSale = useCallback(async () => {
    if (cartItems.length === 0) {
      setErrorMessage("No hay productos en el carrito");
      return;
    }

    setIsProcessing(true);
    
    const payload: SalePayload = {
      id: 1, // Valor arbitrario que el backend ignorará
      total: total.toFixed(2),
      paymentmethod: paymentMethod,
      created: Date.now(),
      products: cartItems.map(item => ({
        first: item.productId,
        second: item.quantity.toFixed(4)
      })),
      subtotal: subtotal.toFixed(2)
    };

    try {
      const response = await fetch("../pages/api/sale.ts", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        // Limpiar el carrito después de una venta exitosa
        setCartItems([]);
        setErrorMessage("");
        alert("Venta completada con éxito");
      } else {
        setErrorMessage("Error al procesar la venta");
      }
    } catch (error) {
      console.error("Error en la petición", error);
      setErrorMessage("Error de conexión");
    } finally {
      setIsProcessing(false);
    }
  }, [cartItems, total, subtotal, paymentMethod]);

  // Manejar la tecla Enter para añadir productos rápidamente
  const handleKeyPress = useCallback((e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      addProductToCart();
    }
  }, [addProductToCart]);

  return (
    <div className="flex flex-col gap-6">
      <h1 className={title()}>Punto de Venta</h1>
      
      {/* Formulario de búsqueda de producto */}
      <div className="flex gap-2 items-end">
        <div className="flex-grow">
          <Input
            label="ID del Producto"
            placeholder="Ingrese ID del producto..."
            value={productId}
            onValueChange={setProductId}
            onKeyPress={handleKeyPress}
            type="number"
          />
        </div>
        <Button 
          color="primary" 
          endContent={<PlusIcon />}
          onPress={addProductToCart}
        >
          Agregar
        </Button>
      </div>
      
      {errorMessage && (
        <div className="text-danger text-sm">{errorMessage}</div>
      )}
      
      {/* Tabla de productos en el carrito */}
      <div className="border rounded-lg shadow">
        <Table
          aria-label="Carrito de compras"
          classNames={{
            wrapper: "min-h-[200px]",
          }}
        >
          <TableHeader>
            <TableColumn>PRODUCTO</TableColumn>
            <TableColumn>PRECIO</TableColumn>
            <TableColumn>CANTIDAD</TableColumn>
            <TableColumn>TOTAL</TableColumn>
            <TableColumn align="center">ACCIONES</TableColumn>
          </TableHeader>
          <TableBody emptyContent="No hay productos en el carrito" items={cartItems}>
            {(item: CartItem) => (
              <TableRow key={item.id}>
                <TableCell>{item.name}</TableCell>
                <TableCell>${item.price.toFixed(2)}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-2">
                    <Button 
                      size="sm" 
                      isIconOnly 
                      variant="flat"
                      onPress={() => handleQuantityChange(item.id, item.quantity - 1)}
                      isDisabled={item.quantity <= 1}
                    >
                      -
                    </Button>
                    <span>{item.quantity}</span>
                    <Button 
                      size="sm" 
                      isIconOnly 
                      variant="flat"
                      onPress={() => handleQuantityChange(item.id, item.quantity + 1)}
                    >
                      +
                    </Button>
                  </div>
                </TableCell>
                <TableCell>${item.total.toFixed(2)}</TableCell>
                <TableCell>
                  <div className="flex justify-center">
                    <Button 
                      isIconOnly 
                      size="sm" 
                      color="danger" 
                      variant="light"
                      onPress={() => removeCartItem(item.id)}
                    >
                      <Trash2Icon />
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      
      {/* Sección de pago */}
      <div className="flex flex-col md:flex-row gap-6 mt-4">
        {/* Método de pago usando Dropdown en lugar de RadioGroup */}
        <div className="flex-1 border rounded-lg p-4 shadow">
          <h2 className="text-xl font-semibold mb-4">Método de Pago</h2>
          <Dropdown>
            <DropdownTrigger>
              <Button 
                variant="flat" 
                endContent={<ChevronDownIcon className="text-small" />}
              >
                {paymentMethod === "CASH" ? "Efectivo" : "Tarjeta"}
              </Button>
            </DropdownTrigger>
            <DropdownMenu 
              aria-label="Método de Pago"
              onAction={(key) => setPaymentMethod(key as PaymentMethod)}
            >
              <DropdownItem key="CASH">Efectivo</DropdownItem>
              <DropdownItem key="CARD">Tarjeta</DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </div>
        
        <div className="flex-1 border rounded-lg p-4 shadow">
          <h2 className="text-xl font-semibold mb-4">Resumen</h2>
          <div className="flex justify-between mb-2">
            <span>Subtotal:</span>
            <span>${subtotal.toFixed(2)}</span>
          </div>
          <div className="flex justify-between font-bold text-lg mt-4 pt-2 border-t">
            <span>Total:</span>
            <span>${total.toFixed(2)}</span>
          </div>
        </div>
      </div>
      
      {/* Botón de procesar venta */}
      <div className="flex justify-end mt-4">
        <Button 
          size="lg" 
          color="success" 
          endContent={<ShoppingCartIcon />}
          onPress={processSale}
          isLoading={isProcessing}
          isDisabled={cartItems.length === 0}
        >
          Procesar Venta
        </Button>
      </div>
    </div>
  );
};

export default SaleForm;