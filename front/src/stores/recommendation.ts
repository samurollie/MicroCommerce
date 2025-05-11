import { Product } from "@/models/product";
import { create } from "zustand";

type RecommendationStore = {
  recommendations: Product[];
  setRecommendations: (recommendations: Product[]) => void;
  addRecommendation: (recommendation: Product) => void;
  removeRecommendation: (recommendation: Product) => void;
};

export const RecommendationStore = create<RecommendationStore>()((set) => ({
  recommendations: [],
  setRecommendations: (recommendations: Product[]) => set({ recommendations }),
  addRecommendation: (recommendation: Product) =>
    set((state) => ({
      recommendations: [...state.recommendations, recommendation],
    })),
  removeRecommendation: (recommendation: Product) =>
    set((state) => ({
      recommendations: state.recommendations.filter(
        (rec) => rec.id !== recommendation.id
      ),
    })),
}));
