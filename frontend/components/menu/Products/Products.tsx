import { title, subtitle } from "@/components/misc/primitives";
import {
  Table,
  TableHeader,
  TableColumn,
  TableBody,
  TableRow,
  TableCell
} from "@heroui/table";
import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { Dropdown, DropdownTrigger, DropdownMenu, DropdownItem } from "@heroui/dropdown";
import { Pagination } from "@heroui/pagination";
import { useState, useMemo, useCallback, useLayoutEffect, useEffect } from "react";
import { SearchIcon, ChevronDownIcon, SaveIcon, PencilIcon, XIcon, UploadIcon, PlusIcon } from "lucide-react";
import { SharedSelection } from "@heroui/system";
import AddProductsModal from "./AddProductsModal";
import ImportProductsModal from "./ImportProductsModal";
import { useSelectedStore } from "@/context/SelectedStoreContext";
import ConfirmationModal from "@/components/misc/ConfirmationModal";
import { AnimatePresence, motion } from "framer-motion";
import { useStatusAlerts } from "@/hooks/useStatusAlerts";
import StatusAlertsStack from "@/components/misc/StatusAlertStack";
import { CircularProgress } from "@heroui/progress";

export const columns = [
  {name: "ID", uid: "id", sortable: true},
  {name: "BARCODE", uid: "barcode", sortable: true},
  {name: "NOMBRE", uid: "name", sortable: true},
  {name: "DESCRIPCION", uid: "description"},
  {name: "PRECIO", uid: "price", sortable: true},
  {name: "MAYOREO", uid: "wholesalePrice", sortable: true},
  {name: "MENUDEO", uid: "retailPrice", sortable: true},
  {name: "STOCK", uid: "stock", sortable: true},
  {name: "MINSTOCK", uid: "minAllowStock"},
  {name: "ACCIONES", uid: "actions"},
];

export type Product = {
  id: number;
  name: string;
  description: string;
  price: number;
  barcode: string;
  wholesalePrice: number;
  retailPrice: number;
  createdAt: number;
  stock: number;
  minAllowStock: number;
  storeId: number;
  [key: string]: any;
};

function parseProducts(data: any): Product[] {
  const productsArray = Array.isArray(data) ? data : data.products || data.data || [];
  
  return productsArray.map((item: any) => ({
    ...item,
    price: parseFloat(item.price),
    wholesalePrice: parseFloat(item.wholesalePrice),
    retailPrice: parseFloat(item.retailPrice)
  }));
}

export type SortDirection = "ascending" | "descending";

export type SortDescriptor = {
  column: string | number;
  direction: SortDirection;
};

export function capitalize(s : string) {
  return s ? s.charAt(0).toUpperCase() + s.slice(1).toLowerCase() : "";
}

const INITIAL_VISIBLE_COLUMNS = ["barcode", "name", "price", "retailPrice", "wholesalePrice", "stock", "actions"];

const Products = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [filterValue, setFilterValue] = useState("");
  const [selectedKeys, setSelectedKeys] = useState<Set<string> | "all">(new Set<string>());
  const [visibleColumns, setVisibleColumns] = useState<Set<string>>(new Set(INITIAL_VISIBLE_COLUMNS));
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [sortDescriptor, setSortDescriptor] = useState<SortDescriptor>({
    column: "name",
    direction: "ascending",
  });
  const [page, setPage] = useState(1);
  const { selectedStoreString } = useSelectedStore();

  const { alerts, triggerAlert, removeAlert } = useStatusAlerts();

  const [isLoading, setIsLoading] = useState(true);

  const [editingRows, setEditingRows] = useState<Record<number, boolean>>({});
  const [drafts, setDrafts] = useState<Record<number, Partial<Product>>>({});
  const toggleEditRow = (product: Product) => {
    const { _editing, ...cleanProduct } = product;
    setEditingRows((prev) => {
      const isNowEditing = !prev[product.id];
      if (isNowEditing) {
        setDrafts((prevDrafts) => ({
          ...prevDrafts,
          [product.id]: { ...cleanProduct },
        }));
      }
      return { ...prev, [product.id]: isNowEditing };
    });
  }
  const cancelEditRow = (id: number) => {
    setEditingRows((prev) => ({ ...prev, [id]: false }));
    setDrafts((prev) => {
      const newDrafts = { ...prev };
      delete newDrafts[id];
      return newDrafts;
    });
  };
  const saveEditRow = async (id: number) => {
    const draft = drafts[id];
    if (!draft) return;
    try {
      await putProduct(draft as Product);
      setEditingRows((prev) => ({ ...prev, [id]: false }));
      setDrafts((prev) => {
        const newDrafts = { ...prev };
        delete newDrafts[id];
        return newDrafts;
      });
    } catch (e) {
      console.error("Error al guardar", e);
    }
  };

  const fetchProducts = async () => {
    try {
      const res = await fetch(process.env.NEXT_PUBLIC_API_URL + `/stores/${selectedStoreString}/products`, {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        }
      });
      const data = await res.json();
      setProducts(parseProducts(data));
    } catch (error) {
      console.error("Error al obtener los productos:", error);
    }
  }

  const putProduct = async (product: Product) => {
    try {
      const res = await fetch(process.env.NEXT_PUBLIC_API_URL + `/stores/${selectedStoreString}/product`, {
        method: "PUT",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(product),
      });
      const status = res.status;
      if (status === 200) {
        setProducts((prevProducts) =>
          prevProducts.map((p) => (p.id === product.id ? { ...p, ...product } : p))
        );
        triggerAlert("Producto actualizado", `El producto ${product.name} ha sido actualizado correctamente.`, 200);
      } else {
        triggerAlert("Error al actualizar el producto", "No se pudo actualizar el producto. Inténtalo de nuevo.", 500);
      }
    } catch (error) {
      console.error("Error al actualizar el producto:", error);
      triggerAlert("Error al actualizar el producto", "No se pudo actualizar el producto. Inténtalo de nuevo.", 500);
    }
  };

  useLayoutEffect(() => {
    fetchProducts();
  }, []);

  const hasSearchFilter = Boolean(filterValue);

  const headerColumns = useMemo(() => {
    return columns.filter((column) => Array.from(visibleColumns).includes(column.uid));
  }, [visibleColumns]);

  const filteredItems = useMemo(() => {
    let filteredproducts = [...products];

    if (hasSearchFilter) {
      filteredproducts = filteredproducts.filter((product) =>
        product.name.toLowerCase().includes(filterValue.toLowerCase()) ||
        product.barcode.includes(filterValue.toLowerCase())
      );
    }

    return filteredproducts;
  }, [products, filterValue]);

  const pages = Math.ceil(filteredItems.length / rowsPerPage);

  const items = useMemo(() => {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;

    return filteredItems.slice(start, end);
  }, [page, filteredItems, rowsPerPage, editingRows, drafts]);

  const sortedItems = useMemo(() => {
    return [...items].map((item) => ({
      ...item,
      _editing: editingRows[item.id] ?? false,
    })).sort((a : Product, b : Product) => {
      let first = a[sortDescriptor.column];
      if (typeof first === "string") {
        first = first.toLowerCase();
      }
      let second = b[sortDescriptor.column];
      if (typeof second === "string") {
        second = second.toLowerCase();
      }
      const cmp = first < second ? -1 : first > second ? 1 : 0;

      return sortDescriptor.direction === "descending" ? -cmp : cmp;
    });
  }, [sortDescriptor, items, editingRows, drafts]);

  function isEqualDraft(draft: Partial<Product>, original: Product): boolean {
    return Object.keys(draft).every((key) => {
      return draft[key as keyof Product] === original[key as keyof Product];
    });
  }  

  const handleDeleteProduct = async (productId: number, productName: string) => {
    const deleteResponse = await deleteProduct(productId);
    if (deleteResponse === 200) {
      setProducts((prevProducts) => prevProducts.filter((p) => p.id !== productId));
      triggerAlert("Producto eliminado", `El producto ${productName} ha sido eliminado correctamente.`, 200);
    } else {
      triggerAlert("Error al eliminar el producto", "No se pudo eliminar el producto. Inténtalo de nuevo.", 500);
    }
  };

  const deleteProduct = async (productId: number) => {
    try {
      const res = await fetch(process.env.NEXT_PUBLIC_API_URL + `/stores/${selectedStoreString}/product/id/${productId}`, {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        }
      });
      const status = res.status;
      return status;
    } catch (error) {
      console.error("Error al eliminar el producto:", error);
      return 500;
    }
  };

  const renderCell = useCallback((product: Product, columnKey: string | number) => {
    const cellValue = product[columnKey];
    const isEditing = product._editing;
    const isEditable = columnKey !== "actions" && columnKey !== "id";
    
    return (
      <AnimatePresence mode="wait" initial={false}>
        <motion.div
          key={isEditing && isEditable ? 'input' : 'text'}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.2 }}
          className="w-full"
        >
          {isEditable && isEditing ? (
            <Input
              variant="faded"
              className="w-full"
              onClick={(e) => e.stopPropagation()}
              onKeyDown={(e) => {
                if (e.key === " ") e.stopPropagation();
              }}
              value={String(drafts[product.id]?.[columnKey] ?? "")}
              onChange={(e) => {
                const newValue = e.target.value;
                setDrafts((prevDrafts) => ({
                  ...prevDrafts,
                  [product.id]: {
                    ...prevDrafts[product.id],
                    [columnKey]: newValue,
                  },
                }));
              }}
            />
          ) : columnKey === "actions" ? (
            <div className="relative flex justify-center items-center gap-2">
              {isEditing ? (
                <>
                  <Button isDisabled={isEqualDraft(drafts[product.id]!, product!)} isIconOnly size="sm" variant="light" onPress={() => saveEditRow(product.id)}>
                    <SaveIcon className="text-default-500" />
                  </Button>
                  <Button isIconOnly size="sm" variant="light" onPress={() => cancelEditRow(product.id)}>
                    <XIcon className="text-default-500" />
                  </Button>
                </>
              ) : (
                <Button isIconOnly size="sm" variant="light" onPress={() => toggleEditRow(product)}>
                  <PencilIcon className="text-default-500" />
                </Button>
              )}
              <ConfirmationModal
                onConfirm={() => {
                  handleDeleteProduct(product.id, product.name);
                }}
                header={
                  <div className="text-default-900">
                    Eliminar Producto:
                    <span className="text-secondary"> {product.name}</span>
                  </div>
                }
                body={
                  <div className="text-default-500">
                    ¿Estás seguro de que deseas eliminar el producto{" "}
                    <span className="text-secondary-500">{product.name}</span>? Esta acción no se puede deshacer.
                  </div>
                }
              />
            </div>
          ) : columnKey === "stock" ? (
            <span className={`${cellValue < product.minAllowStock && "text-red-500"}`}>
              {parseFloat(cellValue).toFixed(2)}
            </span>
          ) : (
            <span className="text-sm">{cellValue}</span>
          )}
        </motion.div>
      </AnimatePresence>
    );
  }, [editingRows, drafts]);

  const onRowsPerPageChange = useCallback((e: React.ChangeEvent<HTMLSelectElement>) => {
    setRowsPerPage(Number(e.target.value));
    setPage(1);
  }, []);

  const onSearchChange = useCallback((value : string) => {
    if (value) {
      setFilterValue(value);
      setPage(1);
    } else {
      setFilterValue("");
    }
  }, []);

  const onClear = useCallback(() => {
    setFilterValue("");
    setPage(1);
  }, []);

  const topContent = useMemo(() => {
    return (
      <div className="flex flex-col gap-4">
        <h1 className={title()}>Productos</h1>
        <div className="flex justify-between gap-3 items-center">
          <Input
            isClearable
            className="w-full sm:max-w-[44%]"
            placeholder="Buscar..."
            startContent={<SearchIcon />}
            value={filterValue}
            onClear={() => onClear()}
            onValueChange={onSearchChange}
          />
          <div className="flex flex-row items-center justify-between gap-3">
            <Dropdown>
              <DropdownTrigger className="hidden sm:flex">
                <Button endContent={<ChevronDownIcon className="text-small" />} variant="flat">
                  Columnas
                </Button>
              </DropdownTrigger>
              <DropdownMenu
                disallowEmptySelection
                aria-label="Table Columns"
                closeOnSelect={false}
                selectedKeys={visibleColumns}
                selectionMode="multiple"
                onSelectionChange={(keys: SharedSelection) => {
                  if (keys === "all") {
                    setVisibleColumns(new Set(columns.map(column => column.uid)));
                  } else {
                    setVisibleColumns(new Set(keys as Set<string>));
                  }
                }}
              >
                {columns.map((column) => (
                  <DropdownItem key={column.uid} className="capitalize">
                    {capitalize(column.name)}
                  </DropdownItem>
                ))}
              </DropdownMenu>
            </Dropdown>
            <div className="flex">
              <AddProductsModal onProductAdded={(newProduct) => setProducts((prevProducts) => [...prevProducts, newProduct])} />
              <ImportProductsModal onProductAdded={(newProduct) => setProducts((prevProducts) => [...prevProducts, newProduct])} />
            </div>
          </div>
        </div>
        <div className="flex justify-end items-center">
          <label className="flex items-center text-default-400 text-small">
            Filas por página:
            <select
              className="bg-transparent outline-none text-default-400 text-small"
              onChange={onRowsPerPageChange}
            >
              <option value="10">10</option>
              <option value="20">20</option>
              <option value={products.length}>*</option>
            </select>
          </label>
        </div>
      </div>
    );
  }, [
    filterValue,
    visibleColumns,
    onRowsPerPageChange,
    products.length,
    onSearchChange,
    hasSearchFilter,
  ]);

  const bottomContent = useMemo(() => {
    return (
      <div className="py-2 px-2 flex justify-between items-center">
        <span className="w-[30%] text-small text-default-400">
          {selectedKeys === "all"
            ? "Todos los productos seleccionados"
            : `${selectedKeys instanceof Set && selectedKeys.size} de ${filteredItems.length} productos seleccionados`}
        </span>
        <Pagination
          isCompact
          showControls
          showShadow
          color="secondary"
          page={page}
          total={pages}
          onChange={setPage}
        />
      </div>
    );
  }, [selectedKeys, items.length, page, pages, hasSearchFilter]);

  return (
    <>
      <Table
        isHeaderSticky
        aria-label="Tabla de productos"
        bottomContent={bottomContent}
        bottomContentPlacement="outside"
        classNames={{
          wrapper: "max-h-[445px] min-h-[445px]",
        }}
        color="secondary"
        selectedKeys={selectedKeys}
        selectionMode="multiple"
        sortDescriptor={sortDescriptor}
        topContent={topContent}
        topContentPlacement="outside"
        onSelectionChange={(keys : SharedSelection) => setSelectedKeys(keys === "all" ? "all" : new Set(keys as Set<string>))}
        onSortChange={setSortDescriptor}
        onRowAction={() => {}}
      >
        <TableHeader columns={headerColumns}>
          {(column: typeof columns[number]) => (
            <TableColumn
              key={column.uid}
              align={column.uid === "actions" ? "center" : "start"}
              allowsSorting={column.sortable}
            >
              {column.name}
            </TableColumn>
          )}
        </TableHeader>
        <TableBody emptyContent={"No se encontraron productos"} items={sortedItems}>
          {(item: Product) => (
            <TableRow key={item.id}>
              {(columnKey: string | number) => 
              <TableCell>
                {renderCell(item, columnKey)}
              </TableCell>}
            </TableRow>
          )}
        </TableBody>
      </Table>
      <StatusAlertsStack alerts={alerts} onClose={removeAlert}/>
      {isLoading && (
        <div className="absolute z-20 inset-0 flex items-center justify-center backdrop-blur-sm">
          <CircularProgress isIndeterminate size="lg" color="secondary" />
        </div>
      )}
    </>
  );
}

export default Products;
