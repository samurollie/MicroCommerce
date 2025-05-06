export type Product = {
  id: number;
  name: string;
  description: string;
  image: string[] | string;
  seller: string;
  price: number;
  quantity: number;
  rating: number;
  type: ProductType;
  createdAt: Date;
  updatedAt: Date;
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
