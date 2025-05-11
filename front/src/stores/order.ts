import { Order, OrderStatus } from "@/models/order";
import { create } from "zustand";

type OrderStore = {
  orders: Order[];
  setOrders: (orders: Order[]) => void;
  addOrder: (order: Order) => void;
  removeOrder: (orderId: number) => void;
  updateOrderStatus: (orderId: number, status: OrderStatus) => void;
};

export const OrderStore = create<OrderStore>()((set) => ({
  orders: [],
  setOrders: (orders: Order[]) => set({ orders: orders }),
  addOrder: (order: Order) =>
    set((state) => ({ orders: [...state.orders, order] })),
  removeOrder: (orderId: number) =>
    set((state) => ({
      orders: state.orders.filter((order) => order.id !== orderId),
    })),
  updateOrderStatus: (orderId: number, status: OrderStatus) =>
    set((state) => ({
      orders: state.orders.map((order) =>
        order.id === orderId ? { ...order, status: status } : order
      ),
    })),
}));
