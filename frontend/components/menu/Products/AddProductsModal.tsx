import { useState } from "react";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { NumberInput } from "@heroui/number-input";
import { useDisclosure } from "@heroui/use-disclosure";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@heroui/modal";
import { PlusIcon } from "lucide-react";
import { Product } from "./Products";

export default function AddProductsModal({ onProductAdded }: { onProductAdded: (product: Product) => void }) {
  const {isOpen, onOpen, onOpenChange} = useDisclosure();
  const [formData, setFormData] = useState<Partial<Product>>({
    name: "",
    description: "",
    price: undefined,
    barcode: "",
    wholesalePrice: undefined,
    retailPrice: undefined,
    stock: undefined,
    minAllowStock: undefined
  });
  const [isInvalid, setIsInvalid] = useState({
    name: false,
    price: false,
    barcode: false,
    wholesalePrice: false,
    retailPrice: false,
    stock: false,
    minAllowStock: false
  });
  const storeId = parseInt(localStorage.getItem("selectedStore") || "0");

  const postProduct = async (product: Product) => {
    try {
      const res = await fetch(`http://localhost:8080/stores/${storeId}/product`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(product),
      });
    } catch (error) {
      console.error("Error al crear el producto:", error);
    }
  }

  const handleSubmit = (onClose: () => void) => {
    const invalidFields = {
      name: !formData.name,
      price: !formData.price,
      barcode: !formData.barcode,
      wholesalePrice: !formData.wholesalePrice,
      retailPrice: !formData.retailPrice,
      stock: !formData.stock,
      minAllowStock: !formData.minAllowStock
    };
    setIsInvalid(invalidFields);

    if (Object.values(invalidFields).some(field => field)) {
      return;
    }

    if (!storeId) {
      console.error("No se ha seleccionado una tienda");
      return;
    }

    const newProduct: Product = {
      id: Math.floor(Math.random() * 1000) + 1, 
      createdAt: Date.now(),
      storeId: storeId,
      ...formData as Omit<Product, "id">
    };
    console.log("New Product:", newProduct);
    postProduct(newProduct);
    onProductAdded(newProduct);
    onClose();
    setFormData({
      name: "",
      description: "",
      price: undefined,
      barcode: "",
      wholesalePrice: undefined,
      retailPrice: undefined,
      stock: undefined,
      minAllowStock: undefined
    });
    setIsInvalid({
      name: false,
      price: false,
      barcode: false,
      wholesalePrice: false,
      retailPrice: false,
      stock: false,
      minAllowStock: false
    });
  };

  return (
    <div className="flex flex-col items-center">
      <Button color="secondary" endContent={<PlusIcon />} onPress={onOpen}>
        Agregar
      </Button>
      <Modal 
        isOpen={isOpen} 
        onOpenChange={onOpenChange}
        size="2xl"
      >
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">Agregar nuevo producto</ModalHeader>
              <ModalBody>
                <div className="flex flex-col gap-4">
                  <Input
                    label="Barcode"
                    placeholder="Ingresa el codigo de barras"
                    value={formData.barcode}
                    onValueChange={(value) => setFormData({...formData, barcode: value})}
                    isRequired
                    isClearable
                    isInvalid={isInvalid.barcode}
                  />
                  <Input
                    label="Nombre"
                    placeholder="Ingresa el nombre del producto"
                    value={formData.name}
                    onValueChange={(value) => setFormData({...formData, name: value})}
                    isRequired
                    isClearable
                    isInvalid={isInvalid.name}
                  />
                  <Input
                    label="Descripción"
                    placeholder="Ingresa una descripción para el producto"
                    value={formData.description}
                    isClearable
                    onValueChange={(value) => setFormData({...formData, description: value})}
                  />
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <NumberInput
                      label="Precio"
                      placeholder="0.00"
                      value={formData.price}
                      onValueChange={(value) => setFormData({...formData, price: value})}
                      formatOptions={{
                        style: 'currency',
                        currency: 'MXN'
                      }}
                      isRequired
                      isClearable
                      hideStepper
                      minValue={0}
                      isInvalid={isInvalid.price}
                    />
                    <NumberInput
                      label="Precio Mayoreo"
                      placeholder="0.00"
                      value={formData.wholesalePrice}
                      onValueChange={(value) => setFormData({...formData, wholesalePrice: value})}
                      formatOptions={{
                        style: 'currency',
                        currency: 'MXN'
                      }}
                      isRequired
                      isClearable
                      hideStepper
                      minValue={0}
                      isInvalid={isInvalid.wholesalePrice}
                    />
                    <NumberInput
                      label="Precio de Venta"
                      placeholder="0.00"
                      value={formData.retailPrice}
                      onValueChange={(value) => setFormData({...formData, retailPrice: value})}
                      formatOptions={{
                        style: 'currency',
                        currency: 'MXN'
                      }}
                      isRequired
                      isClearable
                      hideStepper
                      minValue={0}
                      isInvalid={isInvalid.retailPrice}
                    />
                    <NumberInput
                      label="Stock"
                      placeholder="0"
                      value={formData.stock}
                      onValueChange={(value) => setFormData({...formData, stock: value})}
                      isRequired
                      isClearable
                      hideStepper
                      minValue={0}
                      isInvalid={isInvalid.stock}
                    />
                    <NumberInput
                      label="Stock Mínimo"
                      placeholder="0"
                      value={formData.minAllowStock}
                      onValueChange={(value) => setFormData({...formData, minAllowStock: value})}
                      isRequired
                      isClearable
                      hideStepper
                      minValue={0}
                      isInvalid={isInvalid.minAllowStock}
                    />
                  </div>
                </div>
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  Cancelar
                </Button>
                <Button color="secondary" onPress={() => handleSubmit(onClose)}>
                  Agregar Producto
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </div>
  );
}