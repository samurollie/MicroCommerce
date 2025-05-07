import { Product } from "@/models/product";
import { CartStore } from "@/stores/cart";
import { useCallback, useEffect } from "react";

export const CartService = () => {
  const items = CartStore((state) => state.items);
  const addItem = CartStore((state) => state.addItem);
  const subtractItem = CartStore((state) => state.subtractItem);
  const removeItem = CartStore((state) => state.removeItem);
  const clearCart = CartStore((state) => state.clearCart);
  const total = CartStore((state) => state.total);

  const getCart = useCallback(async () => {
    const productsOnCart = await fetchCart();
    if (productsOnCart) {
      productsOnCart.forEach((product) => {
        addItem(product, product.quantity);
      });
    }
  }, [addItem]);

  const addToCart = useCallback(
    async (product: Product, quantity: number) => {
      addItem(product, quantity);
    },
    [addItem]
  );

  const fetchCart = async (): Promise<Product[]> => {
    const response = await fetch("http://localhost:8080/api/cart");
    if (!response.ok) {
      return [];
    }
    return response.json();
  };

  useEffect(() => {
    if (items.length === 0) {
      getCart();
    }
  }, [items.length, getCart]);

  return { items, total, getCart, addToCart };
};
