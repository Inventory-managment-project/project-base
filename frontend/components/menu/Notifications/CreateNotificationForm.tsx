import React, { useState } from "react";
import { Card, CardBody, CardFooter, CardHeader } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { useNotifications } from "@/context/NotificationsContext";

const CreateNotificationForm: React.FC = () => {
  const { addNotification } = useNotifications();
  const [title, setTitle] = useState("");
  const [message, setMessage] = useState("");
  const [type, setType] = useState<"info" | "warning" | "success" | "error">("info");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!title || !message) return;
    
    addNotification({
      title,
      message,
      type
    });
    
    setTitle("");
    setMessage("");
  };

  return (
    <Card className="mb-6">
      <CardHeader>
        <h3 className="text-lg font-medium">Crear notificación de prueba</h3>
      </CardHeader>
      <CardBody>
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            type="text"
            label="Título"
            placeholder="Ingrese el título de la notificación"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
          
          <Input
            type="text"
            label="Mensaje"
            placeholder="Ingrese el mensaje de la notificación"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            required
          />
          
          <div className="flex flex-col">
            <p className="mb-2 text-sm font-medium">Tipo de notificación</p>
            <div className="flex space-x-2">
              {["info", "success", "warning", "error"].map((option) => (
                <Button
                  key={option}
                  type="button"
                  color={option as "primary" | "success" | "warning" | "danger"}
                  variant={type === option ? "solid" : "bordered"}
                  onClick={() => setType(option as "info" | "warning" | "success" | "error")}
                  className="capitalize"
                >
                  {option}
                </Button>
              ))}
            </div>
          </div>
        </form>
      </CardBody>
      <CardFooter>
        <Button color="primary" onClick={handleSubmit}>
          Crear notificación
        </Button>
      </CardFooter>
    </Card>
  );
};

export default CreateNotificationForm; 