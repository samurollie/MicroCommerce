import { OrderStatus } from "@/models/order";
import { Text } from "@chakra-ui/react";

export const OrderStatusIndicator = ({ status }: { status: OrderStatus }) => {
  switch (status) {
    case OrderStatus.PENDING_PAYMENT:
      return <Text color="red.500">Aguardando pagamento</Text>;
    case OrderStatus.PROCESSING:
      return <Text color="yellow.500">Em processamento</Text>;
    case OrderStatus.SHIPPED:
      return <Text color="green.500">Enviado</Text>;
    case OrderStatus.DELIVERED:
      return <Text color="blue.500">Entregue</Text>;
    default:
      return null;
  }
};
