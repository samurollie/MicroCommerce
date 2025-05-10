"use client";
import { Order, OrderStatus } from "@/models/order";
import { OrderService } from "@/services/order";
import { Button } from "@chakra-ui/react";
import { useRouter } from "next/navigation";

export const OrderActionButton = ({ order }: { order: Order }) => {
  const { updateOrder } = OrderService();
  const router = useRouter();

  switch (order.status) {
    case OrderStatus.PENDING_PAYMENT:
      return (
        <Button
          bgColor={"blue"}
          p={4}
          onClick={() => router.push("/checkout/" + order.id)}
        >
          Continuar para pagamento
        </Button>
      );
    case OrderStatus.SHIPPED:
      return (
        <Button
          bgColor={"blue"}
          p={4}
          onClick={() => updateOrder(order.id, OrderStatus.DELIVERED)}
        >
          Confirmar recebimento
        </Button>
      );
    default:
      return null;
  }
};
