import { Alert } from "@heroui/alert";
import { motion, AnimatePresence } from "framer-motion";

type AlertProps = {
  alerts: {
    id: number;
    title: string;
    description: string;
    statusCode: number;
    count: number;
  }[];
  onClose: (id: number) => void;
};

export default function StatusAlertsStack({ alerts, onClose }: AlertProps) {
  const getColor = (statusCode: number) => {
    if (statusCode.toString().startsWith("1")) return "primary";
    if (statusCode.toString().startsWith("2")) return "success";
    if (statusCode.toString().startsWith("3")) return "secondary";
    if (statusCode.toString().startsWith("4")) return "warning";
    if (statusCode.toString().startsWith("5")) return "danger";
    return "default";
  };

  return (
    <div className="fixed bottom-4 right-4 z-50 space-y-3">
      <AnimatePresence initial={false}>
        {alerts.map((alert) => (
          <motion.div
            key={alert.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: 20 }}
            transition={{ duration: 0.3 }}
          >
            <Alert
              color={getColor(alert.statusCode)}
              title={`${alert.title}${alert.count > 1 ? ` (x${alert.count})` : ""}`}
              description={alert.description}
              variant="faded"
              onClose={() => onClose(alert.id)}
            />
          </motion.div>
        ))}
      </AnimatePresence>
    </div>
  );
}
