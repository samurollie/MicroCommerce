export type Product = {
  id: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  rating: number;
  type: ProductType;
  seller: string;
  imageName: string;
  imageUrl: string;
};

export enum ProductType {
  FOOD = "COMIDA",
  BOOK = "LIVRO",
  ELECTRONIC = "ELETRONICO",
  CLOTHING = "ROUPA",
  FURNITURE = "MOVEIS",
  TOY = "BRINQUEDO",
  COSMETIC = "COSMETICO",
  STATIONERY = "PAPELARIA",
  SPORT = "ESPORTE",
  OTHER = "OUTROS",
}
