"use client";

import { CatalogueService } from "@/services/product";
import { Heading, VStack, Box, HStack } from "@chakra-ui/react";

export default function CataloguePage() {
  const { products } = CatalogueService();

  return (
    <VStack gap={4} align="stretch" p={4}>
      <Heading size="2xl" color="#2c2d97" fontWeight="bold">
        Bem vindo ao microcommerce!
      </Heading>
      <HStack flexWrap={"wrap"}>
        {products.map((product) => (
          <Box key={product.id} p={4} borderWidth={1} borderRadius="md">
            <Heading size="md">{product.name}</Heading>
            <Box mt={2}>{product.description}</Box>
            <Box mt={2} fontWeight="bold">
              R$ {product.price}
            </Box>
            <Box mt={2} color="gray.500">
              {product.seller}
            </Box>
          </Box>
        ))}
      </HStack>
    </VStack>
  );
}
