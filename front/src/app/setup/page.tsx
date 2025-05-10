"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { PaymentMethod } from "@/models/paymentMethod";
import { ProductType } from "@/models/product";
import { SetupService } from "@/services/setup";
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

export default function SetupPage() {
  const router = useRouter();
  const { productType, paymentMethod, setProductType, setPaymentMethod } =
    SetupService();

  const { handleSubmit, register, setValue } = useForm({
    defaultValues: {
      productTypes: productType,
      paymentMethods: paymentMethod,
    },
  });

  const onSubmit = (data: {
    productTypes: ProductType[];
    paymentMethods: PaymentMethod[];
  }) => {
    console.log("Form data:", data);
    // Garantir que estamos passando um array válido de ProductType
    const selectedTypes = data.productTypes.filter((type) =>
      Object.values(ProductType).includes(type as ProductType)
    ) as ProductType[];

    console.log("Filtered types:", selectedTypes);
    setProductType(selectedTypes);
    setPaymentMethod(data.paymentMethods as PaymentMethod[]);
    router.push("/catalogue");
  };

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
        <form onSubmit={handleSubmit(onSubmit)}>
          <Card.Body>
            <VStack align={"flex-start"} gap={4}>
              <Text fontWeight={"bold"}>Features disponíveis:</Text>
              <Separator />
              <Text>O que deseja vender?</Text>
              <HStack flexWrap={"wrap"}>
                {Object.values(ProductType).map((type) => {
                  console.log(
                    `Checkbox for type ${type}, defaultChecked=${productType.includes(
                      type
                    )}`
                  );
                  return (
                    <Checkbox
                      key={type}
                      {...register("productTypes")}
                      value={type}
                      defaultChecked
                    >
                      {type}
                    </Checkbox>
                  );
                })}
              </HStack>
              <Separator />
              <Text>Qual o método de pagamento?</Text>
              <HStack flexWrap={"wrap"}>
                {Object.values(PaymentMethod).map((method) => (
                  <Checkbox
                    key={method}
                    {...register("paymentMethods")}
                    value={method}
                    defaultChecked={paymentMethod.includes(method)}
                  >
                    {method === PaymentMethod.CREDIT_CARD
                      ? "Cartão de crédito"
                      : method === PaymentMethod.DEBIT_CARD
                      ? "Cartão de débito"
                      : method === PaymentMethod.PIX
                      ? "Pix"
                      : "Boleto"}
                  </Checkbox>
                ))}
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
