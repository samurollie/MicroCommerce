"use client";

import ProductCard from "@/components/ui/product-card";
import { CatalogueService } from "@/services/product";
import { Heading, VStack, HStack } from "@chakra-ui/react";

export default function CataloguePage() {
  const { products } = CatalogueService();

  return (
    <VStack gap={4} align="stretch" p={4}>
      <Heading size="2xl" color="#2c2d97" fontWeight="bold">
        Bem vindo ao microcommerce!
      </Heading>
      <HStack
        flexWrap={"wrap"}
        align={"strech"}
        justify={"space-between"}
        gap={4}
      >
        {products.map((product) => (
          <ProductCard key={product.id} product={product} />
        ))}
      </HStack>
    </VStack>
  );
}
