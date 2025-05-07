"use client";

import { EmptyState } from "@/components/ui/empty-state";
import { Product } from "@/models/product";
import { CartService } from "@/services/cart";
import { VStack } from "@chakra-ui/react";
import { LuShoppingCart } from "react-icons/lu";

export default function CartList() {
  const { items, total } = CartService();

  if (items.length === 0) {
    return (
      <VStack align={"center"} justify="center" h="full" w="full">
        <EmptyState
          title="Seu carrinho está vazio"
          description="Explore nossos produtos para adicionar itens ao seu carrinho"
          icon={<LuShoppingCart />}
        />
      </VStack>
    );
  }
  return (
    <div>
      <h1>Meu carrinho</h1>
      <ul>
        {items.map((product: Product) => (
          <li key={product.id}>
            {product.name} - {product.price} - {product.quantity}
          </li>
        ))}
      </ul>
      <p>Total: {total}</p>
    </div>
  );
}
