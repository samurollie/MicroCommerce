import { Product } from "@/models/product";
import { create } from "zustand";

export type ProductStore = {
  products: Product[];
  recommendations: Product[];
  setProducts: (products: Product[]) => void;
  setRecommendations: (products: Product[]) => void;
  removeProduct: (productId: number, quantity: number) => void;
  addProducts: (product: Product, quantity?: number) => void;
  updateProduct: (productId: number, updatedProduct: Product) => void;
};

export const ProductStore = create<ProductStore>()((set) => ({
  products: [],
  recommendations: [],
  setProducts: (products: Product[]) => set({ products: products }),
  setRecommendations: (products: Product[]) => set({ recommendations: products }),
  removeProduct: (productId: number, quantity: number = 1) =>
    set((state) => {
      const product = state.products.find((p) => p.id === productId);
      if (product && product.quantity - quantity > 0) {
        return {
          products: state.products.map((p) =>
            p.id === productId
              ? { ...p, quantity: (p.quantity || 0) - quantity }
              : p
          ),
        };
      }
      return {
        products: state.products.filter((product) => product.id !== productId),
      };
    }),
  addProducts: (product: Product, quantity: number = 1) =>
    set((state) => {
      const productId = product.id;
      const existingProduct = state.products.find((p) => p.id === productId);
      if (existingProduct) {
        return {
          products: state.products.map((p) =>
            p.id === productId
              ? { ...p, quantity: (p.quantity || 0) + quantity }
              : p
          ),
        };
      }

      return {
        products: [...state.products, product],
      };
    }),
  updateProduct: (productId: number, updatedProduct: Product) =>
    set((state) => ({
      products: state.products.map((product) =>
        product.id === productId ? updatedProduct : product
      ),
    })),
}));
