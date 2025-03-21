import { ScrollShadow } from "@heroui/scroll-shadow";
import { Button } from "@heroui/button";
import { ProductPOS } from "@/types/product";
import { Trash2Icon } from "lucide-react";
import { Table, TableHeader, TableBody, TableColumn, TableRow, TableCell } from "@heroui/table"

interface ProductListProps {
  products: ProductPOS[];
  onRemoveProduct: (id: number) => void;
}

export const ProductList = ({ products, onRemoveProduct }: ProductListProps) => {
  return (
    <ScrollShadow visibility="bottom" className="h-[75vh]">
      <Table 
        aria-label="Products list"
        className="mt-4"
        removeWrapper
        isHeaderSticky
      >
        <TableHeader>
          <TableColumn>PRODUCTO</TableColumn>
          <TableColumn>CANTIDAD</TableColumn>
          <TableColumn>PRECIO</TableColumn>
          <TableColumn>SUBTOTAL</TableColumn>
          <TableColumn>ACCIONES</TableColumn>
        </TableHeader>
        <TableBody>
          {products.map((product) => (
            <TableRow key={product.id}>
              <TableCell>{product.name}</TableCell>
              <TableCell>{product.quantity}</TableCell>
              <TableCell>${product.price.toFixed(2)}</TableCell>
              <TableCell>${(product.price * product.quantity).toFixed(2)}</TableCell>
              <TableCell>
                <Button
                  isIconOnly
                  color="danger"
                  variant="light"
                  onPress={() => onRemoveProduct(product.id)}
                  aria-label="Delete product"
                >
                  <Trash2Icon className="h-5 w-5" />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </ScrollShadow>
  );
};