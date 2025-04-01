import { Button } from "@heroui/button";
import {
  Listbox,
  ListboxItem
} from "@heroui/listbox";
import { useDisclosure } from "@heroui/use-disclosure";
import { StoreModal } from "./StoreModal";
import type { Store } from "@/types/store";
import { PlusIcon } from "lucide-react";
import { useState, useLayoutEffect, useMemo, useEffect } from "react";
import { title } from "@/components/misc/primitives";
import StatusAlert from "@/components/misc/StatusAlert";
import ConfirmationModal from "@/components/misc/ConfirmationModal";
import { Switch } from "@heroui/switch";
import { motion } from "framer-motion";

export default function Stores() {
  const { 
    isOpen, 
    onOpen, 
    onOpenChange
  } = useDisclosure();
  const [stores, setStores] = useState<Store[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<Set<string>>(new Set([]));
  const [showDelete, setShowDelete] = useState(false);

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertDescription, setAlertDescription] = useState("");
  const [alertStatusCode, setAlertStatusCode] = useState(0);

  const fetchStores = async () => {
    try {
      const res = await fetch("http://localhost:8080/stores", {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        }
      });
      const data = await res.json();
      setStores(data);
    } catch (error) {
      console.error("Error al obtener las tiendas:", error);
    }
  }

  useLayoutEffect(() => {
    fetchStores();
  }, []);

  useEffect(() => {
    const storedValue = localStorage.getItem("selectedStore");
    setSelectedKeys(new Set([storedValue || "1"]));
  }, []);

  useEffect(() => {
    localStorage.setItem("selectedStore", Array.from(selectedKeys).join(", "));
  }, [selectedKeys]);

  const postStore = async (name: string, address: string) => {
    try {
      const res = await fetch("http://localhost:8080/createStore", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ name, address }),
      });
      const status = res.status;
      if (status === 201) {
        const newStore: Store = { 
          id: String(stores.length + 1), 
          name, 
          address, 
          createdAt: 1, 
          owner: 1
        };
        updateLocalStores(newStore);
        handleShowAlert("Tienda creada con éxito", `La tienda ${name} ha sido creada correctamente.`, 201);
      } else if (status === 400) {
        handleShowAlert("Nombre de tienda duplicado", `Ya existe una tienda con el nombre ${name}.`, 400);
      } else {
        handleShowAlert("Error al crear la tienda", "No se pudo crear la tienda. Inténtalo de nuevo.", 500);
      }
    } catch (error) {
      console.error("Error al crear la tienda:", error);
      handleShowAlert("Error al crear la tienda", "No se pudo crear la tienda. Inténtalo de nuevo.", 500);
    }
  }

  const handleDeleteStore = (storeId: string, storeName: string) => {
    setStores((prevStores) => prevStores.filter((store) => store.id !== storeId));
    handleShowAlert("Tienda eliminada", `La tienda ${storeName} ha sido eliminada correctamente.`, 200);
  };

  const handleShowAlert = (title : string, description : string, statusCode : number) => {
    setAlertTitle(title);
    setAlertDescription(description);
    setAlertStatusCode(statusCode);
    setShowAlert(true);
    setTimeout(() => {
      setShowAlert(false);
    }, 3000);
  };

  const handleSaveStore = (name: string, address: string) => {
    postStore(name, address);
  };

  const updateLocalStores = (newStore: Store) => {
    setStores((prevStores) => [...prevStores, newStore]);
  };

  return (
    <div className="w-full h-full mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className={title()}>Mis Tiendas</h1>
        <div>
          <Switch size="lg" className="mr-5" color="danger" isSelected={showDelete} onChange={() => setShowDelete(!showDelete)}>
            Eliminar
          </Switch>
          <Button 
            color="secondary" 
            onPress={onOpen}
            startContent={<PlusIcon />}
          >
            Crear Tienda
          </Button>
        </div>
      </div>

      <div className="grid grid-cols- md:grid-cols-2 gap-6">
        <div className="border border-default-200 rounded-lg">
          <Listbox
            aria-label="Lista de tiendas"
            selectionMode="single"
            selectedKeys={selectedKeys}
            onSelectionChange={(keys) => {
              const newSelectedKey = new Set<string>(
                Array.from(keys).map((key) => String(key)),
              );
              setSelectedKeys(newSelectedKey);
            }}
            emptyContent={"No tienes tiendas. Crea una tienda para empezar."}
            disallowEmptySelection   
          >
            {stores.map((store) => (
              <ListboxItem
                key={store.id}
                textValue={store.name}
                className="px-4"
                endContent={
                  <>
                    {showDelete && (
                      <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ duration: 0.5 }}
                        className="flex items-center gap-2"
                      >
                        <ConfirmationModal
                          header={
                          <div className="text-default-900">
                            Eliminar Tienda:
                            <span className="text-secondary"> {store.name}</span>
                          </div>}
                          body={
                            <div className="text-default-500">
                              ¿Estás seguro de que deseas eliminar la tienda <span className="text-secondary-500">{store.name}</span>? Esta acción no se puede deshacer.
                            </div>
                          }
                          onConfirm={() => {
                            handleDeleteStore(store.id, store.name);
                          }}
                        />
                      </motion.div>
                    )}
                  </>
                }
              >
                <div className="flex flex-col gap-1">
                  <span className="font-medium">{store.name}</span>
                  <span className="text-small text-default-500">{store.address}</span>
                </div>

              </ListboxItem>
            ))}
          </Listbox>
        </div>
      </div>

      <StatusAlert
        show={showAlert}
        title={alertTitle}
        description={alertDescription}
        statusCode={alertStatusCode}
      />

      <StoreModal
        isOpen={isOpen}
        onOpenChange={onOpenChange}
        onSave={handleSaveStore}
      />
    </div>
  );
}