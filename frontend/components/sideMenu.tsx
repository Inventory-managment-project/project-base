import { Listbox, ListboxItem, ListboxSection } from "@heroui/listbox";
import { Origami, UserSearch, ChartBar, FileStack, Mail, Settings, Power, ChevronRight, ChevronLeft } from "lucide-react";
import React from "react";
import { IconWrapper } from "@/components/misc/IconWrapper";
import { Icon } from "@/components/misc/Icon";
import { ThemeSwitch } from "@/components/misc/theme-switch";
import { title, subtitle } from "@/components/misc/primitives";
import { Button } from "@heroui/button";

interface SideMenuProps {
  setContent: (content: JSX.Element) => void;
}

const SideMenu: React.FC<SideMenuProps> = ({ setContent }) => {
  const [selectedKey, setSelectedKey] = React.useState<Set<string>>(
    new Set(["stats"]),
  );
  const [sidebarVisible, setSidebarVisible] = React.useState(true);

  const toggleSidebar = () => {
    setSidebarVisible(!sidebarVisible);
  };

  const selectedValue = React.useMemo(
    () => Array.from(selectedKey).join(", "),
    [selectedKey],
  );

  React.useEffect(() => {
    switch (selectedValue) {
      case "settings":
        setContent(
          <div className="flex flex-col w-[50%] max-md:w-[85%] h-full gap-4 justify-start items-center">
            <h1 className={title()}>Configuración</h1>
            <div className="flex flex-row justify-between w-full">
              <p className={subtitle()}>Visualización:</p>
              <ThemeSwitch />
            </div>
          </div>
        );
        break;
      case "logout":
      setContent(
        <div className="flex flex-col w-[50%] gap-4 justify-start items-center">
          <h1 className={title()}>Cerrar Sesión</h1>
          <div className="flex flex-row justify-between w-full">
            <p className={subtitle()}>¿Estás seguro que quieres cerrar sesión?</p>
            <Button color="danger">Confirmar</Button>
          </div>
        </div>
      );
      break;
      default:
        setContent(
          <div>La función {selectedValue} estará disponible pronto.</div>,
        );
        break;
    }
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
            onSelectionChange={(keys) => {
              const newSelectedKey = new Set<string>(
                Array.from(keys).map((key) => String(key)),
              );
              setSelectedKey(newSelectedKey);
            }}
          >
            <ListboxSection title="User Management" showDivider>
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
                key="pdv"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-warning/10 text-warning">
                    <Origami />
                  </IconWrapper>
                }
              >
                Punto de Venta
              </ListboxItem>
              <ListboxItem
                key="products"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-success/10 text-success">
                    <UserSearch />
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
            </ListboxSection>
            <ListboxSection title="Administration" showDivider>
              <ListboxItem
                key="notifications"
                endContent={<ChevronRight />}
                startContent={
                  <IconWrapper className="bg-pink-500/10 text-pink-500">
                    <Mail />
                  </IconWrapper>
                }
              >
                Notificaciones
              </ListboxItem>
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
                ></span>

                <span
                    className={`h-1 w-8 rounded-full bg-black transition dark:bg-white 
                    ${sidebarVisible && 'scale-x-0' }`}
                ></span>

                <span
                    className={`h-1 w-8 rounded-full bg-black transition dark:bg-white 
                    ${sidebarVisible && '-rotate-45 -translate-y-2.5' }`}
                ></span>          </div>
        </button>
    </div>
  );
}

export default SideMenu; 
