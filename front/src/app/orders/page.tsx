"use client";

import { Order, OrderStatus } from "@/models/order";
import { ProductType } from "@/models/product";
import { Box, Heading, Tabs, VStack } from "@chakra-ui/react";
import OrderItemCard from "./components/OrderItem";
import { LuShoppingCart } from "react-icons/lu";
import { EmptyState } from "@/components/ui/empty-state";
import { OrderService } from "@/services/order";
import { useEffect } from "react";

export default function OrdersPage() {
  const { orders, setOrders } = OrderService();
  const categories = Object.values(OrderStatus);
  console.log(orders);

  return (
    <>
      <Heading>Meus Pedidos</Heading>
      <Box w="full">
        <Tabs.Root defaultValue={"all"} variant="enclosed" fitted w="full">
          <Tabs.List p={4} mb={2}>
            <Tabs.Trigger value="all" mr={4}>
              Todos
            </Tabs.Trigger>
            {categories.map((category) => (
              <Tabs.Trigger key={category} value={category} mr={4}>
                {category}
              </Tabs.Trigger>
            ))}
          </Tabs.List>
          <Tabs.Content value="all" w="full">
            <VStack w="full" align={"flex-start"} gap={4} h="full">
              {orders.length == 0 ? (
                <VStack align={"center"} justify="center" h="full" w="full">
                  <EmptyState
                    mt={16}
                    title="Não há pedidos nesta categoria"
                    icon={<LuShoppingCart />}
                  />
                </VStack>
              ) : (
                orders.map((order) => (
                  <OrderItemCard key={order.id} order={order} />
                ))
              )}
            </VStack>
          </Tabs.Content>
          {categories.map((category) => (
            <Tabs.Content key={category} value={category} w="full">
              <VStack w="full" align={"flex-start"} gap={4} h="full">
                {orders.filter((order) => order.status === category).length ===
                0 ? (
                  <VStack align={"center"} justify="center" h="full" w="full">
                    <EmptyState
                      mt={16}
                      title="Não há pedidos nesta categoria"
                      icon={<LuShoppingCart />}
                    />
                  </VStack>
                ) : (
                  orders
                    .filter((order) => order.status === category)
                    .map((order) => (
                      <OrderItemCard key={order.id} order={order} />
                    ))
                )}
              </VStack>
            </Tabs.Content>
          ))}
        </Tabs.Root>
      </Box>
    </>
  );
}
