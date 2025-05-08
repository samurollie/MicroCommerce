import { Product } from "./product";

export type Order = {
  id: string;
  status: OrderStatus;
  customerId: string;
  items: Product[];
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
  SHIPPED = "Enviados",
  DELIVERED = "Entregues",
  CANCELLED = "Cancelados",
  FAILED = "Falhados",
}
