import { RegisterDTO } from "@/models/dtos/register";
import { User, UserRoles } from "@/models/user";
import { UserStore } from "@/stores/user";
import { useCallback } from "react";

export const UserService = () => {
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

  const setCurrentUser = useCallback(
    (user: User) => {
      setUser(user);
    },
    [setUser]
  );

  const loginUser = useCallback(
    async (email: string, password: string) => {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        document.cookie = `token=${data.token}; path=/`;
        setCurrentUser(data.user);
      } else {
        throw new Error("Login failed");
      }
    },
    [setCurrentUser]
  );

  const registerUser = useCallback(async (user: RegisterDTO) => {
    const response = await fetch("http://localhost:8080/api/auth/signup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    });

    if (!response.ok) {
      throw new Error("Registration failed");
    }
  }, []);

  return { getCurrentUser, setCurrentUser, loginUser, registerUser };
};
