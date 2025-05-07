"use client";

import { EmptyState } from "@/components/ui/empty-state";
import { CartService } from "@/services/cart";
import {
  Box,
  Button,
  FormatNumber,
  HStack,
  Text,
  VStack,
} from "@chakra-ui/react";
import { LuShoppingCart } from "react-icons/lu";
import CartListItem from "./CartListItem";
import { CatalogueService } from "@/services/product";

export default function CartList() {
  const { items, total, clearCart } = CartService();
  const { addProduct } = CatalogueService();

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

  const handleClearCart = () => {
    items.forEach((item) => {
      addProduct(item, item.quantity);
    });
    clearCart();
  };

  return (
    <HStack w="full" align={"flex-start"} h="50%" overflow={"scroll"}>
      <VStack w="70%" align={"flex-start"} gap={4} h="full">
        {items.map((item) => (
          <CartListItem key={item.id} item={item} />
        ))}
      </VStack>
      <VStack
        align={"flex-start"}
        h="full"
        w="30%"
        justify={"space-between"}
        borderWidth={1}
        borderRadius="md"
        p={4}
      >
        <Box>
          <Text>Total:</Text>
          <Text fontSize={"xl"} fontWeight={"bold"}>
            <FormatNumber value={total} style="currency" currency="BRL" />
          </Text>
        </Box>
        <Box>
          <Button w="full" p="4" bgColor={"#2c2d97"}>
            Continuar para pagamento
          </Button>
          <Button
            w="full"
            mt={1}
            p="4"
            variant="outline"
            onClick={handleClearCart}
          >
            Esvaziar carrinho
          </Button>
        </Box>
      </VStack>
    </HStack>
  );
}
