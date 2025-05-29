import { Listbox, ListboxItem, ListboxSection } from "@heroui/listbox";
import { Package, ChartBar, FileStack, Mail, Settings, ChevronRight, Home, ReceiptText } from "lucide-react";
import { IconWrapper } from "@/components/misc/IconWrapper";
import { Icon } from "@/components/misc/Icon";
import Logout from "./menu/Logout";
import Setting from "./menu/Settings";
import Products from "./menu/Products/Products";
import POS from "./menu/POS/POS"
import Stores from "./menu/Stores/Stores";
import Notifications from "./menu/Notifications";
import { useState, useMemo, useEffect, useLayoutEffect } from "react";
import { useSelectedStore } from "@/context/SelectedStoreContext";
import { NotificationBadge } from "@/components/misc/NotificationBadge";
import Reports from "./menu/Reports/Reports";

interface SideMenuProps {
  setContent: (content: JSX.Element) => void;
}

const SideMenu: React.FC<SideMenuProps> = ({ setContent }) => {
  const [selectedKey, setSelectedKey] = useState<Set<string>>(
    new Set(typeof window !== "undefined" ? [localStorage.getItem("selectedKey") || "home"] : [])
  );
  const [sidebarVisible, setSidebarVisible] = useState(false);
  const { selectedStoreString } = useSelectedStore();

  const toggleSidebar = () => {
    setSidebarVisible(!sidebarVisible);
  };

  useEffect(() => {
    let storedValue = localStorage.getItem("selectedKey") || "home";
    if (selectedStoreString == "0") {
      storedValue = "home";
    }
    setSelectedKey(new Set([storedValue]));
  }, []);

  const selectedValue = useMemo(
    () => Array.from(selectedKey).join(", "),
    [selectedKey],
  );

  useEffect(() => {
    switch (selectedValue) {
      case "home":
        setContent(
          <Stores />
        );
        break;
      case "pdv":
        setContent(
          <POS />
        );
        break;
      case "products":
        setContent(
          <Products />
        );
        break;
      case "notifications":
        setContent(
          <Notifications />
        );
        break;
      case "settings":
        setContent(
          <Setting />
        );
        break;
      case "logout":
      setContent(
        <Logout />
      );
      break;
      case "reports":
        setContent(
          <Reports />
        );
        break;
      default:
        setContent(
          <div>La función {selectedValue} estará disponible pronto.</div>,
        );
        break;
    }
    localStorage.setItem("selectedKey", selectedValue);
  }, [selectedValue]);

  return (
      <div>
        <div
          className={`sidebar ${sidebarVisible ? "sidebar-visible" : "sidebar-hidden"}`}
        >
          <Listbox
            variant="flat"
            aria-label="Listbox menu"
            disallowEmptySelection
            selectionMode="single"
            selectedKeys={selectedKey}
            disabledKeys={selectedStoreString == "0" ? ["pdv", "products", "reports", "stats", "notifications"] : []}
            onSelectionChange={(keys) => {
              const newSelectedKey = new Set<string>(
                Array.from(keys).map((key) => String(key)),
              );
              setTimeout(() => {
                setSidebarVisible(false);
              }, 350);
              setSelectedKey(newSelectedKey);
            }}
          >
            <ListboxSection showDivider>
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
            </ListboxSection>
            <ListboxSection title="Manejo de Tienda" showDivider>
              <ListboxItem
                key="pdv"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-warning/10 text-warning">
                    <ReceiptText />
                  </IconWrapper>
                }
              >
                Punto de Venta
              </ListboxItem>
              <ListboxItem
                key="stats"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-primary/10 text-primary">
                    <ChartBar />
                  </IconWrapper>
                }
              >
                Analytics
              </ListboxItem>
              <ListboxItem
                key="products"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-success/10 text-success">
                    <Package />
                  </IconWrapper>
                }
              >
                Productos
              </ListboxItem>
              <ListboxItem
                key="reports"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-secondary/10 text-secondary">
                    <FileStack />
                  </IconWrapper>
                }
              >
                Reportes
              </ListboxItem>
              <ListboxItem
                key="notifications"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-pink-500/10 text-pink-500">
                    <Mail />
                  </IconWrapper>
                }
              >
                <div className="flex items-center justify-between w-full">
                  <span>Notificaciones</span>
                  <NotificationBadge className="ml-2" />
                </div>
              </ListboxItem>
            </ListboxSection>
            <ListboxSection title="Administración" showDivider>
              <ListboxItem
                key="settings"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-info/10 text-info">
                    <Settings />
                  </IconWrapper>
                }
              >
                Configuración
              </ListboxItem>
              <ListboxItem
                key="logout"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-danger/10 text-danger">
                    <Icon name="power" />
                  </IconWrapper>
                }
              >
                Cerrar Sesión
              </ListboxItem>
            </ListboxSection>
          </Listbox>
        </div>      
       <button onClick={toggleSidebar} className="toggle-btn group h-15 w-15">
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

export default SideMenu;

// In the Listbox, add the Reports item:
<ListboxItem
  key="reports"
  startContent={
     <IconWrapper className="bg-success/10 text-success">
       <ChartBar className="text-lg" />
     </IconWrapper>
  }
  endContent={<ChevronRight className="text-default-400" />}
>
  Reports
</ListboxItem>
