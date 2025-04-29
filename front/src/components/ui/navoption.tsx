import { Link as ChakraLink } from "@chakra-ui/react";
import Link from "next/link";

export type NavOptionProps = {
  label: string;
  href: string;
};

export default function NavOption({ label, href }: NavOptionProps) {
  return (
    <div>
      <ChakraLink as={Link} href={href} fontSize="lg" color={"white"}>
        {label}
      </ChakraLink>
    </div>
  );
}
