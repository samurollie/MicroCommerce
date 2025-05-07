"use client";

import { toaster } from "@/components/ui/toaster";
import { RegisterDTO } from "@/models/dtos/register";
import { UserService } from "@/services/user";
import {
  Box,
  Button,
  Card,
  HStack,
  Input,
  Link,
  Text,
  VStack,
} from "@chakra-ui/react";
import { useState } from "react";

export default function RegisterPage() {
  const { registerUser } = UserService();

  const [credentials, setCredentials] = useState<RegisterDTO>(
    {} as RegisterDTO
  );

  const handleRegister = async () => {
    try {
      await registerUser(credentials).then(() => {
        toaster.create({
          title: "Cadastro realizado com sucesso",
          description: "Você pode fazer login agora",
          type: "success",
          duration: 5000,
        });
      });
    } catch (error) {
      console.error("Erro ao cadastrar usuário:", error);
      toaster.create({
        title: "Erro ao cadastrar usuário",
        description: "Verifique os dados e tente novamente",
        type: "error",
        duration: 5000,
      });
    }
  };

  return (
    <Card.Root mx="auto" mt={8} p="8">
      <form onSubmit={handleRegister}>
        <VStack gap={4} align={"flex-start"}>
          <HStack>
            <Box>
              <Text>Nome</Text>
              <Input
                value={credentials.firstName}
                onChange={(e) =>
                  setCredentials((prev) => ({
                    ...prev,
                    firstName: e.target.value,
                  }))
                }
              />
            </Box>
            <Box>
              <Text>Sobrenome</Text>
              <Input
                value={credentials.lastName}
                onChange={(e) =>
                  setCredentials((prev) => ({
                    ...prev,
                    lastName: e.target.value,
                  }))
                }
              />
            </Box>
          </HStack>
          <Text>Nome de usuário</Text>
          <Input
            value={credentials.username}
            onChange={(e) =>
              setCredentials((prev) => ({ ...prev, username: e.target.value }))
            }
          />
          <Text>Email</Text>
          <Input
            type="email"
            value={credentials.email}
            onChange={(e) =>
              setCredentials((prev) => ({ ...prev, email: e.target.value }))
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
          <Button onClick={handleRegister} bgColor="#2c2d97" width="full">
            Criar conta
          </Button>
          <Link href="/login" color="blue">
            Já tem uma conta? Faça login
          </Link>
        </VStack>
      </form>
    </Card.Root>
  );
}
