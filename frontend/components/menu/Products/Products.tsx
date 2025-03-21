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
import { useState, useMemo, useCallback, useLayoutEffect } from "react";
import { SearchIcon, ChevronDownIcon, PlusIcon, PencilIcon, Trash2Icon } from "lucide-react";
import { SharedSelection } from "@heroui/system";
import AddProductsModal from "./AddProductsModal";

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

function parseProducts(data: any[]): Product[] {
  return data.map(item => ({
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

const INITIAL_VISIBLE_COLUMNS = ["barcode", "name", "price", "stock", "actions"];

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

  const fetchProducts = async () => {
    try {
      const res = await fetch(`http://localhost:8080/stores/${localStorage.getItem("selectedStore")}/products`, {
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
  }, [page, filteredItems, rowsPerPage]);

  const sortedItems = useMemo(() => {
    return [...items].sort((a : Product, b : Product) => {
      const first = a[sortDescriptor.column];
      const second = b[sortDescriptor.column];
      const cmp = first < second ? -1 : first > second ? 1 : 0;

      return sortDescriptor.direction === "descending" ? -cmp : cmp;
    });
  }, [sortDescriptor, items]);

  const renderCell = useCallback((product : Product, columnKey : string|number) => {
    const cellValue = product[columnKey];

    switch (columnKey) {
      case "actions":
        return (
          <div className="relative flex justify-center items-center gap-2">
            <Button isIconOnly size="sm" variant="light">
              <PencilIcon className="text-default-500"/>
            </Button>
            <Button isIconOnly size="sm" variant="light">
              <Trash2Icon className="text-danger"/>
            </Button>
          </div>
        );
      default:
        return cellValue;
    }
  }, []);

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
            <AddProductsModal onProductAdded={(newProduct) => setProducts((prevProducts) => [...prevProducts, newProduct])} />
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
            {(columnKey: string | number) => <TableCell>{renderCell(item, columnKey)}</TableCell>}
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
}

export default Products;
