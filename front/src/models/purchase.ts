import { Product } from "./product";

export type Purchase = {
  id: number;
  product: Product;
  status: "pending" | "completed" | "cancelled";
  quantity: number;
};
