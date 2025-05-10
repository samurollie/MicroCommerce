import { Product } from "@/models/product";
import { ProductStore } from "@/stores/products";
import { RecommendationStore } from "@/stores/recommendation";
import { useCallback, useEffect } from "react";

export const RecommendationService = () => {
  const recommendations = RecommendationStore((state) => state.recommendations);
  const setRecommendations = RecommendationStore(
    (state) => state.setRecommendations
  );

  const fetchRecommendations = useCallback(
    async (id: number): Promise<Product[]> => {
      const response = await fetch(
        `http://localhost:8080/api/catalogue/${id}/recommendations`
      );
      if (!response.ok) {
        throw new Error("Failed to fetch recommendations");
      }
      return response.json();
    },
    []
  );

  const getRecommendations = useCallback(async () => {
    const products = ProductStore((state) => state.products);
    setRecommendations(products.slice(0, 5));
  }, [setRecommendations]);

  useEffect(() => {
    if (recommendations.length === 0) {
      getRecommendations();
    }
  }, [recommendations.length, getRecommendations]);

  return { recommendations };
};
