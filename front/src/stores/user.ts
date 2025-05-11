import { User } from "@/models/user";
import { create } from "zustand";
import { persist } from "zustand/middleware";

type UserStore = {
  user: User | null;
  setUser: (user: User | undefined) => void;
  clearUser: () => void;
};

export const UserStore = create<UserStore>()(
  persist(
    (set) => ({
      user: null,
      setUser: (user) => set({ user: user || null }),
      clearUser: () => set({ user: null }),
    }),
    {
      name: 'user-storage', // nome para o storage
    }
  )
);