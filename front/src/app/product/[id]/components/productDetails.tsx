"use client";

import { toaster } from "@/components/ui/toaster";
import { CartService } from "@/services/cart";
import { CatalogueService } from "@/services/product";
import {
  Box,
  Breadcrumb,
  Button,
  Card,
  FormatNumber,
  HStack,
  IconButton,
  Input,
  NumberInput,
  RatingGroup,
  Text,
  VStack,
} from "@chakra-ui/react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { LuMinus, LuPlus } from "react-icons/lu";

export default function ProductDetails({ id }: { id: number }) {
  const { products, removeProducts } = CatalogueService();
  const { addToCart } = CartService();
  const product = products.find((product) => product.id === Number(id));
  const [quantity, setValue] = useState("0");
  const [cep, setCep] = useState("");
  const [deliveryPrice, setDeliveryPrice] = useState(0);
  const router = useRouter();

  useEffect(() => {
    if (cep.length === 8) {
      setDeliveryPrice(100);
    } else {
      setDeliveryPrice(0);
    }
  }, [cep]);

  if (!product) {
    return <div>Product not found</div>;
  }

  const handleAddToCart = () => {
    if (quantity === "0") {
      return;
    }
    removeProducts(product.id, Number(quantity));
    addToCart(product, Number(quantity));
    toaster.create({
      title: "Produto adicionado ao carrinho",
      type: "success",
      duration: 10000,
      action: {
        label: "Ver carrinho",
        onClick: () => {
          router.push("/cart");
        },
      },
    });
  };

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
            <Box>
              <Text fontSize={"sm"} color="gray.500" mt={2}>
                Insira seu CEP
              </Text>
              <Input
                id="CEP"
                value={cep}
                onChange={(e) => setCep(e.target.value)}
                placeholder="00000-000"
              />
              {deliveryPrice > 0 && (
                <Text
                  fontSize={"sm"}
                  color="green.600"
                  mt={2}
                  fontWeight={"bold"}
                >
                  Entrega até {""}
                  {new Date(
                    Date.now() + 7000 * 60 * 60 * 24
                  ).toLocaleDateString("pt-BR")}{" "}
                  por:{" "}
                  <FormatNumber
                    value={deliveryPrice}
                    style="currency"
                    currency="BRL"
                  />
                </Text>
              )}
            </Box>
          </Card.Header>
          <Card.Body marginTop={16}>
            <NumberInput.Root
              value={quantity}
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
                value={Number(product.price) * Number(quantity)}
                style="currency"
                currency="BRL"
              />
            </Text>
          </Card.Body>
          <Card.Footer justifyContent="flex-end">
            <Button colorPalette={"blue"} padding={4} onClick={handleAddToCart}>
              Adicionar ao carrinho
            </Button>
          </Card.Footer>
        </Card.Root>
      </HStack>
    </>
  );
}
