import ProductDetails from "./components/productDetails";

export default async function ProductPage({
  params,
}: {
  params: Promise<{ id: number }>;
}) {
  const { id } = await params;
  return <ProductDetails id={id} />;
}
