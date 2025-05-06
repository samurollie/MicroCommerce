import { User, UserRoles } from "@/models/user";
import { UserStore } from "@/stores/user";

export const userService = () => {
  const { user, setUser } = UserStore();

  const getCurrentUser = (): User | undefined => {
    // QUando integrar com o login, colocar para retornar user;
    // return user;
    return {
      id: 1,
      name: "John Doe",
      email: "email@email.com",
      isActive: true,
      password: "password",
      username: "johndoe",
      phone: "1234567890",
      roles: [UserRoles.ADMIN, UserRoles.USER, UserRoles.SERVICE],
    };
  };

  const setCurrentUser = (user: User) => {
    setUser(user);
  };

  return { getCurrentUser, setCurrentUser };
};
