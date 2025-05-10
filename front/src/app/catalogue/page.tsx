"use client";

import ProductCard from "@/components/ui/product-card";
import { CatalogueService } from "@/services/product";
import { SetupService } from "@/services/setup";
import { ProductType } from "@/models/product";
import { Heading, VStack, HStack } from "@chakra-ui/react";

const typeMapping: Record<string, ProductType> = {
  FOOD: ProductType.FOOD,
  BOOK: ProductType.BOOK,
  ELECTRONIC: ProductType.ELECTRONIC,
  CLOTHING: ProductType.CLOTHING,
  FURNITURE: ProductType.FURNITURE,
  TOY: ProductType.TOY,
  COSMETIC: ProductType.COSMETIC,
  STATIONERY: ProductType.STATIONERY,
  SPORT: ProductType.SPORT,
  OTHER: ProductType.OTHER,
};

export default function CataloguePage() {
  const { products } = CatalogueService();
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
          .filter((product) =>
            availableTypes.includes(typeMapping[product.type])
          )
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
          .filter((product) =>
            availableTypes.includes(typeMapping[product.type])
          )
          .map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
      </HStack>
    </VStack>
  );
}
