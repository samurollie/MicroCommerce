import { Product } from "@/models/product";
import { CartService } from "@/services/cart";
import { CatalogueService } from "@/services/product";
import {
  Box,
  HStack,
  Text,
  FormatNumber,
  NumberInput,
  IconButton,
} from "@chakra-ui/react";
import Image from "next/image";
import { LuMinus, LuPlus } from "react-icons/lu";

export default function CartListItem({
  item,
  showNumberInput = true,
}: {
  item: Product;
  showNumberInput?: boolean;
}) {
  const { getProduct, addProduct, removeProducts } = CatalogueService();
  const { addToCart, removeFromCart } = CartService();
  const maxQuantity = getProduct(item.id)?.quantity || 0;
  const updateItem = (value: number) => {
    if (value < item.quantity) {
      removeFromCart(item.id, 1);
      addProduct(item, 1);
    } else if (value > item.quantity) {
      addToCart(item, 1);
      removeProducts(item.id, 1);
    }
  };

  return (
    <HStack
      borderWidth={1}
      w="full"
      borderRadius={"md"}
      p={4}
      gap={4}
      justify={"space-between"}
    >
      <HStack gap={4} w="full">
        <Image
          src="https://placehold.co/60x60/png"
          alt="Placeholder"
          width={60}
          height={60}
        />
        <Box>
          <Text fontWeight="bold">{item.name}</Text>
          <Text mt={1} color="gray.500">
            {item.seller}
          </Text>
        </Box>
      </HStack>
      <HStack>
        {showNumberInput && (
          <NumberInput.Root
            value={item.quantity.toString()}
            onValueChange={(e) => updateItem(e.valueAsNumber)}
            unstyled
            spinOnPress={false}
            max={maxQuantity}
            min={0}
            step={1}
          >
            <HStack gap="2">
              <NumberInput.DecrementTrigger asChild>
                <IconButton variant="outline" size="sm">
                  <LuMinus />
                </IconButton>
              </NumberInput.DecrementTrigger>
              <NumberInput.ValueText
                textAlign="center"
                fontSize="lg"
                minW="3ch"
              />
              <NumberInput.IncrementTrigger asChild>
                <IconButton variant="outline" size="sm">
                  <LuPlus />
                </IconButton>
              </NumberInput.IncrementTrigger>
            </HStack>
          </NumberInput.Root>
        )}

        <Text fontWeight="bold" color="gray.500">
          {item.quantity}x
        </Text>
        <Text fontWeight="bold" color="gray.500">
          <FormatNumber value={item.price} style="currency" currency="BRL" />
        </Text>
      </HStack>
    </HStack>
  );
}
