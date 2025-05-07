"use client";

import { CatalogueService } from "@/services/product";
import {
  Box,
  Breadcrumb,
  Button,
  Card,
  FormatNumber,
  Heading,
  HStack,
  IconButton,
  NumberInput,
  RatingGroup,
  Text,
  VStack,
} from "@chakra-ui/react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { LuMinus, LuPlus } from "react-icons/lu";

export default function ProductDetails({ id }: { id: number }) {
  const { products } = CatalogueService();
  const product = products.find((product) => product.id === Number(id));
  const [value, setValue] = useState("0");

  if (!product) {
    return <div>Product not found</div>;
  }

  return (
    <>
      <Breadcrumb.Root marginBottom={8}>
        <Breadcrumb.List>
          <Breadcrumb.Item>
            <Breadcrumb.Link href="/catalogue">Catálogo</Breadcrumb.Link>
          </Breadcrumb.Item>
          <Breadcrumb.Separator></Breadcrumb.Separator>
          <Breadcrumb.Item>
            <Breadcrumb.CurrentLink>{product.name}</Breadcrumb.CurrentLink>
          </Breadcrumb.Item>
        </Breadcrumb.List>
      </Breadcrumb.Root>
      <HStack align={"flex-start"} gap={4} width={"full"}>
        <Image
          src="https://placehold.co/400x400/png"
          alt="Placeholder"
          width={400}
          height={400}
        />
        <VStack align={"flex-start"} gap={4} width="60%">
          <Text fontSize="3xl">{product.name}</Text>
          <Text fontWeight="bold">Vendido por: {product.seller}</Text>
          <RatingGroup.Root
            count={5}
            defaultValue={product.rating}
            size="lg"
            colorPalette={"blue"}
            readOnly
          >
            <RatingGroup.HiddenInput />
            <RatingGroup.Control />
          </RatingGroup.Root>

          <Text>{product.description}</Text>
        </VStack>
        <Card.Root
          width={"20%"}
          h="full"
          borderWidth={1}
          borderRadius="md"
          p={4}
        >
          <Card.Header>
            <Text fontSize={"xl"} fontWeight={"bold"}>
              <FormatNumber
                value={product.price}
                style="currency"
                currency="BRL"
              />
            </Text>
            <Text fontSize={"sm"} color="gray.500" mt={2}>
              Frete grátis para todo o Brasil
            </Text>
          </Card.Header>
          <Card.Body marginTop={16}>
            <NumberInput.Root
              value={value}
              onValueChange={(e) => setValue(e.value)}
              unstyled
              spinOnPress={false}
              max={product.quantity}
              min={0}
              step={1}
            >
              <HStack gap="2">
                <NumberInput.DecrementTrigger asChild>
                  <IconButton variant="outline" size="sm">
                    <LuMinus />
                  </IconButton>
                </NumberInput.DecrementTrigger>
                <NumberInput.ValueText
                  textAlign="center"
                  fontSize="lg"
                  minW="3ch"
                />
                <NumberInput.IncrementTrigger asChild>
                  <IconButton variant="outline" size="sm">
                    <LuPlus />
                  </IconButton>
                </NumberInput.IncrementTrigger>
              </HStack>
            </NumberInput.Root>
            <Text fontSize={"sm"} color="gray.500" mt={2}>
              Quantidade disponível: {product.quantity}
            </Text>
            <Text fontSize={"md"} fontWeight={"bold"} mt={6}>
              Total:{" "}
              <FormatNumber
                value={Number(product.price) * Number(value)}
                style="currency"
                currency="BRL"
              />
            </Text>
          </Card.Body>
          <Card.Footer justifyContent="flex-end">
            <Button colorPalette={"blue"} padding={4}>
              Adicionar ao carrinho
            </Button>
          </Card.Footer>
        </Card.Root>
      </HStack>
    </>
  );
}
