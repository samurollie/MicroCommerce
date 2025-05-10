"use client";

import ProductCard from "@/components/ui/product-card";
import { CatalogueService } from "@/services/product";
import { RecommendationService } from "@/services/recommendation";
import { SetupService } from "@/services/setup";
import { Heading, VStack, HStack } from "@chakra-ui/react";

export default function CataloguePage() {
  const { products } = CatalogueService();
  const { recommendations } = RecommendationService();
  const { productType: availableTypes } = SetupService();

  return (
    <VStack gap={4} align="stretch" p={4}>
      <Heading size="2xl" color="#2c2d97" fontWeight="bold">
        Bem vindo ao microcommerce!
      </Heading>
      <Heading size="xl" color="#2c2d97" fontWeight="bold">
        Recomendados para você
      </Heading>
      <HStack flexWrap={"wrap"} align={"flex-start"} gap={4}>
        {products
          .filter((product) => availableTypes.includes(product.type))
          .slice(0, 5)
          .map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
      </HStack>
      <Heading size="xl" color="#2c2d97" fontWeight="bold">
        Todos os produtos
      </Heading>
      <HStack flexWrap={"wrap"} align={"flex-start"} gap={4}>
        {products
          .filter((product) => availableTypes.includes(product.type))
          .map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
      </HStack>
    </VStack>
  );
}
