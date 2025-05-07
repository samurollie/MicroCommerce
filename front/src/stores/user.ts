import { User } from "@/models/user";
import { create } from "zustand";

type UserStore = {
  user: User;
  setUser: (user: User) => void;
  clearUser: () => void;
};

export const UserStore = create<UserStore>()((set) => ({
  user: {} as User,
  setUser: (user: User) => set({ user }),
  clearUser: () => set({ user: {} as User}),
}));
