import { Product } from "./product";

export type Order = {
  id: number;
  product: Product;
  status: "pending" | "completed" | "cancelled";
  quantity: number;
};
