import { Order, OrderStatus } from "@/models/order";
import { OrderStore } from "@/stores/order";
import { useCallback } from "react";

export const OrderService = () => {
  const orders = OrderStore((state) => state.orders);
  const setOrders = OrderStore((state) => state.setOrders);
  const addOrder = OrderStore((state) => state.addOrder);
  const removeOrder = OrderStore((state) => state.removeOrder);
  const updateOrderStatus = OrderStore((state) => state.updateOrderStatus);

  const fetchOrders = useCallback(async () => {
    const response = await fetch("/api/orders");
    if (!response.ok) {
      console.error(response);
      return null;
    }
    return response.json();
  }, []);

  const fetchCreateOrder = useCallback(async (order: Order) => {
    const response = await fetch("/api/orders", {
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
      const response = await fetch(`/api/orders/${orderId}`, {
        method: "PUT",
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
    },
    []
  );

  const fetchDeleteOrder = useCallback(async (orderId: number) => {
    const response = await fetch(`/api/orders/${orderId}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      console.error(response);
      return null;
    }
    return response.json();
  }, []);

  const getOrders = useCallback(async () => {
    const orders = await fetchOrders();
    setOrders(orders ?? []);
  }, [fetchOrders, setOrders]);

  const createOrder = useCallback(
    async (order: Order): Promise<Order> => {
      // const newOrder = await fetchCreateOrder(order); TO-DO: integrar com o back
      addOrder(order);
      return order;
    },
    [addOrder]
  );

  const updateOrder = useCallback(
    async (orderId: number, status: OrderStatus) => {
      // const updatedOrder = await fetchUpdateOrder(orderId, { status }); TO
      // if (updatedOrder) {
      updateOrderStatus(orderId, status);
      // }
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

  return {
    orders,
    setOrders,
    createOrder,
    updateOrder,
    deleteOrder,
    getOrders,
  };
};
