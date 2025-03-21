"use client";

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
import { Tooltip } from "@heroui/tooltip";
import { Input } from "@heroui/input";
import { Dropdown, DropdownTrigger, DropdownMenu, DropdownItem } from "@heroui/dropdown";
import { Pagination } from "@heroui/pagination";
import { useState, useMemo, useCallback } from "react";
import { SearchIcon, ChevronDownIcon, PlusIcon, PencilIcon, Trash2Icon } from "lucide-react";
import { SharedSelection } from "@heroui/system";

export const columns = [
  { name: "ID", uid: "id", sortable: true },
  { name: "BARCODE", uid: "barcode", sortable: true },
  { name: "NOMBRE", uid: "name", sortable: true },
  { name: "DESCRIPCION", uid: "description" },
  { name: "PRECIO", uid: "price", sortable: true },
  { name: "MAYOREO", uid: "wholesale", sortable: true },
  { name: "MENUDEO", uid: "retail", sortable: true },
  { name: "STOCK", uid: "stock", sortable: true },
  { name: "ACCIONES", uid: "actions" },
];

export const products = [
  {
    "id": 1,
    "barcode": "1234567890123",
    "name": "Camiseta Negra",
    "description": "Camiseta de algodón 100% en color negro",
    "price": 150.00,
    "wholesale": 130.00,
    "retail": 160.00,
    "stock": 50
  },
  {
    "id": 2,
    "barcode": "9876543210987",
    "name": "Pantalón Vaquero",
    "description": "Pantalón de mezclilla azul claro",
    "price": 350.00,
    "wholesale": 320.00,
    "retail": 370.00,
    "stock": 30
  },
  {
    "id": 3,
    "barcode": "4561237890456",
    "name": "Sudadera con Capucha",
    "description": "Sudadera de felpa con capucha ajustable",
    "price": 450.00,
    "wholesale": 400.00,
    "retail": 470.00,
    "stock": 20
  },
  {
    "id": 4,
    "barcode": "1593574568523",
    "name": "Zapatillas Deportivas",
    "description": "Zapatillas ligeras para correr",
    "price": 600.00,
    "wholesale": 550.00,
    "retail": 620.00,
    "stock": 40
  },
  {
    "id": 5,
    "barcode": "7418529637410",
    "name": "Gorra Snapback",
    "description": "Gorra ajustable con diseño urbano",
    "price": 200.00,
    "wholesale": 180.00,
    "retail": 210.00,
    "stock": 60
  },
  {
    "id": 6,
    "barcode": "9874563210123",
    "name": "Bufanda de Lana",
    "description": "Bufanda suave y cálida para invierno",
    "price": 250.00,
    "wholesale": 220.00,
    "retail": 270.00,
    "stock": 35
  },
  {
    "id": 7,
    "barcode": "3214569876541",
    "name": "Cinturón de Cuero",
    "description": "Cinturón de cuero genuino",
    "price": 300.00,
    "wholesale": 270.00,
    "retail": 320.00,
    "stock": 15
  },
  {
    "id": 8,
    "barcode": "6549873216541",
    "name": "Reloj Digital",
    "description": "Reloj digital resistente al agua",
    "price": 700.00,
    "wholesale": 650.00,
    "retail": 750.00,
    "stock": 25
  },
  {
    "id": 9,
    "barcode": "7412589630123",
    "name": "Bolsa de Viaje",
    "description": "Bolsa espaciosa para viajes cortos",
    "price": 850.00,
    "wholesale": 800.00,
    "retail": 870.00,
    "stock": 10
  },
  {
    "id": 10,
    "barcode": "1472583690123",
    "name": "Mochila Escolar",
    "description": "Mochila con múltiples compartimentos",
    "price": 450.00,
    "wholesale": 420.00,
    "retail": 470.00,
    "stock": 45
  },
  {
    "id": 11,
    "barcode": "9876541236540",
    "name": "Termo Acero Inoxidable",
    "description": "Termo de 1L ideal para viajes",
    "price": 300.00,
    "wholesale": 270.00,
    "retail": 320.00,
    "stock": 20
  },
  {
    "id": 12,
    "barcode": "1597534568521",
    "name": "Lentes de Sol",
    "description": "Lentes polarizados con protección UV",
    "price": 400.00,
    "wholesale": 350.00,
    "retail": 420.00,
    "stock": 18
  },
  {
    "id": 13,
    "barcode": "7531598529632",
    "name": "Calcetines Deportivos",
    "description": "Pack de 5 pares de calcetines",
    "price": 100.00,
    "wholesale": 90.00,
    "retail": 110.00,
    "stock": 80
  },
  {
    "id": 14,
    "barcode": "3217896549875",
    "name": "Guantes Térmicos",
    "description": "Guantes de lana para invierno",
    "price": 180.00,
    "wholesale": 160.00,
    "retail": 200.00,
    "stock": 22
  },
  {
    "id": 15,
    "barcode": "3214567896541",
    "name": "Playera Deportiva",
    "description": "Playera transpirable para ejercicio",
    "price": 220.00,
    "wholesale": 190.00,
    "retail": 240.00,
    "stock": 30
  },
  {
    "id": 16,
    "barcode": "7894561237896",
    "name": "Faja Reductora",
    "description": "Faja ajustable para entrenamiento",
    "price": 500.00,
    "wholesale": 450.00,
    "retail": 520.00,
    "stock": 12
  },
  {
    "id": 17,
    "barcode": "4563217896547",
    "name": "Banda para la Cabeza",
    "description": "Banda elástica ideal para deportes",
    "price": 120.00,
    "wholesale": 100.00,
    "retail": 130.00,
    "stock": 40
  },
  {
    "id": 18,
    "barcode": "4569871236547",
    "name": "Shorts Deportivos",
    "description": "Shorts ligeros para entrenamiento",
    "price": 250.00,
    "wholesale": 220.00,
    "retail": 270.00,
    "stock": 25
  },
  {
    "id": 19,
    "barcode": "1237894569870",
    "name": "Chamarra Impermeable",
    "description": "Chamarra ligera resistente al agua",
    "price": 700.00,
    "wholesale": 650.00,
    "retail": 720.00,
    "stock": 18
  },
  {
    "id": 20,
    "barcode": "7894561236540",
    "name": "Pantalón Jogger",
    "description": "Pantalón cómodo para entrenamiento",
    "price": 320.00,
    "wholesale": 290.00,
    "retail": 350.00,
    "stock": 22
  },
  {
    "id": 21,
    "barcode": "7894561236545",
    "name": "Pantalón Recto",
    "description": "Pantalón cómodo para entrenamiento",
    "price": 320.00,
    "wholesale": 290.00,
    "retail": 350.00,
    "stock": 22
  }
];

export type Product = {
  id: number;
  barcode: string;
  name: string;
  description: string;
  price: number;
  wholesale: number;
  retail: number;
  stock: number;
  [key: string]: any;
};

// Nuevo tipo para la estructura que requiere el backend
export type BackendProduct = {
  id: number;
  name: string;
  description: string;
  price: string;
  barcode: string;
  wholesalePrice: string;
  retailPrice: string;
  createdAt: number;
  stock: number;
  minAllowStock: number;
  storeId: number;
};

export type SortDirection = "ascending" | "descending";

export type SortDescriptor = {
  column: string | number;
  direction: SortDirection;
};

export function capitalize(s: string) {
  return s ? s.charAt(0).toUpperCase() + s.slice(1).toLowerCase() : "";
}

const INITIAL_VISIBLE_COLUMNS = ["barcode", "name", "price", "stock", "actions"];

// Función para mapear un producto a la estructura requerida por el backend
const mapProductToBackend = (product: Product): BackendProduct => {
  return {
    id: product.id, // Este campo es ignorado en creación, se puede enviar cualquier valor
    name: product.name,
    description: product.description,
    price: product.price.toFixed(2),
    barcode: product.barcode,
    wholesalePrice: product.wholesale.toFixed(2),
    retailPrice: product.retail.toFixed(2),
    createdAt: Math.floor(Date.now() / 1000), // Timestamp actual en segundos
    stock: product.stock,
    minAllowStock: product.minAllowStock || 5, // Valor por defecto de 5 si no está definido
    storeId: 1, // Valor fijo o por defecto
  };
};

const Products = () => {
  const [filterValue, setFilterValue] = useState("");
  const [selectedKeys, setSelectedKeys] = useState<Set<string> | "all">(new Set<string>());
  const [visibleColumns, setVisibleColumns] = useState<Set<string>>(new Set(INITIAL_VISIBLE_COLUMNS));
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [sortDescriptor, setSortDescriptor] = useState<SortDescriptor>({
    column: "name",
    direction: "ascending",
  });
  const [page, setPage] = useState(1);

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
  }, [filterValue]);

  const pages = Math.ceil(filteredItems.length / rowsPerPage);

  const items = useMemo(() => {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;

    return filteredItems.slice(start, end);
  }, [page, filteredItems, rowsPerPage]);

  const sortedItems = useMemo(() => {
    return [...items].sort((a: Product, b: Product) => {
      const first = a[sortDescriptor.column];
      const second = b[sortDescriptor.column];
      const cmp = first < second ? -1 : first > second ? 1 : 0;

      return sortDescriptor.direction === "descending" ? -cmp : cmp;
    });
  }, [sortDescriptor, items]);

  const renderCell = useCallback((product: Product, columnKey: string | number) => {
    const cellValue = product[columnKey];

    switch (columnKey) {
      case "actions":
        return (
          <div className="relative flex justify-center items-center gap-2">
            <Tooltip content="Editar">
              <Button isIconOnly size="sm" variant="light">
                <PencilIcon className="text-default-400" />
              </Button>
            </Tooltip>
            <Tooltip color="danger" content="Eliminar">
              <Button isIconOnly size="sm" variant="light">
                <Trash2Icon className="text-danger" />
              </Button>
            </Tooltip>
          </div>
        );
      default:
        return cellValue;
    }
  }, []);

  const onNextPage = useCallback(() => {
    if (page < pages) {
      setPage(page + 1);
    }
  }, [page, pages]);

  const onPreviousPage = useCallback(() => {
    if (page > 1) {
      setPage(page - 1);
    }
  }, [page]);

  const onRowsPerPageChange = useCallback((e: React.ChangeEvent<HTMLSelectElement>) => {
    setRowsPerPage(Number(e.target.value));
    setPage(1);
  }, []);

  const onSearchChange = useCallback((value: string) => {
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

  // Función para crear un nuevo producto y enviar los datos al backend
  const onAddProduct = useCallback(async () => {
    // Se simula la creación de un producto; en la práctica, estos datos se obtendrían de un formulario
    const newProduct: Product = {
      id: products.length + 1, // Valor arbitrario (el backend ignora este campo en creación)
      barcode: "0000000000000",
      name: "Nuevo Producto",
      description: "Descripción del nuevo producto",
      price: 100.00,
      wholesale: 90.00,
      retail: 110.00,
      stock: 20,
    };

    const payload = mapProductToBackend(newProduct);

    try {
      const response = await fetch("../pages/api/product.ts", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        console.log("Producto creado correctamente");
        // Aquí se podría actualizar el estado para reflejar el nuevo producto
      } else {
        console.error("Error al crear el producto");
      }
    } catch (error) {
      console.error("Error en la petición", error);
    }
  }, []);

  const topContent = useMemo(() => {
    return (
      <div className="flex flex-col gap-4">
        <h1 className={title()}>Productos</h1>
        <div className="flex justify-between gap-3 items-end">
          <Input
            isClearable
            className="w-full sm:max-w-[44%]"
            placeholder="Buscar..."
            startContent={<SearchIcon />}
            value={filterValue}
            onClear={() => onClear()}
            onValueChange={onSearchChange}
          />
          <div className="flex gap-3">
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
            {/* Se añade el manejador onPress para enviar los datos al backend */}
            <Button onPress={onAddProduct} color="secondary" endContent={<PlusIcon />}>
              Agregar
            </Button>
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
    onAddProduct,
    onClear,
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
        <div className="hidden sm:flex w-[30%] justify-end gap-2">
          <Button isDisabled={page === 1} size="sm" variant="flat" onPress={onPreviousPage}>
            Anterior
          </Button>
          <Button isDisabled={page === pages} size="sm" variant="flat" onPress={onNextPage}>
            Siguiente
          </Button>
        </div>
      </div>
    );
  }, [selectedKeys, page, pages, onNextPage, onPreviousPage, filteredItems.length]);

  return (
    <Table
      isHeaderSticky
      aria-label="Tabla de productos"
      bottomContent={bottomContent}
      bottomContentPlacement="outside"
      classNames={{
        wrapper: "max-h-[445px] min-h-[445px]",
      }}
      selectedKeys={selectedKeys}
      selectionMode="multiple"
      sortDescriptor={sortDescriptor}
      topContent={topContent}
      topContentPlacement="outside"
      onSelectionChange={(keys: SharedSelection) =>
        setSelectedKeys(keys === "all" ? "all" : new Set(keys as Set<string>))
      }
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
            {(columnKey: string | number) => <TableCell>{renderCell(item, columnKey)}</TableCell>}
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
};

export default Products;
