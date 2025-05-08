import { Product } from "./product";

export type Order = {
  id: string; // UUID
  customerId: string;
  // ISO 8601 format
  status: "PENDING_PAYMENT" | "PROCESSING" | "SHIPPED" | "DELIVERED" | "CANCELED" | "FAILED";
  itemsTotalAmount: number; // Represented as a decimal
  shippingAmount: number; // Represented as a decimal
  discountAmount: number; // Represented as a decimal
  grandTotalAmount: number; // Represented as a decimal
  items: OrderItem[];
  shippingAddressId?: string; // Optional
  paymentTransactionId?: string; // Optional
  shippingTrackingId?: string; // Optional
};

export type OrderItem = {
  productId: string;
  productNameSnapshot?: string; // Snapshot of the product name
  quantity: number;
  unitPrice: number; // Represented as a decimal
  subtotal: number; // Represented as a decimal
};
