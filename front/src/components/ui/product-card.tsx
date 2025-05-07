import { Product } from "@/models/product";
import { Box, Heading, RatingGroup } from "@chakra-ui/react";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function ProductCard({ product }: { product: Product }) {
  const router = useRouter();

  return (
    <Box
      p={4}
      borderWidth={1}
      borderRadius="md"
      _hover={{ cursor: "pointer" }}
      onClick={() => router.push(`/product/${product.id}`)}
    >
      <Image
        src="https://placehold.co/215x215/png"
        alt="Placeholder"
        width={215}
        height={215}
      />
      <div>
        <Heading size="md">{product.name}</Heading>
        <Box mt={2} fontWeight="bold">
          R$ {product.price}
        </Box>
        <RatingGroup.Root
          count={5}
          defaultValue={product.rating}
          size="sm"
          colorPalette={"blue"}
          readOnly
        >
          <RatingGroup.HiddenInput />
          <RatingGroup.Control />
        </RatingGroup.Root>
        <Box mt={2} color="gray.500">
          {product.seller}
        </Box>
      </div>
    </Box>
  );
}
