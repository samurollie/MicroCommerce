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

  /* useEffect(() => {
    setOrders([
      {
        id: 1,
        status: OrderStatus.PENDING_PAYMENT,
        customerId: "customer1",
        items: [
          {
            id: 1,
            name: "Product 1",
            description: "Description of Product 1",
            imageUrl: "image1.jpg",
            imageName: "image1.jpg",
            price: 100,
            quantity: 1,
            seller: "Seller 1",
            rating: 4.5,
            type: ProductType.FOOD,
          },
          {
            id: 9,
            name: "Product 9",
            description: "Description of Product 1",
            imageUrl: "image1.jpg",
            imageName: "image1.jpg",
            price: 100,
            quantity: 1,
            seller: "Seller 1",
            rating: 4.5,
            type: ProductType.FOOD,
          },
        ],
      },
      {
        id: 2,
        status: OrderStatus.PROCESSING,
        customerId: "customer2",
        items: [
          {
            id: 2,
            name: "Product 2",
            description: "Description of Product 2",
            imageUrl: "image1.jpg",
            imageName: "image1.jpg",
            price: 200,
            quantity: 2,
            seller: "Seller 2",
            rating: 4.0,
            type: ProductType.ELECTRONIC,
          },
        ],
      },
      {
        id: 3,
        status: OrderStatus.SHIPPED,
        customerId: "customer3",
        items: [
          {
            id: 3,
            name: "Product 3",
            description: "Description of Product 3",
            imageUrl: "image1.jpg",
            imageName: "image1.jpg",
            price: 300,
            quantity: 1,
            seller: "Seller 3",
            rating: 4.8,
            type: ProductType.CLOTHING,
          },
        ],
      },
      {
        id: 4,
        status: OrderStatus.DELIVERED,
        customerId: "customer4",
        items: [
          {
            id: 4,
            name: "Product 4",
            description: "Description of Product 4",
            imageUrl: "image1.jpg",
            imageName: "image1.jpg",
            price: 400,
            quantity: 3,
            seller: "Seller 4",
            rating: 4.2,
            type: ProductType.FOOD,
          },
        ],
      },
      {
        id: 5,
        status: OrderStatus.SHIPPED,
        customerId: "customer5",
        items: [
          {
            id: 5,
            name: "Product 5",
            description: "Description of Product 5",
            imageUrl: "image1.jpg",
            imageName: "image1.jpg",
            price: 500,
            quantity: 1,
            seller: "Seller 5",
            rating: 3.5,
            type: ProductType.COSMETIC,
          },
        ],
      },
    ]);
  }, []); */

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
