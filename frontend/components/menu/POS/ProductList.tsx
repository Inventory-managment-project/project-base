import { ScrollShadow } from "@heroui/scroll-shadow";
import { Button } from "@heroui/button";
import { ProductPOS } from "@/types/product";
import { Trash2Icon } from "lucide-react";
import { Table, TableHeader, TableBody, TableColumn, TableRow, TableCell } from "@heroui/table"
import { Input } from "@heroui/input";

interface ProductListProps {
  products: ProductPOS[];
  setProducts: (products: ProductPOS[]) => void;
  onRemoveProduct: (id: number) => void;
}

export const ProductList = ({ products, setProducts, onRemoveProduct }: ProductListProps) => {
  if (products.length === 0) {
    return (
      <div className="text-center py-10 text-gray-500">
        <p>No hay productos en el carrito</p>
      </div>
    );
  }

  return (
    <ScrollShadow visibility="bottom" className="h-[58vh]">
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
              <TableCell>
                <Input
                  type="number"
                  isInvalid={isNaN(product.quantity)}
                  value={String(product.quantity)}
                  onChange={(e) => {
                    const newQuantity = parseInt(e.target.value);
                    const updatedProducts = [...products];
                    const productIndex = updatedProducts.findIndex(p => p.id === product.id);
                    if (productIndex === -1) return;
                    const newProduct = { ...updatedProducts[productIndex] };
                    if (newQuantity < 1) {
                      newProduct.quantity = 1;
                    } else {
                      newProduct.quantity = newQuantity;
                    }
                    updatedProducts[productIndex] = newProduct;
                    setProducts(updatedProducts);
                  }}
                  className="w-16"
                  min={1}
                  max={99}
                  step={1}
                  aria-label="Product quantity"
                />
              </TableCell>
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