import { useState } from "react";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { NumberInput } from "@heroui/number-input";
import { useDisclosure } from "@heroui/use-disclosure";
import {Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@heroui/modal";
import { PlusIcon } from "lucide-react";

interface Product {
  id: number;
  barcode: string;
  name: string;
  description: string;
  price: number;
  wholesale: number;
  retail: number;
  stock: number;
  minStock: number;
}

export default function AddProductsModal() {
  const {isOpen, onOpen, onOpenChange} = useDisclosure();
  const [formData, setFormData] = useState<Partial<Product>>({
    barcode: "",
    name: "",
    description: "",
    price: undefined,
    wholesale: undefined,
    retail: undefined,
    stock: undefined,
    minStock: undefined
  });

  const handleSubmit = (onClose: () => void) => {
    const newProduct: Product = {
      id: Date.now(), // Temporary ID generation
      ...formData as Omit<Product, "id">
    };
    console.log("New Product:", newProduct);
    onClose();
    setFormData({
      barcode: "",
      name: "",
      description: "",
      price: undefined,
      wholesale: undefined,
      retail: undefined,
      stock: undefined,
      minStock: undefined
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
                  />
                  <Input
                    label="Nombre"
                    placeholder="Ingresa el nombre del producto"
                    value={formData.name}
                    onValueChange={(value) => setFormData({...formData, name: value})}
                    isRequired
                    isClearable
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
                    />
                    <NumberInput
                      label="Precio Mayoreo"
                      placeholder="0.00"
                      value={formData.wholesale}
                      onValueChange={(value) => setFormData({...formData, wholesale: value})}
                      formatOptions={{
                        style: 'currency',
                        currency: 'MXN'
                      }}
                      isRequired
                      isClearable
                      hideStepper
                      minValue={0}
                    />
                    <NumberInput
                      label="Precio de Venta"
                      placeholder="0.00"
                      value={formData.retail}
                      onValueChange={(value) => setFormData({...formData, retail: value})}
                      formatOptions={{
                        style: 'currency',
                        currency: 'MXN'
                      }}
                      isRequired
                      isClearable
                      hideStepper
                      minValue={0}
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
                    />
                    <NumberInput
                      label="Stock Mínimo"
                      placeholder="0"
                      value={formData.minStock}
                      onValueChange={(value) => setFormData({...formData, minStock: value})}
                      isRequired
                      isClearable
                      hideStepper
                      minValue={0}
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