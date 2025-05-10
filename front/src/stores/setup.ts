import { PaymentMethod } from "@/models/paymentMethod";
import { ProductType } from "@/models/product";
import { create } from "zustand";

export type SetupStore = {
  productType: ProductType[];
  paymentMethod: PaymentMethod[];
  setProductType: (productType: ProductType[]) => void;
  setPaymentMethod: (paymentMethod: PaymentMethod[]) => void;
};

export const SetupStore = create<SetupStore>((set) => ({
  productType: Object.keys(ProductType) as ProductType[],
  paymentMethod: Object.values(PaymentMethod),
  setProductType: (productType) => set({ productType }),
  setPaymentMethod: (paymentMethod) => set({ paymentMethod }),
}));
