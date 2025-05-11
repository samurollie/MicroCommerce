import { PaymentMethod } from "@/models/paymentMethod";
import { ProductType } from "@/models/product";
import { SetupStore } from "@/stores/setup";

export const SetupService = () => {
  const { productType, paymentMethod } = SetupStore.getState();

  const setProductType = (productType: ProductType[]) => {
    SetupStore.setState({ productType });
  };

  const setPaymentMethod = (paymentMethod: PaymentMethod[]) => {
    SetupStore.setState({ paymentMethod });
  };

  return {
    productType,
    paymentMethod,
    setProductType,
    setPaymentMethod,
  };
};
