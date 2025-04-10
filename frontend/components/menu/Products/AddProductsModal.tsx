import { useState } from "react";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { NumberInput } from "@heroui/number-input";
import { useDisclosure } from "@heroui/use-disclosure";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@heroui/modal";
import { PlusIcon } from "lucide-react";
import { Product } from "./Products";
import { useSelectedStore } from "@/context/SelectedStoreContext";
import { useStatusAlerts } from "@/hooks/useStatusAlerts";
import StatusAlertsStack from "@/components/misc/StatusAlertStack";

export default function AddProductsModal({ onProductAdded }: { onProductAdded: (product: Product) => void }) {
  const {isOpen, onOpen, onOpenChange} = useDisclosure();
  const { alerts, triggerAlert, removeAlert } = useStatusAlerts();

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
  const { selectedStoreString } = useSelectedStore();

  const postProduct = async (product: Product) => {
    try {
      const res = await fetch(`http://localhost:8080/stores/${selectedStoreString}/product`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(product),
      });
      const status = res.status;
      if (status === 201) {
        const data = await res.json();
        product.id = data.id;
        triggerAlert("Producto creado con éxito", `El producto ${product.name} ha sido creado correctamente.`, 201);
        onProductAdded(product);
      } else {
        triggerAlert("Error al crear el producto", "No se pudo crear el producto. Inténtalo de nuevo.", 500);
      }
    } catch (error) {
      console.error("Error al crear el producto:", error);
      triggerAlert("Error al crear el producto", "No se pudo crear el producto. Inténtalo de nuevo.", 500);
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

    if (!selectedStoreString) {
      console.error("No se ha seleccionado una tienda");
      return;
    }

    const newProduct: Product = {
      id: Math.floor(Math.random() * 1000) + 1, 
      createdAt: Date.now(),
      storeId: Number(selectedStoreString),
      name: formData.name || "",
      description: formData.description || "",
      price: formData.price || 0,
      barcode: formData.barcode || "",
      wholesalePrice: formData.wholesalePrice || 0,
      retailPrice: formData.retailPrice || 0,
      stock: formData.stock || 0,
      minAllowStock: formData.minAllowStock || 0
    };
    postProduct(newProduct);
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
      <StatusAlertsStack alerts={alerts} onClose={removeAlert} />
    </div>
  );
}