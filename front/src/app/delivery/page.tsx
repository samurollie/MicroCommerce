import { Heading, Separator, Text } from "@chakra-ui/react";
import DeliveryList from "./components/DeliveryList";

export default function DeliveryPage() {
  return (
    <>
      <Heading size="2xl" color="#2c2d97" fontWeight="bold">
        Entrega
      </Heading>
      <Separator w="full" />
      <DeliveryList />
    </>
  );
}
