"use client";

import { Box, HStack, Link } from "@chakra-ui/react";
import NavOption, { NavOptionProps } from "./navoption";
import { getAvailableRoutes } from "@/utils/navigation";
import { UserService } from "@/services/user";
import { useEffect } from "react";

export default function NavBar() {
  const { user } = UserService();
  const options: NavOptionProps[] = getAvailableRoutes();

  useEffect(() => {
    const token = document.cookie
      .split(";")
      .find((c) => c.trim().startsWith("token="));
    console.log("Token no NavBar:", token);
    console.log("User no NavBar:", user);
  }, [user]);

  return (
    <HStack
      w="full"
      h="65px"
      bgColor={"#2c2d97"}
      borderBottomColor={"black"}
      justify={"space-between"}
      padding={4}
    >
      <Link fontSize={"xl"} fontWeight={"extrabold"} href="/" color={"white"}>
        Microcommerce
      </Link>
      <Box>
        <HStack gap={8}>
          {options.map((option) => (
            <NavOption
              key={option.label}
              label={option.label}
              href={option.href}
            />
          ))}
        </HStack>
      </Box>
    </HStack>
  );
}
