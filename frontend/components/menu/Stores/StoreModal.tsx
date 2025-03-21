import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter
} from "@heroui/modal";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { useState } from "react";

interface StoreModalProps {
  isOpen: boolean;
  onOpenChange: () => void;
  onSave: (name: string, address: string) => void;
}

export const StoreModal: React.FC<StoreModalProps> = ({
  isOpen,
  onOpenChange,
  onSave,
}) => {
  const [name, setName] = useState("");
  const [address, setAddress] = useState("");

  const handleSave = () => {
    onSave(name, address);
    setName("");
    setAddress("");
  };

  return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">
              Crear Nueva Tienda
            </ModalHeader>
            <ModalBody>
              <div className="flex flex-col gap-4">
                <Input
                  label="Nombre"
                  placeholder="Ingrese el nombre de la tienda"
                  value={name}
                  onValueChange={setName}
                />
                <Input
                  label="Dirección"
                  placeholder="Ingrese la dirección de la tienda"
                  value={address}
                  onValueChange={setAddress}
                />
              </div>
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onClose}>
                Cancelar
              </Button>
              <Button color="primary" onPress={() => {
                handleSave();
                onClose();
              }}>
                Guardar
              </Button>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
  );
};