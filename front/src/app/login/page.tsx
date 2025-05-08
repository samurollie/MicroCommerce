"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Button, Card, Input, Text, VStack } from "@chakra-ui/react";
import Link from "next/link";
import { UserService } from "@/services/user";
import { toaster } from "@/components/ui/toaster";

export default function LoginPage() {
  const pageToRedirect = new URLSearchParams(window.location.search).get(
    "redirectTo"
  );
  // const redirectTo = pageToRedirect ? `/${pageToRedirect}` : "/";
  const { loginUser } = UserService();
  const router = useRouter();
  const [credentials, setCredentials] = useState({ username: "", password: "" });

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await loginUser(credentials.username, credentials.password).then(() => {
        router.push(pageToRedirect ?? "/" );
      });
    } catch (error) {
      console.error("Erro ao fazer login:", error);
      toaster.create({
        title: "Erro ao fazer login",
        description: "Verifique suas credenciais e tente novamente",
        type: "error",
        duration: 5000,
      });
    }
  };

  return (
    <Card.Root maxW="md" mx="auto" mt={8} p="8">
      <form onSubmit={handleLogin}>
        <VStack gap={4} align={"flex-start"}>
          <Text>Nome de usuário</Text>
          <Input
            type="username"
            value={credentials.username}
            onChange={(e) =>
              setCredentials((prev) => ({ ...prev, username: e.target.value }))
            }
          />
          <Text>Senha</Text>
          <Input
            type="password"
            value={credentials.password}
            onChange={(e) =>
              setCredentials((prev) => ({
                ...prev,
                password: e.target.value,
              }))
            }
          />
          <Button type="submit" bgColor="blue" width="full">
            Entrar
          </Button>
          <Link href="/register">
            <Text color="blue.500" textAlign="center">
              Não tem uma conta? Crie uma!
            </Text>
          </Link>
        </VStack>
      </form>
    </Card.Root>
  );
}
