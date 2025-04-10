import { useState } from "react";

type Alert = {
  id: number;
  title: string;
  description: string;
  statusCode: number;
};

export function useStatusAlerts(timeout = 3000) {
  const [alerts, setAlerts] = useState<Alert[]>([]);

  const triggerAlert = (title: string, description: string, statusCode: number) => {
    const id = Date.now();
    const newAlert: Alert = { id, title, description, statusCode };
    setAlerts((prev) => [...prev, newAlert]);

    setTimeout(() => {
      setAlerts((prev) => prev.filter((a) => a.id !== id));
    }, timeout);
  };

  return {
    alerts,
    triggerAlert,
  };
}