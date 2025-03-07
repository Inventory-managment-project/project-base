import { Listbox, ListboxItem, ListboxSection } from "@heroui/listbox";
import { Origami, UserSearch, ChartBar, FileStack, Mail, Settings, Power, ChevronRight, ChevronLeft } from "lucide-react";
import React from "react";
import PanelLayout from "@/layouts/panel";
import { useRouter } from "next/router";
import { IconWrapper } from "@/components/IconWrapper";
import { Icon } from "@/components/Icon";

export default function PanelPage() {
  const router = useRouter();
  const [content, setContent] = React.useState<JSX.Element | null>(null);
  const [selectedKey, setSelectedKey] = React.useState<Set<string>>(
    new Set(["stats"]),
  );
  const [windowSize, setWindowSize] = React.useState({
    width: typeof window !== "undefined" ? window.innerWidth : 0,
    height: typeof window !== "undefined" ? window.innerHeight : 0,
  });
  const [sidebarVisible, setSidebarVisible] = React.useState(true);
  const [timer, setTimer] = React.useState(0);
  const errorRef = React.useRef(200);

  const toggleSidebar = () => {
    setSidebarVisible(!sidebarVisible);
  };

  React.useEffect(() => {
    if (typeof window === "undefined") return;

    const handleResize = () => {
      setWindowSize({
        width: window.innerWidth,
        height: window.innerHeight,
      });
    };

    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const selectedValue = React.useMemo(
    () => Array.from(selectedKey).join(", "),
    [selectedKey],
  );

  React.useEffect(() => {
    if (timer !== 0) {
      const countdown = setInterval(() => {
        setTimer((prev) => {
          if (prev >= 1) return prev - 1;
          clearInterval(countdown);
          return 0;
        });
      }, 1000);

      return () => {
        clearInterval(countdown);
      };
    }
  }, [timer]);

  React.useEffect(() => {
    switch (selectedValue) {
      default:
        setContent(
          <div>La función {selectedValue} estará disponible pronto.</div>,
        );
        break;
    }
  }, [selectedValue, timer]);

  return (
    <PanelLayout>
      <div className="containerPanel">
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
      <button className="toggle-btn" onClick={toggleSidebar}>
        {sidebarVisible ? (
          <ChevronLeft />
        ) : (
          <ChevronRight />
        )}
      </button>
      <div className="content">{content}</div>
    </div>
    </PanelLayout>
  );
}
