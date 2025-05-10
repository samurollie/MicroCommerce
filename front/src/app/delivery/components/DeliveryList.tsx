"use client";
import CartListItem from "@/app/cart/components/CartListItem";
import { EmptyState } from "@/components/ui/empty-state";
import { Order, OrderStatus } from "@/models/order";
import { CartService } from "@/services/cart";
import { DeliveryService } from "@/services/delivery";
import { OrderService } from "@/services/order";
import { CatalogueService } from "@/services/product";
import {
  VStack,
  HStack,
  Box,
  Text,
  FormatNumber,
  Button,
  Card,
  Input,
  RadioCard,
  Group,
} from "@chakra-ui/react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { LuShoppingCart } from "react-icons/lu";

export default function DeliveryList() {
  const { items, total, clearCart } = CartService();
  const { createOrder } = OrderService();
  const { cep, setCep, values, generateDeliveryValues } = DeliveryService();
  const [selectedValue, setSelectedValue] = useState(0);
  const router = useRouter();

  useEffect(() => {
    if (cep.length === 8) {
      generateDeliveryValues(cep);
    }
  }, [cep, generateDeliveryValues]);

  if (items.length === 0) {
    return (
      <VStack align={"center"} justify="center" h="full" w="full">
        <EmptyState
          title="Seu carrinho está vazio"
          description="Explore nossos produtos para adicionar itens ao seu carrinho"
          icon={<LuShoppingCart />}
        />
      </VStack>
    );
  }

  const handlePayment = () => {
    const newOrder: Order = {
      id: 1,
      items: items,
      customerId: "1",
      status: OrderStatus.PENDING_PAYMENT,
      total: total + selectedValue,
    };

    createOrder(newOrder).then((order) => {
      clearCart();
      router.push("/checkout?id=" + order.id);
    });
  };

  return (
    <HStack
      w="full"
      align={"flex-start"}
      h="100%"
      overflow={"scroll"}
      flexDir={"row-reverse"}
    >
      <VStack w="30%" align={"flex-start"} gap={4} h="full">
        {items.map((item) => (
          <CartListItem key={item.id} item={item} showNumberInput={false} />
        ))}
      </VStack>
      <VStack
        align={"flex-start"}
        h="full"
        w="70%"
        overflow={"scroll"}
        justify={"space-between"}
        borderWidth={1}
        borderRadius="md"
        p={4}
      >
        <Box>
          <Text>Total (sem frete):</Text>
          <Text fontSize={"xl"} fontWeight={"bold"}>
            <FormatNumber value={total} style="currency" currency="BRL" />
          </Text>
        </Box>
        <Box w="full">
          <VStack w="full" mb={4} gap={3}>
            <Text fontSize="lg" fontWeight="semibold">
              Endereço de entrega
            </Text>
            <Box w="full">
              <Text mb={1}>CEP</Text>
              <Input
                type="text"
                maxLength={8}
                value={cep}
                onChange={(e) => setCep(e.target.value.replace(/\D/g, ""))}
                placeholder="00000-000"
                className="w-full p-2 border rounded"
              />
            </Box>
            <Box w="full">
              <Text mb={1}>Rua</Text>
              <Input type="text" className="w-full p-2 border rounded" />
            </Box>
            <HStack w="full" gap={4}>
              <Box flex={1}>
                <Text mb={1}>Número</Text>
                <Input type="text" className="w-full p-2 border rounded" />
              </Box>
              <Box flex={2}>
                <Text mb={1}>Complemento</Text>
                <Input type="text" className="w-full p-2 border rounded" />
              </Box>
            </HStack>
            <Box w="full">
              <Text mb={1}>Bairro</Text>
              <Input type="text" className="w-full p-2 border rounded" />
            </Box>
            <HStack w="full" gap={4}>
              <Box flex={2}>
                <Text mb={1}>Cidade</Text>
                <Input type="text" className="w-full p-2 border rounded" />
              </Box>
              <Box flex={1}>
                <Text mb={1}>Estado</Text>
                <Input
                  type="text"
                  maxLength={2}
                  className="w-full p-2 border rounded"
                />
              </Box>
            </HStack>
          </VStack>
          <RadioCard.Root
            defaultValue="next"
            gap="4"
            w="full"
            mb={4}
            onValueChange={(e) => setSelectedValue(values[e.value] || 0)}
          >
            <RadioCard.Label>Opções de frete:</RadioCard.Label>
            <Group orientation="vertical">
              {values &&
                values.sort().map((value, index) => (
                  <RadioCard.Item
                    key={value}
                    value={index.toString()}
                    width="full"
                    p="4"
                    colorPalette={"blue"}
                  >
                    <RadioCard.ItemHiddenInput />
                    <RadioCard.ItemControl>
                      <RadioCard.ItemIndicator />
                      <RadioCard.ItemContent>
                        <RadioCard.ItemText>
                          <FormatNumber
                            value={value}
                            style="currency"
                            currency="BRL"
                          />
                        </RadioCard.ItemText>
                        <RadioCard.ItemDescription>
                          Entrega até {""}
                          {new Date(
                            Date.now() + 7000 * 60 * 60 * 24
                          ).toLocaleDateString("pt-BR")}
                        </RadioCard.ItemDescription>
                      </RadioCard.ItemContent>
                    </RadioCard.ItemControl>
                  </RadioCard.Item>
                ))}
            </Group>
          </RadioCard.Root>

          <Box>
            <Text>Total (com frete):</Text>
            <Text fontSize={"xl"} fontWeight={"bold"}>
              <FormatNumber
                value={total + selectedValue}
                style="currency"
                currency="BRL"
              />
            </Text>
          </Box>

          <Button w="full" p="4" bgColor={"#2c2d97"} onClick={handlePayment}>
            Continuar para pagamento
          </Button>
          <Button
            w="full"
            mt={1}
            p="4"
            variant="outline"
            onClick={() => router.back()}
          >
            Voltar
          </Button>
        </Box>
      </VStack>
    </HStack>
  );
}
