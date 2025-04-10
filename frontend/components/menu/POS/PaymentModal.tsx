import { BanknoteIcon, CreditCardIcon, LandmarkIcon } from "lucide-react";
import { useEffect, useMemo, useState, useRef } from "react";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { RadioGroup, Radio } from "@heroui/radio";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@heroui/modal";
import { motion } from "framer-motion";
import { Kbd } from "@heroui/kbd";

interface PaymentModalProps {
  isOpen: boolean;
  onOpenChange: () => void;
  total: number;
  onFinishSale: (paymentMethod: "CASH" | "CARD", cashReceived: string) => void;
}

export const PaymentModal = ({ isOpen, onOpenChange, total, onFinishSale }: PaymentModalProps) => {
  const [paymentMethod, setPaymentMethod] = useState<"CASH" | "CARD">("CASH");
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

  const togglePaymentMethod = () => {
    setPaymentMethod((prev) => (prev === "CASH" ? "CARD" : "CASH"));
  };

  const inputRef = useRef<HTMLInputElement>(null);
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (isOpen && e.key === "Enter" && paymentMethod === "CASH" && change >= 0) {
        handleFinish();
      }
      if (isOpen && e.key === "Enter" && paymentMethod === "CARD") {
        handleFinish();
      }

      if (isOpen && e.key === "Tab") {
        e.preventDefault();
        togglePaymentMethod();
      }

      if (isOpen && paymentMethod === "CASH" && e.key >= '0' && e.key <= '9') {
        inputRef.current?.focus();
      }
    };

    window.addEventListener("keydown", handleKeyDown);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
    
  }, [isOpen, paymentMethod, cashReceived]);

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
                onValueChange={(value) => setPaymentMethod(value as "CASH" | "CARD")}
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
              </RadioGroup>

              {paymentMethod === "CASH" && (
                <div className="mt-4 space-y-4">
                  <Input
                    ref={inputRef}
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
              <Button color="danger" endContent={<Kbd>Esc</Kbd>} variant="light" onPress={onClose}>
                Cancelar
              </Button>
              <Button 
                color="success" 
                endContent={<Kbd>Enter</Kbd>}
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