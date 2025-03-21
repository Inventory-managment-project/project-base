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

export default function Stores() {
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const [stores, setStores] = useState<Store[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<Set<string>>(new Set([]));

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
    } catch (error) {
      console.error("Error al crear la tienda:", error);
    }
  }

  const handleSaveStore = (name: string, address: string) => {
    postStore(name, address);
    fetchStores();
  };

  return (
    <div className="w-full h-full mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className={title()}>Mis Tiendas</h1>
        <Button 
          color="secondary" 
          onPress={onOpen}
          startContent={<PlusIcon />}
        >
          Crear Tienda
        </Button>
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
            className="p-0"
          >
            {stores.map((store) => (
              <ListboxItem
                key={store.id}
                textValue={store.name}
                className="px-4"
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

      <StoreModal
        isOpen={isOpen}
        onOpenChange={onOpenChange}
        onSave={handleSaveStore}
      />
    </div>
  );
}