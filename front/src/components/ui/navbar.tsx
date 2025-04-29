import { Box, HStack, Link } from "@chakra-ui/react";
import NavOption, { NavOptionProps } from "./navoption";
import { getAvailableRoutes } from "@/utils/navigation";

export default function NavBar() {
  const options: NavOptionProps[] = getAvailableRoutes();

  return (
    <HStack
      w="full"
      h="65px"
      bgColor={"purple"}
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
