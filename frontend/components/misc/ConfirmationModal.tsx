import { ReactNode, useState } from "react";
import { Button } from "@heroui/button";
import { useDisclosure } from "@heroui/use-disclosure";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@heroui/modal";
import { PlusIcon, Trash2Icon } from "lucide-react";

export default function ConfirmationModal({ onConfirm, header, body, icon }: { onConfirm: () => void, header: ReactNode, body: ReactNode, icon?: ReactNode }) {
  const {isOpen, onOpen, onOpenChange} = useDisclosure();

  return (
    <div className="flex flex-col items-center">
      <Button isIconOnly size="sm" variant="light" onPress={onOpen}>
        {icon || <Trash2Icon className="text-danger"/>}
      </Button>
      <Modal 
        isOpen={isOpen} 
        onOpenChange={onOpenChange}
        size="2xl"
      >
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">{header}</ModalHeader>
              <ModalBody>
                {body}
              </ModalBody>
              <ModalFooter>
              <Button color="danger" variant="light" onPress={onClose}>
                  Cancelar
                </Button>
                <Button color="secondary" onPress={() => onConfirm()}>
                  Confirmar
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </div>
  );
}