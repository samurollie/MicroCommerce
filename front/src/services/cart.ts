import { Product, ProductType } from "@/models/product";

export const CartService = () => {
  const getCart = (): Product[] => {
    return [
      {
        id: 1,
        name: "Product 1",
        price: 10,
        quantity: 2,
        description: "Description for Product 1",
        image: "image1.jpg",
        rating: 4.5,
        type: ProductType.BOOK,
        seller: "Seller 1",
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 2,
        name: "Product 2",
        price: 20,
        quantity: 1,
        description: "Description for Product 2",
        image: "image2.jpg",
        seller: "Seller 2",
        createdAt: new Date(),
        updatedAt: new Date(),
        type: ProductType.FOOD,
        rating: 5,
      },
    ];
  };

  const addToCart = (product: Product) => {
    console.log("Product added to cart:", product);
  };

  const removeFromCart = (productId) => {
    console.log("Product removed from cart:", productId);
  };

  return { getCart, addToCart, removeFromCart };
};
