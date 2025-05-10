import { Product } from "./product";

export type Order = {
  id: number;
  status: OrderStatus;
  customerId: string;
  items: OrderItem[];
  totalPrice: number;
};

export type OrderItem = {
  id: string;
  productId: string;
  name: string;
  price: number;
  quantity: number;
};

export enum OrderStatus {
  PENDING_PAYMENT = "Aguardando pagamento",
  PROCESSING = "Processando",
  SHIPPED = "A caminho",
  DELIVERED = "Entregue",
  CANCELLED = "Cancelado",
  FAILED = "Devolução",
}
