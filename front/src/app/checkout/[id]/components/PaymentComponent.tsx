"use client";

import { toaster } from "@/components/ui/toaster";
import { Order, OrderStatus } from "@/models/order";
import { PaymentMethod } from "@/models/paymentMethod";
import { OrderService } from "@/services/order";
import { SetupService } from "@/services/setup";
import {
  VStack,
  Box,
  FormatNumber,
  Input,
  HStack,
  Button,
  Text,
} from "@chakra-ui/react";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

export default function PaymentComponent({ id }: { id: number }) {
  const { updateOrder, getOrder } = OrderService();
  const [order, setOrder] = useState<Order | null>(null);
  const { paymentMethod: availableMethods } = SetupService();
  const [paymentMethod, setPaymentMethod] = useState("");
  const router = useRouter();

  useEffect(() => {
    if (id) {
      setOrder(getOrder(Number(id)));
    }
  }, []);

  if (!order) {
    return <div>Order not found</div>;
  }

  const handlePayment = () => {
    if (order && paymentMethod) {
      updateOrder(order.id, OrderStatus.PROCESSING);
      toaster.create({
        title: "Pagamento realizado com sucesso",
        description: `Seu pedido foi confirmado com sucesso.`,
        type: "success",
        duration: 5000,
      });
      router.push("/orders");
    }
  };

  return (
    <VStack
      align={"center"}
      justify="center"
      h="full"
      w="full"
      bgColor={"#f7fafc"}
    >
      <VStack
        align={"flex-start"}
        h="lg"
        w={"lg"}
        overflow={"scroll"}
        justify={"space-between"}
        borderWidth={1}
        borderRadius="md"
        p={4}
      >
        <Box>
          <Text>Total</Text>
          <Text fontSize={"xl"} fontWeight={"bold"}>
            <FormatNumber
              value={order.totalPrice}
              style="currency"
              currency="BRL"
            />
          </Text>
        </Box>
        <Box w="full">
          <Text mb={2}>Forma de pagamento</Text>
          <select
            style={{
              width: "100%",
              padding: "10px",
              marginBottom: "16px",
              borderRadius: "4px",
              borderColor: "#E2E8F0",
            }}
            onChange={(e) => setPaymentMethod(e.target.value)}
            value={paymentMethod}
          >
            <option value="">Selecione uma forma de pagamento</option>
            {availableMethods.map((method) => (
              <option key={method} value={method}>
                {method === PaymentMethod.PIX
                  ? "PIX"
                  : method === PaymentMethod.BOLETO
                  ? "Boleto"
                  : method === PaymentMethod.CREDIT_CARD
                  ? "Cartão de Crédito"
                  : method === PaymentMethod.DEBIT_CARD
                  ? "Cartão de Débito"
                  : ""}
              </option>
            ))}
          </select>

          {paymentMethod === PaymentMethod.PIX && (
            <VStack p={4} borderWidth={1} borderRadius="md">
              <Text>Código PIX</Text>
              <Text fontSize="sm" fontFamily="monospace">
                00020126580014BR.GOV.BCB.PIX0136123e4567-e89b-12d3-a456-426614174000
              </Text>
            </VStack>
          )}

          {paymentMethod === PaymentMethod.BOLETO && (
            <VStack p={4} borderWidth={1} borderRadius="md">
              <Text>Código do Boleto</Text>
              <Text fontSize="sm" fontFamily="monospace">
                34191.79001 01043.510047 91020.150008 4 84770026000
              </Text>
            </VStack>
          )}

          {(paymentMethod === PaymentMethod.CREDIT_CARD ||
            paymentMethod === PaymentMethod.DEBIT_CARD) && (
            <VStack gap={4}>
              <Input placeholder="Número do cartão" />
              <HStack w="full">
                <Input placeholder="MM/AA" />
                <Input placeholder="CVV" />
              </HStack>
            </VStack>
          )}
        </Box>
        <Box>
          <Button w="full" p="4" bgColor={"#2c2d97"} onClick={handlePayment}>
            Confirmar pagamento
          </Button>
        </Box>
      </VStack>
    </VStack>
  );
}
