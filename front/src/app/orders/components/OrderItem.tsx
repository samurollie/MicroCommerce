import { FormatNumber, HStack, Link, Text, VStack } from "@chakra-ui/react";
import OrderListItem from "./OrderListItem";
import { Order } from "@/models/order";
import { OrderStatusIndicator } from "./OrderStatusIndicator";
import { OrderActionButton } from "./OrderActionButton";

export default function OrderItemCard({ order }: { order: Order }) {
  return (
    <VStack
      align={"flex-start"}
      key={order.id}
      p={4}
      borderWidth="1px"
      borderRadius="md"
      w="full"
      gap={4}
    >
      <HStack w="full" justify={"space-between"}>
        <Link href={"/orders" + order.id}>
          Pedido #{order.id.toString().padStart(8, "0")}
        </Link>
        <OrderStatusIndicator status={order.status} />
      </HStack>
      {order.items.map((item) => (
        <OrderListItem key={item.id} itemId={item.id} />
      ))}
      <HStack w="full" justify={"space-between"}>
        <Text fontSize={"xl"} fontWeight={"bold"}>
          Total do pedido{" "}
          <FormatNumber
            value={order.items.reduce(
              (acc, item) => acc + item.price * item.quantity,
              0
            )}
            style="currency"
            currency="BRL"
          />
        </Text>
        <OrderActionButton order={order} />
      </HStack>
    </VStack>
  );
}
