import { Listbox, ListboxItem } from "@heroui/listbox";
import { Info, Gem, Mail, ChevronRight, Home, ReceiptText } from "lucide-react";
import { IconWrapper } from "@/components/misc/IconWrapper";
import { useState, useMemo, useEffect, useLayoutEffect } from "react";

const LinksMobile = () => {
  const [selectedKey, setSelectedKey] = useState<Set<string>>(new Set(["home"]));
  const [sidebarVisible, setSidebarVisible] = useState(false);

  const toggleSidebar = () => {
    setSidebarVisible(!sidebarVisible);
  };

  const selectedValue = useMemo(
    () => Array.from(selectedKey).join(", "),
    [selectedKey],
  );

  return (
      <div>
        <div
          className={`sidebar-nav ${sidebarVisible ? "sidebar-visible-nav" : "sidebar-hidden-nav"}`}
        >
          <Listbox
            variant="flat"
            aria-label="Listbox menu"
            disallowEmptySelection
            selectionMode="single"
            selectedKeys={selectedKey}
            onSelectionChange={(keys) => {
              const newSelectedKey = new Set<string>(
                Array.from(keys).map((key) => String(key)),
              );
              setTimeout(() => {
                setSidebarVisible(false);
              }, 300);
              setSelectedKey(newSelectedKey);
            }}
          >
            <ListboxItem
              key="home"
              endContent={<ChevronRight />}
              startContent={
                <IconWrapper className="bg-pink-500/10 text-pink-500">
                  <Home />
                </IconWrapper>
              }
            >
              Inicio
            </ListboxItem>
            <ListboxItem
              key="docs"
              endContent={<ChevronRight />}
              startContent={
                <IconWrapper className="bg-warning/10 text-warning">
                  <ReceiptText />
                </IconWrapper>
              }
            >
              Documentacion
            </ListboxItem>
            <ListboxItem
              key="pricing"
              endContent={<ChevronRight />}
              startContent={
                <IconWrapper className="bg-primary/10 text-primary">
                  <Gem />
                </IconWrapper>
              }
            >
              Costos
            </ListboxItem>
            <ListboxItem
              key="contact"
              endContent={<ChevronRight />}
              startContent={
                <IconWrapper className="bg-success/10 text-success">
                  <Mail />
                </IconWrapper>
              }
            >
              Contacto
            </ListboxItem>
            <ListboxItem
              key="about"
              endContent={<ChevronRight />}
              startContent={
                <IconWrapper className="bg-secondary/10 text-secondary">
                  <Info />
                </IconWrapper>
              }
            >
              Acerca de
            </ListboxItem>
          </Listbox>
        </div>      
        <button onClick={toggleSidebar} className="toggle-btn-nav group h-15 w-15">
          <div className="grid justify-items-center gap-1.5">
          <span
            className={`h-1 w-8 rounded-full bg-black transition dark:bg-white 
            ${sidebarVisible && 'rotate-45 translate-y-2.5' }`}
          />
          <span
            className={`h-1 w-8 rounded-full bg-black transition dark:bg-white 
            ${sidebarVisible && 'scale-x-0' }`}
          />
          <span
            className={`h-1 w-8 rounded-full bg-black transition dark:bg-white 
            ${sidebarVisible && '-rotate-45 -translate-y-2.5' }`}
          />        
          </div>
        </button>
    </div>
  );
}

export default LinksMobile; 
