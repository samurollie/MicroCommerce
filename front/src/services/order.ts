import { CreateOrderDTO } from "@/models/dtos/order";
import { Order, OrderStatus } from "@/models/order";
import { OrderStore } from "@/stores/order";
import { useCallback, useEffect } from "react";

export const OrderService = () => {
  const orders = OrderStore((state) => state.orders);
  const setOrders = OrderStore((state) => state.setOrders);
  const addOrder = OrderStore((state) => state.addOrder);
  const removeOrder = OrderStore((state) => state.removeOrder);
  const updateOrderStatus = OrderStore((state) => state.updateOrderStatus);

  const fetchOrders = useCallback(async () => {
    const response = await fetch(
      "http://localhost:8080/api/orders?customerId=1"
    );
    if (!response.ok) {
      console.error(response);
      return null;
    }
    return response.json();
  }, []);

  const fetchCreateOrder = useCallback(async (order: CreateOrderDTO) => {
    const response = await fetch("http://localhost:8080/api/orders", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(order),
    });
    if (!response.ok) {
      console.error(response);
      return null;
    }
    return response.json();
  }, []);

  const fetchUpdateOrder = useCallback(
    async (orderId: number, order: Partial<Order>) => {
      const response = await fetch(
        `http://localhost:8080/api/orders/${orderId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(order),
        }
      );
      if (!response.ok) {
        console.error(response);
        return null;
      }
      return response.json();
    },
    []
  );

  const fetchDeleteOrder = useCallback(async (orderId: number) => {
    const response = await fetch(
      `http://localhost:8080/api/orders/${orderId}`,
      {
        method: "DELETE",
      }
    );
    if (!response.ok) {
      console.error(response);
      return null;
    }
    return response.json();
  }, []);

  const getOrders = useCallback(async () => {
    const orders = await fetchOrders();
    const mappedOrders = orders?.map((order: Order) => ({
      ...order,
      status: OrderStatus[order.status as unknown as keyof typeof OrderStatus]
    }));
    setOrders(mappedOrders ?? []);
  }, [fetchOrders, setOrders]);

  const createOrder = useCallback(
    async (order: CreateOrderDTO): Promise<Order> => {
      const newOrder = await fetchCreateOrder(order);
      addOrder(newOrder);
      return newOrder;
    },
    [addOrder, fetchCreateOrder]
  );

  const updateOrder = useCallback(
    async (orderId: number, status: OrderStatus) => {
      const updatedOrder = await fetchUpdateOrder(orderId, { status });
      if (updatedOrder) {
        updateOrderStatus(orderId, status);
      }
    },
    [fetchUpdateOrder, updateOrderStatus]
  );
  const deleteOrder = useCallback(
    async (orderId: number) => {
      await fetchDeleteOrder(orderId);
      removeOrder(orderId);
    },
    [fetchDeleteOrder, removeOrder]
  );

  const getOrder = useCallback(
    (orderId: number) => {
      const order = orders.find((order) => order.id === orderId);
      if (!order) {
        console.error(`Order with id ${orderId} not found`);
        return null;
      }
      return order;
    },
    [orders]
  );

  useEffect(() => {
    if (orders.length === 0) {
      getOrders();
    }
  }, [getOrders, orders.length]);

  return {
    orders,
    getOrder,
    setOrders,
    createOrder,
    updateOrder,
    deleteOrder,
    getOrders,
  };
};
