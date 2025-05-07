import { Product } from "@/models/product";
import { create } from "zustand";

type CartStore = {
  items: Product[];
  total: number;
  addItem: (product: Product, quantity?: number) => void;
  subtractItem: (productId: number) => void;
  removeItem: (productId: number) => void;
  clearCart: () => void;
  calculateTotal: () => void;
};

export const CartStore = create<CartStore>()((set) => ({
  items: [],
  total: 0,
  addItem: (product: Product, quantity?: number) =>
    set((state) => {
      const existingItem = state.items.find((item) => item.id === product.id);
      if (existingItem) {
        return {
          items: state.items.map((item) =>
            item.id === product.id
              ? { ...item, quantity: quantity ?? item.quantity + 1 }
              : item
          ),
          total: state.total + product.price,
        };
      } else {
        return {
          items: [...state.items, { ...product, quantity: quantity ?? 1 }],
          total: state.total + product.price,
        };
      }
    }),
  subtractItem: (productId: number) =>
    set((state) => {
      const itemToSubtract = state.items.find((item) => item.id === productId);
      if (itemToSubtract && itemToSubtract.quantity > 1) {
        return {
          items: state.items.map((item) =>
            item.id === productId
              ? { ...item, quantity: item.quantity - 1 }
              : item
          ),
          total: state.total - itemToSubtract.price,
        };
      } else if (itemToSubtract && itemToSubtract.quantity <= 1) {
        const newItems = state.items.filter((item) => item.id !== productId);
        return {
          items: newItems,
          total: state.total - itemToSubtract.price,
        };
      }
      return state;
    }),
  removeItem: (productId: number) =>
    set((state) => {
      const itemToRemove = state.items.find((item) => item.id === productId);
      if (itemToRemove) {
        const newItems = state.items.filter((item) => item.id !== productId);
        return {
          items: newItems,
          total: state.total - itemToRemove.price * itemToRemove.quantity,
        };
      }
      return state;
    }),
  clearCart: () =>
    set(() => ({
      items: [],
      total: 0,
    })),
  calculateTotal: () =>
    set((state) => ({
      total: state.items.reduce(
        (acc, item) => acc + item.price * item.quantity,
        0
      ),
    })),
}));
