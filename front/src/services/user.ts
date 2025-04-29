import { UserRoles, User } from "@/models/user";

export const userService = () => {
  const getCurrentUser = (): User => {
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

  return { getCurrentUser };
};
