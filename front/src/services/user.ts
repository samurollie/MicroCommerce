"use client";

import { RegisterDTO } from "@/models/dtos/register";
import { User } from "@/models/user";
import { UserStore } from "@/stores/user";
import { useCallback, useEffect } from "react";

export const UserService = () => {
  const user = UserStore((state) => state.user);
  const setUser = UserStore((state) => state.setUser);

  const getToken = () => {
    const cookies = document.cookie.split(";");
    const tokenCookie = cookies.find((cookie) =>
      cookie.trim().startsWith("token=")
    );
    return tokenCookie ? tokenCookie.split("=")[1] : null;
  };

  const fetchUser = useCallback(async () => {
    /* const token = getToken();

    if (!token) {
      console.log("Token não encontrado"); // Debug
      UserStore.getState().clearUser(); // Limpa o usuário se não houver token
      // throw new Error("No token found");
    }

    try {
      const response = await fetch("http://localhost:8080/users/me", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
          credentials: "include",
        },
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.log("Erro na resposta:", errorData);
        if (response.status === 401) {
          UserStore.getState().clearUser();
        }
        // throw new Error("Failed to fetch user");
      }

      return response.json();
    } catch (error) {
      console.error("Erro na requisição:", error);
      throw error;
    } */
    return null;
  }, []);

  const getCurrentUser = useCallback(async () => {
    // Se já tiver usuário no store e o token for válido, usa ele
    if (user && getToken()) {
      return user;
    }

    try {
      const loggedUser = await fetchUser();
      setUser(loggedUser);
    } catch (error) {
      console.error("Erro ao buscar usuário:", error);
      setUser(undefined);
    }
  }, [fetchUser, setUser, user]);

  const setCurrentUser = useCallback(
    (user: User) => {
      setUser(user);
    },
    [setUser]
  );

  const loginUser = useCallback(
    async (username: string, password: string) => {
      const response = await fetch("http://localhost:8080/api/auth/signin", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const data = await response.json();
        // Definindo uma data de expiração e outras configurações importantes
        document.cookie = `token=${data.token}; path=/; max-age=86400; samesite=strict`;
        await getCurrentUser();
      } else {
        const json = await response.json();
        console.error(json);
        throw new Error("Login failed");
      }
    },
    [getCurrentUser]
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
      console.error(response);
      throw new Error("Registration failed");
    }
  }, []);

  useEffect(() => {
    getCurrentUser();
  }, [getCurrentUser]);

  return { user, setCurrentUser, loginUser, registerUser };
};
