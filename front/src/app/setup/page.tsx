"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { ProductType } from "@/models/product";

import {
  Button,
  Card,
  Heading,
  HStack,
  Separator,
  Text,
  VStack,
} from "@chakra-ui/react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";

export default function CataloguePage() {
  const router = useRouter();

  const { handleSubmit } = useForm();

  const onSubmit = (data) => {
    console.log(data);
  }

  return (
    <VStack align={"center"} gap={8}>
      <Card.Root p={8} borderRadius={8} variant={"elevated"}>
        <Card.Header>
          <Heading size="2xl" color={"#2c2d97"} fontWeight={"bold"}>
            Configurações do MicroCommerce
          </Heading>
          <Text>
            Selecione as features desejadas para o seu microcommerce. Você pode
            adicionar ou remover features a qualquer momento.
          </Text>
        </Card.Header>
        <form onSubmit={handleSubmit((data) => console.log(data))}>
          <Card.Body>
            <VStack align={"flex-start"} gap={4}>
              <Text fontWeight={"bold"}>Features disponíveis:</Text>
              <Separator />
              <Text>O que deseja vender?</Text>
              <HStack flexWrap={"wrap"}>
                {Object.values(ProductType).map((type) => (
                  <Checkbox key={type} value={type}>
                    {type.charAt(0).toUpperCase() + type.slice(1).toLowerCase()}
                  </Checkbox>
                ))}
              </HStack>
              <Separator />
              <Text>Qual o método de pagamento?</Text>
              <HStack flexWrap={"wrap"}>
                <Checkbox value="creditCard">Cartão de crédito</Checkbox>
                <Checkbox value="debitCard">Cartão de débito</Checkbox>
                <Checkbox value="pix">Pix</Checkbox>
                <Checkbox value="boleto">Boleto</Checkbox>
              </HStack>
              <Separator />
              <Text>Quem pode anunciar no seu microcommerce?</Text>
              <Separator />
              <HStack flexWrap={"wrap"} gap={4}>
                <Checkbox value="anyone">Qualquer um</Checkbox>
                <Checkbox value="onlyMe">Somente eu</Checkbox>
              </HStack>
            </VStack>
          </Card.Body>
          <Card.Footer>
            <HStack w="full" justify={"flex-end"} gap={4}>
              <Button
                variant={"outline"}
                colorPalette="blue"
                onClick={() => {
                  router.back();
                }}
              >
                Cancelar
              </Button>
              <Button variant={"solid"} colorPalette="blue" type="submit">
                Salvar
              </Button>
            </HStack>
          </Card.Footer>
        </form>
      </Card.Root>
    </VStack>
  );
}
