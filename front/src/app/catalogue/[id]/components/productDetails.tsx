"use client";

import { CatalogueService } from "@/services/product";
import { Breadcrumb, Heading, IconButton } from "@chakra-ui/react";
import { useRouter } from "next/navigation";

export default function ProductDetails({ id }: { id: number }) {
  const router = useRouter();

  const { products } = CatalogueService();
  const product = products.find((product) => product.id === Number(id));

  if (!product) {
    return <div>Product not found</div>;
  }

  return (
    <div>
      <Breadcrumb.Root>
        <Breadcrumb.List>
          <Breadcrumb.Item>
            <Breadcrumb.Link href="/catalogue">Catálogo</Breadcrumb.Link>
          </Breadcrumb.Item>
          <Breadcrumb.Separator></Breadcrumb.Separator>
          <Breadcrumb.Item>
            <Breadcrumb.CurrentLink>{product.name}</Breadcrumb.CurrentLink>
          </Breadcrumb.Item>
        </Breadcrumb.List>
      </Breadcrumb.Root>
      <Heading>{product.name}</Heading>
    </div>
  );
}
