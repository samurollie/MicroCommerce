"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { ProductType } from "@/models/productType";
import {
  Button,
  Heading,
  HStack,
  Separator,
  Text,
  VStack,
} from "@chakra-ui/react";
import { useForm } from "react-hook-form";

export default function CataloguePage() {
  const { handleSubmit } = useForm();

  return (
    <VStack align={"flext-start"} gap={8} w={"full"}>
      <Heading size="2xl" color={"#2c2d97"} fontWeight={"bold"}>
        Configurações do MicroCommerce
      </Heading>
      <Text>
        Selecione as features desejadas para o seu microcommerce. Você pode
        adicionar ou remover features a qualquer momento.
      </Text>
      <form onSubmit={handleSubmit((data) => console.log(data))}>
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
        <HStack w="full" justify={"flex-end"} gap={4}>
          <Button variant={"outline"} colorPalette="blue">
            Cancelar
          </Button>
          <Button variant={"solid"} colorPalette="blue" type="submit">
            Salvar
          </Button>
        </HStack>
      </form>
    </VStack>
  );
}
