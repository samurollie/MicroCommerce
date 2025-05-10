import { create } from "zustand";

type DeliveryStore = {
  cep: string;
  values: number[];
  setCep: (cep: string) => void;
  setValues: (values: number[]) => void;
};

export const DeliveryStore = create<DeliveryStore>((set) => ({
  cep: "",
  values: [],
  setCep: (cep: string) => set({ cep }),
  setValues: (values: number[]) => set({ values }),
}));
