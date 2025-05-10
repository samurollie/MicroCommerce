export type CreteOrderItemDTO = {
  productId: number;
  name: string;
  price: number;
  quantity: number;
};

export type CreateOrderDTO = {
  customerId: string;
  orderItems: CreteOrderItemDTO[];
  totalPrice: number;
};
