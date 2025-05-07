"use client";

import { Product } from "@/models/product";
import { ProductStore } from "@/stores/products";
import { useCallback, useEffect } from "react";

export const CatalogueService = () => {
  const products = ProductStore((state) => state.products);
  const setProducts = ProductStore((state) => state.setProducts);
  const removeProduct = ProductStore((state) => state.removeProduct);
  const addProducts = ProductStore((state) => state.addProducts);

  const getProducts = useCallback(async () => {
    const existingProducts = await fetchProducts();
    setProducts(existingProducts ?? []);
  }, [setProducts]);

  const fetchProducts = async (): Promise<Product[]> => {
    const response = await fetch("http://localhost:8080/api/catalogue");
    if (!response.ok) {
      throw new Error("Failed to fetch products");
    }
    return response.json();
  };

  const removeProducts = useCallback(
    async (productId: number, quantity: number = 1) => {
      removeProduct(productId, quantity);
    },
    [removeProduct]
  );

  const addProduct = useCallback(
    async (product: Product, quantity: number = 1) => {
      addProducts(product, quantity);
    },
    [addProducts]
  );

  const getProduct = useCallback(
    (id: number) => products.find((p) => p.id === id),
    [products]
  );

  useEffect(() => {
    if (products.length === 0) {
      getProducts();
    }
  }, [products.length, getProducts]);

  return { products, getProducts, removeProducts, addProduct, getProduct };
};
