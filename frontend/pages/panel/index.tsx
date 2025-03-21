import React, { useState } from "react";
import PanelLayout from "@/layouts/panel";
import SideMenu from "@/components/sideMenu";

// Componente para el formulario de venta
const SalesForm: React.FC = () => {
  const [paymentMethod, setPaymentMethod] = useState("CASH");
  const [subtotal, setSubtotal] = useState("0.00");
  const [total, setTotal] = useState("0.00");
  const [products, setProducts] = useState<{ id: number; quantity: string }[]>([]);
  const [content, setContent] = useState<JSX.Element | null>(null);

  // Agregar una nueva fila para producto
  const addProductRow = () => {
    setProducts([...products, { id: 0, quantity: "" }]);
  };

  // Actualizar datos de un producto en la lista
  const updateProduct = (index: number, key: "id" | "quantity", value: string) => {
    const newProducts = [...products];
    if (key === "id") {
      newProducts[index].id = Number(value);
    } else {
      newProducts[index].quantity = value;
    }
    setProducts(newProducts);
  };

  // Eliminar una fila de producto
  const removeProduct = (index: number) => {
    const newProducts = products.filter((_, i) => i !== index);
    setProducts(newProducts);
  };

  // Manejo del envío del formulario
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const venta = {
      id: 0, // El backend reasigna este valor
      total,
      paymentmethod: paymentMethod,
      created: Date.now(), // Fecha actual en milisegundos
      products: products.map(p => ({ first: p.id, second: p.quantity })),
      subtotal
    };

    console.log("Venta a enviar:", venta);
    alert("Venta registrada. Revisa la consola del navegador para ver el objeto generado.");
    // Aquí se puede agregar la lógica para enviar la venta al backend usando fetch o axios
  };

  return (
    <div className="form-container p-4">
      <h2 className="text-2xl font-bold mb-4">Registrar Venta</h2>
      <form onSubmit={handleSubmit}>
        {/* Selección del método de pago */}
        <div className="mb-4">
          <label htmlFor="paymentMethod" className="block mb-2">Método de Pago:</label>
          <select
            id="paymentMethod"
            value={paymentMethod}
            onChange={(e) => setPaymentMethod(e.target.value)}
            className="border p-2"
          >
            <option value="CASH">Efectivo (CASH)</option>
            <option value="CARD">Tarjeta (CARD)</option>
          </select>
        </div>

        {/* Sección de productos */}
        <div className="mb-4">
          <h3 className="text-xl font-semibold mb-2">Productos</h3>
          {products.map((product, index) => (
            <div key={index} className="flex items-center mb-2">
              <input
                type="number"
                placeholder="ID del producto"
                value={product.id || ""}
                onChange={(e) => updateProduct(index, "id", e.target.value)}
                className="border p-2 mr-2"
                required
              />
              <input
                type="text"
                placeholder="Cantidad (ej: 10.0000)"
                value={product.quantity}
                onChange={(e) => updateProduct(index, "quantity", e.target.value)}
                className="border p-2 mr-2"
                required
              />
              <button
                type="button"
                onClick={() => removeProduct(index)}
                className="bg-red-500 text-white p-2"
              >
                Eliminar
              </button>
            </div>
          ))}
          <button
            type="button"
            onClick={addProductRow}
            className="bg-blue-500 text-white p-2 mt-2"
          >
            Agregar Producto
          </button>
        </div>

        {/* Campos para subtotal y total */}
        <div className="mb-4">
          <label htmlFor="subtotal" className="block mb-2">Subtotal:</label>
          <input
            type="text"
            id="subtotal"
            value={subtotal}
            onChange={(e) => setSubtotal(e.target.value)}
            placeholder="0.00"
            className="border p-2"
            required
          />
        </div>
        <div className="mb-4">
          <label htmlFor="total" className="block mb-2">Total:</label>
          <input
            type="text"
            id="total"
            value={total}
            onChange={(e) => setTotal(e.target.value)}
            placeholder="0.00"
            className="border p-2"
            required
          />
        </div>

        <button type="submit" className="bg-green-500 text-white p-2">
          Registrar Venta
        </button>
      </form>
    </div>
  );
};

export default function PanelPage() {
  // Se inicializa el contenido con el formulario de venta
  const [content, setContent] = useState<JSX.Element | null>(<SalesForm />);

  return (
    <PanelLayout>
      <SideMenu setContent={setContent} />
      <div className="content p-4">{content}</div>
    </PanelLayout>
  );
}
