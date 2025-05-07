import { useState, useRef } from "react";
import { Button } from "@heroui/button";
import { useDisclosure } from "@heroui/use-disclosure";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@heroui/modal";
import { UploadIcon, FileText, Download, UploadCloud } from "lucide-react";
import { Product } from "./Products";
import { useSelectedStore } from "@/context/SelectedStoreContext";
import { useStatusAlerts } from "@/hooks/useStatusAlerts";
import StatusAlertsStack from "@/components/misc/StatusAlertStack";
import Papa from "papaparse";
import { CircularProgress } from "@heroui/progress";

export default function ImportProductsModal({ onProductAdded }: { onProductAdded: (product: Product) => void }) {
  const {isOpen, onOpen, onOpenChange} = useDisclosure();
  const { alerts, triggerAlert, removeAlert } = useStatusAlerts();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const { selectedStoreString } = useSelectedStore();
  const [progress, setProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);

  const postProduct = async (product: Product) => {
    try {
      const res = await fetch(process.env.NEXT_PUBLIC_API_URL + `/stores/${selectedStoreString}/product`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(product),
      });
      const status = res.status;
      if (status === 201) {
        const data = await res.json();
        product.id = data.id;
        onProductAdded(product);
      } else {
        throw new Error("Error al crear el producto");
      }
    } catch (error) {
      console.error("Error al crear el producto:", error);
      triggerAlert("Error al crear el producto", "No se pudo crear el producto. Inténtalo de nuevo.", 500);
    }
  }

  const fileInputRef = useRef<HTMLInputElement>(null);
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setSelectedFile(e.target.files[0]);
    }
  };
  const handleUploadClick = () => {
    fileInputRef.current?.click();
  };
  const handleUploadCSV = (onClose: () => void) => {
    if (!selectedFile) return;
    Papa.parse(selectedFile, {
      header: true,
      skipEmptyLines: true,
      complete: async (results) => {
        const products = results.data as Product[];
        setProgress(0);
        setIsUploading(true);
        try {
          for (let i = 0; i < products.length; i++) {
            const product = products[i];
            const newProduct: Product = {
              id: Math.floor(Math.random() * 1000) + 1, 
              createdAt: Date.now(),
              storeId: Number(selectedStoreString),
              name: product.name,
              description: product.description,
              price: product.price,
              barcode: product.barcode,
              wholesalePrice: product.wholesalePrice,
              retailPrice: product.retailPrice,
              stock: product.stock,
              minAllowStock: product.minStock
            };
            await postProduct(newProduct);
            setProgress(Math.round(((i + 1) / products.length) * 100));
          };
        }
        catch (error) {
          console.error("Error al procesar el archivo CSV:", error);
          triggerAlert("Error al procesar el archivo CSV", "No se pudo procesar el archivo. Inténtalo de nuevo.", 500);
        }
        finally {
          setProgress(100);
          setIsUploading(false);
          setTimeout(() => {
            setProgress(0);
          }, 2000);
          triggerAlert("Productos importados con éxito", "Los productos han sido importados correctamente.", 200);
        }
      },
      error: (error) => {
        console.error("Error al procesar el archivo CSV:", error);
        triggerAlert("Error al procesar el archivo CSV", "No se pudo procesar el archivo. Inténtalo de nuevo.", 500);
      }
    });
    onClose();
    setSelectedFile(null);
  };
  const handleRemoveFile = (onClose: () => void) => {
    setSelectedFile(null);
    onClose();
  };

  return (
    <div className="flex flex-col items-center">
      <Button color="secondary" className="rounded-none rounded-r-xl" variant="flat" startContent={<UploadIcon />} onPress={onOpen}>
        <div className="hidden lg:block">Importar</div>
      </Button>
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">Importar Productos desde CSV</ModalHeader>
              <ModalBody>
                <div className="flex flex-col gap-4">
                  <p className="text-default-500 text-small text-justify">
                    Sube un archivo CSV con los datos de tus productos. Asegúrate de que el archivo esté en el formato correcto para evitar errores durante la importación.
                  </p>
                  
                  <div 
                    className="border-2 border-dashed border-default-200 rounded-lg p-8 text-center cursor-pointer hover:bg-default-50 transition-colors"
                    onClick={handleUploadClick}
                  >
                    <input
                      type="file"
                      ref={fileInputRef}
                      accept=".csv"
                      className="hidden"
                      onChange={handleFileChange}
                    />
                    
                    {selectedFile ? (
                      <div className="flex flex-col items-center gap-2">
                        <FileText className="text-secondary" size={32}/>
                        <p className="text-default-700 font-medium">{selectedFile.name}</p>
                        <p className="text-default-500 text-tiny">
                          {(selectedFile.size / 1024).toFixed(2)} KB
                        </p>
                      </div>
                    ) : (
                      <div className="flex flex-col items-center gap-2">
                        <UploadCloud className="text-default-400" size={32}/>
                        <p className="text-default-600 font-medium">Subir un archivo CSV</p>
                        <p className="text-default-400 text-tiny">o sueltalo aquí</p>
                      </div>
                    )}
                  </div>
                  
                  <div className="flex justify-between items-center">
                    <a href="example.csv" className="text-secondary text-small" download>
                      <span className="flex items-center gap-1">
                        <Download size={18} />
                        Descargar plantilla
                      </span>
                    </a>
                    <p className="text-default-400 text-tiny">Tamaño Máximo: 10MB</p>
                  </div>
                </div>
              </ModalBody>
              <ModalFooter>
                <Button variant="flat" onPress={() => handleRemoveFile(onClose)}>
                  Cancelar
                </Button>
                <Button 
                  color="secondary" 
                  onPress={() => handleUploadCSV(onClose)}
                  isDisabled={!selectedFile}
                >
                  Procesar
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
      <StatusAlertsStack alerts={alerts} onClose={removeAlert} />
      {isUploading && (
        <div className="absolute z-20 inset-0 flex items-center justify-center backdrop-blur-sm">
          <CircularProgress
            aria-label="Loading..."
            color="warning"
            showValueLabel={true}
            size="lg"
            value={progress}
          />
        </div>
      )}
    </div>
  );
}