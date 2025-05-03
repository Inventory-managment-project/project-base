import React, { useState } from "react";
import { title, subtitle } from "@/components/misc/primitives";
import { Card, CardBody, CardFooter, CardHeader } from "@heroui/card";
import { Button } from "@heroui/button";
import { motion, AnimatePresence } from "framer-motion";
import { useNotifications } from "@/context/NotificationsContext";
import CreateNotificationForm from "./CreateNotificationForm";

const Notifications = () => {
  const { 
    notifications, 
    unreadCount, 
    loading, 
    markAsRead, 
    markAllRead, 
    deleteNotification 
  } = useNotifications();
  const [showCreateForm, setShowCreateForm] = useState(false);

  // Función para formatear la fecha
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', { 
      year: 'numeric', 
      month: 'short', 
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  // Función para obtener el color de la tarjeta según el tipo de notificación
  const getCardClass = (type: string) => {
    switch(type) {
      case "info": return "border-blue-300 dark:border-blue-600";
      case "success": return "border-green-300 dark:border-green-600";
      case "warning": return "border-yellow-300 dark:border-yellow-600";
      case "error": return "border-red-300 dark:border-red-600";
      default: return "border-gray-300 dark:border-gray-600";
    }
  };

  const getBackgroundClass = (type: string, read: boolean) => {
    if (read) return "bg-gray-50 dark:bg-gray-800/50";
    
    switch(type) {
      case "info": return "bg-blue-50 dark:bg-blue-900/20";
      case "success": return "bg-green-50 dark:bg-green-900/20";
      case "warning": return "bg-yellow-50 dark:bg-yellow-900/20";
      case "error": return "bg-red-50 dark:bg-red-900/20";
      default: return "bg-gray-50 dark:bg-gray-800";
    }
  };

  return (
    <div className="flex flex-col w-full max-w-3xl mx-auto px-4 py-6 gap-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className={title()}>Notificaciones</h1>
          <p className={subtitle()}>Gestiona las notificaciones de tu tienda</p>
        </div>
        <div className="flex gap-2 items-center">
          {unreadCount > 0 && (
            <span className="inline-flex items-center justify-center px-3 py-1 rounded-full bg-primary text-white text-sm font-medium">
              {unreadCount} no leídas
            </span>
          )}
          <Button 
            color="primary"
            variant="bordered"
            onClick={() => setShowCreateForm(!showCreateForm)}
          >
            {showCreateForm ? "Ocultar formulario" : "Crear notificación"}
          </Button>
        </div>
      </div>
      
      {showCreateForm && <CreateNotificationForm />}
      
      {loading ? (
        <div className="flex justify-center items-center h-40">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
        </div>
      ) : notifications.length === 0 ? (
        <Card className="text-center py-12">
          <CardBody>
            <p className="text-gray-500 dark:text-gray-400">No tienes notificaciones</p>
          </CardBody>
        </Card>
      ) : (
        <AnimatePresence>
          {notifications.map((notification) => (
            <motion.div
              key={notification.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              transition={{ duration: 0.3 }}
            >
              <Card 
                className={`mb-4 border-l-4 ${getCardClass(notification.type)} ${getBackgroundClass(notification.type, notification.read)}`}
              >
                <CardHeader className="pb-0 pt-4 px-4 flex-col items-start">
                  <div className="flex w-full justify-between items-center">
                    <h4 className="text-lg font-semibold">
                      {notification.title}
                      {!notification.read && (
                        <span className="ml-2 inline-block w-2 h-2 bg-primary rounded-full"></span>
                      )}
                    </h4>
                    <span className="text-sm text-gray-500 dark:text-gray-400">
                      {formatDate(notification.date)}
                    </span>
                  </div>
                </CardHeader>
                <CardBody className="py-2 px-4">
                  <p>{notification.message}</p>
                </CardBody>
                <CardFooter className="pt-0 px-4 pb-4 flex justify-end gap-2">
                  {!notification.read && (
                    <Button 
                      variant="flat" 
                      color="primary" 
                      size="sm"
                      onClick={() => markAsRead(notification.id)}
                    >
                      Marcar como leída
                    </Button>
                  )}
                  <Button 
                    variant="light" 
                    color="danger" 
                    size="sm"
                    onClick={() => deleteNotification(notification.id)}
                  >
                    Eliminar
                  </Button>
                </CardFooter>
              </Card>
            </motion.div>
          ))}
        </AnimatePresence>
      )}
      
      {/* Botones para gestionar las notificaciones */}
      {notifications.length > 0 && (
        <div className="flex justify-end gap-2">
          {unreadCount > 0 && (
            <Button 
              color="primary"
              variant="flat"
              onClick={markAllRead}
            >
              Marcar todas como leídas
            </Button>
          )}
        </div>
      )}
    </div>
  );
};

export default Notifications; 