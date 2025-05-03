import React, { createContext, useContext, useReducer, useEffect } from "react";
import { useSelectedStore } from "./SelectedStoreContext";

// Definir la estructura de cada notificación
export interface Notification {
  id: string;
  title: string;
  message: string;
  date: string;
  read: boolean;
  type: "info" | "warning" | "success" | "error";
}

// Definir los tipos de acciones para el reducer
type NotificationAction =
  | { type: 'LOAD_NOTIFICATIONS'; payload: Notification[] }
  | { type: 'ADD_NOTIFICATION'; payload: Notification }
  | { type: 'MARK_AS_READ'; payload: string }
  | { type: 'MARK_ALL_READ' }
  | { type: 'DELETE_NOTIFICATION'; payload: string }
  | { type: 'CLEAR_ALL' };

// Definir la interfaz del contexto
interface NotificationsContextType {
  notifications: Notification[];
  unreadCount: number;
  loading: boolean;
  addNotification: (notification: Omit<Notification, 'id' | 'date' | 'read'>) => void;
  markAsRead: (id: string) => void;
  markAllRead: () => void;
  deleteNotification: (id: string) => void;
  clearAll: () => void;
}

// Crear el contexto
const NotificationsContext = createContext<NotificationsContextType | undefined>(undefined);

// Reducer para gestionar las acciones de notificaciones
const notificationsReducer = (state: Notification[], action: NotificationAction): Notification[] => {
  switch (action.type) {
    case 'LOAD_NOTIFICATIONS':
      return action.payload;
    case 'ADD_NOTIFICATION':
      return [action.payload, ...state];
    case 'MARK_AS_READ':
      return state.map(notification =>
        notification.id === action.payload
          ? { ...notification, read: true }
          : notification
      );
    case 'MARK_ALL_READ':
      return state.map(notification => ({ ...notification, read: true }));
    case 'DELETE_NOTIFICATION':
      return state.filter(notification => notification.id !== action.payload);
    case 'CLEAR_ALL':
      return [];
    default:
      return state;
  }
};

// Proveedor del contexto
export function NotificationsProvider({ children }: { children: React.ReactNode }) {
  const [notifications, dispatch] = useReducer(notificationsReducer, []);
  const [loading, setLoading] = React.useState(true);
  const { selectedStoreString } = useSelectedStore();

  // Calcular el número de notificaciones no leídas
  const unreadCount = notifications.filter(notification => !notification.read).length;

  // Cargar notificaciones (mock)
  useEffect(() => {
    setLoading(true);
    // Simular carga de datos
    setTimeout(() => {
      const mockNotifications: Notification[] = [
        {
          id: "1",
          title: "Nuevos productos agregados",
          message: "Se han agregado 5 nuevos productos al catálogo.",
          date: "2023-11-15T10:30:00",
          read: false,
          type: "info"
        },
        {
          id: "2",
          title: "Venta exitosa",
          message: "Se ha completado una venta por $1500.",
          date: "2023-11-14T15:45:00",
          read: true,
          type: "success"
        },
        {
          id: "3",
          title: "Stock bajo",
          message: "El producto 'Laptop HP' tiene un stock bajo (2 unidades).",
          date: "2023-11-13T09:15:00",
          read: false,
          type: "warning"
        },
        {
          id: "4",
          title: "Error en transacción",
          message: "No se pudo procesar la transacción #12345.",
          date: "2023-11-12T14:20:00",
          read: true,
          type: "error"
        }
      ];
      
      dispatch({ type: 'LOAD_NOTIFICATIONS', payload: mockNotifications });
      setLoading(false);
    }, 1000);
  }, [selectedStoreString]);

  // Función para añadir una nueva notificación
  const addNotification = (notification: Omit<Notification, 'id' | 'date' | 'read'>) => {
    const newNotification: Notification = {
      ...notification,
      id: Date.now().toString(),
      date: new Date().toISOString(),
      read: false
    };
    dispatch({ type: 'ADD_NOTIFICATION', payload: newNotification });
  };

  // Función para marcar una notificación como leída
  const markAsRead = (id: string) => {
    dispatch({ type: 'MARK_AS_READ', payload: id });
  };

  // Función para marcar todas las notificaciones como leídas
  const markAllRead = () => {
    dispatch({ type: 'MARK_ALL_READ' });
  };

  // Función para eliminar una notificación
  const deleteNotification = (id: string) => {
    dispatch({ type: 'DELETE_NOTIFICATION', payload: id });
  };

  // Función para eliminar todas las notificaciones
  const clearAll = () => {
    dispatch({ type: 'CLEAR_ALL' });
  };

  return (
    <NotificationsContext.Provider value={{
      notifications,
      unreadCount,
      loading,
      addNotification,
      markAsRead,
      markAllRead,
      deleteNotification,
      clearAll
    }}>
      {children}
    </NotificationsContext.Provider>
  );
}

// Hook personalizado para usar el contexto
export function useNotifications() {
  const context = useContext(NotificationsContext);
  if (!context) {
    throw new Error("useNotifications debe ser usado dentro de un NotificationsProvider");
  }
  return context;
} 