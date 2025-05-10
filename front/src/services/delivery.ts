import { DeliveryStore } from "@/stores/delivery";
import { useCallback } from "react";

export const DeliveryService = () => {
  const cep = DeliveryStore((state) => state.cep);
  const setCep = DeliveryStore((state) => state.setCep);
  const values = DeliveryStore((state) => state.values);
  const setValues = DeliveryStore((state) => state.setValues);

  const fetchDeliveryValue = useCallback(async (cep: string) => {
    /* const response = await fetch(`/api/delivery`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ zipCode: cep }),
    });
    if (!response.ok) {
      console.error(response);
      return null;
    }
    return response.json(); */
    return 100;
  }, []);

  const generateDeliveryValues = useCallback(
    async (cep: string) => {
      const deliveryValues: number[] = [];

      for (let i = 0; i < 3; i++) {
        const response = await fetchDeliveryValue(cep);
        if (!response) return;
        deliveryValues.push(response * (i + 1));
      }

      setValues(deliveryValues);
    },
    [fetchDeliveryValue, setValues]
  );

  return { cep, setCep, values, generateDeliveryValues };
};
