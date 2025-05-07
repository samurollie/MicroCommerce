import { Heading, Separator } from "@chakra-ui/react";
import CartList from "./components/CartList";

export default function CataloguePage() {
  return (
    <>
      <Heading size="2xl" color="#2c2d97" fontWeight="bold">
        Meu carrinho
      </Heading>
      <Separator w="full" />
      <CartList />
    </>
  );
}
