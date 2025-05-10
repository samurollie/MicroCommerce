import PaymentComponent from "./components/PaymentComponent";

export default async function CheckoutPage({
  params,
}: {
  params: Promise<{ id: number }>;
}) {
  const { id } = await params;

  return <PaymentComponent id={id} />;
}
