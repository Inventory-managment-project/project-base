import React, { createContext, useContext, useEffect, useState } from "react";

interface SelectedStoreContextType {
  selectedStoreString: string;
  selectedStore: Set<string>;
  setSelectedStore: React.Dispatch<React.SetStateAction<Set<string>>>;
}

const SelectedStoreContext = createContext<SelectedStoreContextType | undefined>(undefined);

export function SelectedStoreProvider({ children }: { children : React.ReactNode}) {
  const [selectedStore, setSelectedStore] = useState<Set<string>>(new Set([]));
  let selectedStoreString = Array.from(selectedStore).join(", ");

  useEffect(() => {
    const storedValue = localStorage.getItem("selectedStore");
    selectedStoreString = storedValue || "0";
    setSelectedStore(new Set([storedValue || "0"]));
  }, []);

  useEffect(() => {
    localStorage.setItem("selectedStore", Array.from(selectedStore).join(", "));
  }, [selectedStore]);

  return (
    <SelectedStoreContext.Provider value={{ selectedStoreString, selectedStore, setSelectedStore }}>
      {children}
    </SelectedStoreContext.Provider>
  );
}

export function useSelectedStore() {
  const context =  useContext(SelectedStoreContext);
  if (!context) {
    throw new Error("useSelectedStore must be used inside a SelectedStoreProvider");
  }
  return context;
}