import { useState } from "react";

type Alert = {
  id: number;
  title: string;
  description: string;
  statusCode: number;
  count: number;
};

const alertTimeouts = new Map<number, NodeJS.Timeout>();

export function useStatusAlerts(timeout = 3000) {
  const [alerts, setAlerts] = useState<Alert[]>([]);

  const clearExistingTimeout = (id: number) => {
    const existingTimeout = alertTimeouts.get(id);
    if (existingTimeout) {
      clearTimeout(existingTimeout);
    }
  };

  const triggerAlert = (title: string, description: string, statusCode: number) => {
    setAlerts((prev) => {
      const existing = prev.find(
        (a) => a.title === title && a.description === description && a.statusCode === statusCode
      );

      if (existing) {
        const updatedAlerts = prev.map((a) =>
          a.id === existing.id ? { ...a, count: a.count + 1 } : a
        );

        clearExistingTimeout(existing.id);

        const newTimeout = setTimeout(() => {
          setAlerts((prev) => prev.filter((a) => a.id !== existing.id));
          alertTimeouts.delete(existing.id);
        }, timeout);

        alertTimeouts.set(existing.id, newTimeout);
        return updatedAlerts;
      }

      const id = Date.now();
      const newAlert: Alert = {
        id,
        title,
        description,
        statusCode,
        count: 1,
      };

      const newTimeout = setTimeout(() => {
        setAlerts((prev) => prev.filter((a) => a.id !== id));
        alertTimeouts.delete(id);
      }, timeout);

      alertTimeouts.set(id, newTimeout);
      return [...prev, newAlert];
    });
  };

  const removeAlert = (id: number) => {
    clearExistingTimeout(id);
    setAlerts((prev) => prev.filter((a) => a.id !== id));
    alertTimeouts.delete(id);
  };

  return {
    alerts,
    triggerAlert,
    removeAlert,
  };
}
