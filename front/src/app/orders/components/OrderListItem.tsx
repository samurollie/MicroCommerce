"use client";

import { OrderService } from "@/services/order";
import { CatalogueService } from "@/services/product";
import { Box, HStack, Text, FormatNumber } from "@chakra-ui/react";
import Image from "next/image";

export default function OrderListItem({
  itemId,
  index,
}: {
  itemId: number;
  index: number;
}) {
  const { getProduct } = CatalogueService();
  const { getOrder } = OrderService();
  const item = getProduct(itemId);
  const order = getOrder(itemId);

  if (!item) {
    return null;
  }

  return (
    <HStack
      borderWidth={1}
      w="full"
      borderRadius={"md"}
      p={4}
      gap={4}
      justify={"space-between"}
    >
      <HStack gap={4} w="full">
        <Image
          src="https://placehold.co/60x60/png"
          alt="Placeholder"
          width={60}
          height={60}
        />
        <Box>
          <Text fontWeight="bold">{item.name}</Text>
          <Text mt={1} color="gray.500">
            {item.seller}
          </Text>
        </Box>
      </HStack>
      <HStack>
        <Text fontWeight="bold" color="gray.500">
          {order?.items[index].quantity}x
        </Text>
        <Text fontWeight="bold" color="gray.500">
          <FormatNumber value={item.price} style="currency" currency="BRL" />
        </Text>
      </HStack>
    </HStack>
  );
}
