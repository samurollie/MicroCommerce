"use client";

import { Product } from "@/models/product";
import { ProductStore } from "@/stores/products";
import { useCallback, useEffect } from "react";

export const CatalogueService = () => {
  const products = ProductStore((state) => state.products);
  const setProducts = ProductStore((state) => state.setProducts);

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

  useEffect(() => {
    if (products.length === 0) {
      getProducts();
    }
  }, [products.length, getProducts]);

  return { products, getProducts };
};