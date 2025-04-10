import { BanknoteIcon, CreditCardIcon, LandmarkIcon } from "lucide-react";
import { useMemo, useState } from "react";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { RadioGroup, Radio } from "@heroui/radio";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@heroui/modal";
import { motion } from "framer-motion";

interface PaymentModalProps {
  isOpen: boolean;
  onOpenChange: () => void;
  total: number;
  onFinishSale: (paymentMethod: "CASH" | "CARD" | "TRANSFER", cashReceived: string) => void;
}

export const PaymentModal = ({ isOpen, onOpenChange, total, onFinishSale }: PaymentModalProps) => {
  const [paymentMethod, setPaymentMethod] = useState<"CASH" | "CARD" | "TRANSFER">("CASH");
  const [cashReceived, setCashReceived] = useState("");

  const change = useMemo(() => {
    const received = parseFloat(cashReceived) || 0;
    return received - total;
  }, [cashReceived, total]);

  const handleFinish = () => {
    if (paymentMethod === "CASH" && change < 0) return;
    onFinishSale(paymentMethod, cashReceived);
    setCashReceived("");
    setPaymentMethod("CASH");
  };

  return (
    <Modal 
      isOpen={isOpen} 
      onOpenChange={onOpenChange}
      size="lg"
    >
      <ModalContent>
        {(onClose) => (
          <motion.div layout style={{ height: "fit-content" }}>
            <ModalHeader className="flex flex-col gap-1">
              Finalizar Venta - Total: ${total.toFixed(2)}
            </ModalHeader>
            <ModalBody>
              <RadioGroup
                label="Método de Pago"
                value={paymentMethod}
                onValueChange={(value) => setPaymentMethod(value as "CASH" | "CARD" | "TRANSFER")}
              >
                <Radio 
                  value="CASH"
                >
                  Efectivo
                  <BanknoteIcon className="text-default-500" />
                </Radio>
                <Radio 
                  value="CARD"
                >
                  Tarjeta
                  <CreditCardIcon className="text-default-500" />
                </Radio>
                <Radio 
                  value="TRANSFER"
                >
                  Transferencia
                  <LandmarkIcon className="text-default-500" />
                </Radio>
              </RadioGroup>

              {paymentMethod === "CASH" && (
                <div className="mt-4 space-y-4">
                  <Input
                    type="number"
                    label="Efectivo Recibido"
                    placeholder="Ingrese la cantidad"
                    value={cashReceived}
                    onValueChange={setCashReceived}
                    startContent={
                      <div className="pointer-events-none flex items-center">
                        <span className="text-default-400 text-small">$</span>
                      </div>
                    }
                  />
                  <div className="flex justify-between items-center p-4 bg-default-100 rounded-lg">
                    <span className="font-semibold">Cambio a entregar:</span>
                    <span className={`text-xl font-bold ${change < 0 ? 'text-danger' : 'text-success'}`}>
                      ${change.toFixed(2)}
                    </span>
                  </div>
                </div>
              )}
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onClose}>
                Cancelar
              </Button>
              <Button 
                color="success" 
                onPress={handleFinish}
                isDisabled={paymentMethod === "CASH" && change < 0}
              >
                Confirmar Pago
              </Button>
            </ModalFooter>
          </motion.div>
        )}
      </ModalContent>
    </Modal>
  );
};