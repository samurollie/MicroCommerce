package com.microcommerce.catalogue.config;

import com.microcommerce.catalogue.models.ProductModel;
import com.microcommerce.catalogue.models.ProductType;
import com.microcommerce.catalogue.repos.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                List<ProductModel> products = new ArrayList<>();
                Random random = new Random();

                for (ProductType type : ProductType.values()) {
                    List<String> productNames = getProductNames(type);
                    String sellerName = getSeller(type);

                    for (int i = 0; i < productNames.size(); i++) {
                        ProductModel product = new ProductModel();
                        String productName = productNames.get(i);
                        product.setName(productName);
                        product.setDescription(getDescription(type, i));
                        product.setPrice((float) (Math.round(random.nextFloat(500) * 1000 + 1) / 100.0));
                        product.setRating(Math.round(random.nextFloat() * 40 + 10) / 10.0f);
                        product.setSeller(sellerName);
                        product.setType(type);
                        product.setQuantity(random.nextInt(50) + 1);

                        // Generate image name
//                        String imageName = "png-transparent-laptop-intel-core-i7-lenovo-ideapad-notebook-miscellaneous-electronics-gadget.png";
                        String saveDir = "/images/";
                        String imageFileName = type.name().toLowerCase() + "_" + i + ".jpg";
                        String savePath = saveDir + imageFileName;

                        downloadAndSaveImage(type, i, productName, savePath);

                        boolean downloaded = downloadAndSaveImage(type, i, productName, savePath);
                        if (downloaded) {
                            product.setImageName(imageFileName);
                        } else {
                            product.setImageName("png-transparent-laptop-intel-core-i7-lenovo-ideapad-notebook-miscellaneous-electronics-gadget.png");
                        }

                        products.add(product);
                    }
                }

                productRepository.saveAll(products);
            }
        };
    }

    private boolean downloadAndSaveImage(ProductType type, int index, String productName, String savePath) {
        try {
            File directory = new File(savePath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String searchTerm = URLEncoder.encode(type.name().toLowerCase(), StandardCharsets.UTF_8);
            URL url = new URL("https://loremflickr.com/640/480/" + searchTerm);

            try (InputStream in = url.openStream();
                 FileOutputStream out = new FileOutputStream(savePath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("Downloaded image for " + productName + " to " + savePath);
            }
        } catch (Exception e) {
            System.err.println("Error downloading image for " + productName + ": " + e.getMessage());
        }
    }

    private String getSeller(ProductType type) {
        return switch (type) {
            case FOOD -> "Mercado Fresco";
            case BOOK -> "Livraria Cultura";
            case ELECTRONIC -> "Tech & Co";
            case CLOTHING -> "Fashion Avenue";
            case FURNITURE -> "Casa & Design";
            case TOY -> "Mundo dos Brinquedos";
            case COSMETIC -> "Beleza Natural";
            case STATIONERY -> "Escritório Moderno";
            case SPORT -> "Esporte Total";
            case OTHER -> "Variedades";
        };
    }

    private List<String> getProductNames(ProductType type) {
        return switch (type) {
            case FOOD -> List.of(
                    "Café Especial Bourbon", "Chocolate 70% Cacau", "Azeite Extra Virgem",
                    "Mix de Castanhas", "Queijo Brie", "Vinho Tinto Reserva",
                    "Pasta de Amendoim", "Geleia de Frutas Vermelhas", "Mel Orgânico", "Biscoitos Artesanais"
            );
            case BOOK -> List.of(
                    "O Poder do Hábito", "Sapiens: Uma Breve História da Humanidade",
                    "A Psicologia Financeira", "Pai Rico, Pai Pobre", "O Alquimista",
                    "Mindset: A Nova Psicologia do Sucesso", "A Sutil Arte de Ligar o F*da-se",
                    "1984", "Cem Anos de Solidão", "A Revolução dos Bichos"
            );
            case ELECTRONIC -> List.of(
                    "Smartphone Galaxy S23", "Notebook Dell XPS", "Smart TV 55\" OLED",
                    "Fone de Ouvido Bluetooth", "Câmera DSLR", "Smartwatch",
                    "Console PlayStation 5", "Tablet iPad Air", "Monitor 27\" 4K", "Caixa de Som Portátil"
            );
            case CLOTHING -> List.of(
                    "Camisa Social Slim", "Vestido Longo Casual", "Calça Jeans Premium",
                    "Blazer Moderno", "Tênis Casual", "Jaqueta de Couro",
                    "Pijama Confortável", "Suéter de Lã", "Camiseta Básica", "Shorts Esportivo"
            );
            case FURNITURE -> List.of(
                    "Sofá Retrátil 3 Lugares", "Mesa de Jantar 6 Cadeiras", "Poltrona Reclinável",
                    "Cama Box Queen", "Estante Modular", "Mesa de Centro",
                    "Escrivaninha Home Office", "Guarda-Roupa 6 Portas", "Rack para TV", "Cadeira Ergonômica"
            );
            case TOY -> List.of(
                    "LEGO City", "Boneca Articulada", "Quebra-Cabeça 1000 Peças",
                    "Carrinho Controle Remoto", "Jogo de Tabuleiro Monopoly", "Pelúcia Antialérgica",
                    "Kit Massinha Modelar", "Pista Hot Wheels", "Jogo UNO", "Nerf Elite"
            );
            case COSMETIC -> List.of(
                    "Protetor Solar FPS 50", "Shampoo Hidratante", "Perfume Importado",
                    "Creme Facial Anti-idade", "Batom Matte", "Base Líquida",
                    "Óleo Corporal", "Máscara Capilar", "Paleta de Sombras", "Sérum Vitamina C"
            );
            case STATIONERY -> List.of(
                    "Caderno Universitário", "Caneta Esferográfica Premium", "Mochila Executiva",
                    "Agenda 2023", "Kit Lápis de Cor", "Bloco de Notas Adesivas",
                    "Lapiseira 0.7mm", "Marcador de Texto", "Fichário", "Papel Sulfite A4"
            );
            case SPORT -> List.of(
                    "Bola de Futebol", "Raquete de Tênis", "Tênis para Corrida",
                    "Mochila Esportiva", "Luvas de Boxe", "Kit Yoga",
                    "Bicicleta Ergométrica", "Corda de Pular", "Kettlebell 10kg", "Bermuda Térmica"
            );
            case OTHER -> List.of(
                    "Kit Jardinagem", "Carregador Portátil", "Mala de Viagem",
                    "Kit Ferramentas", "Relógio de Parede", "Guarda-Chuva Automático",
                    "Porta-Retrato Digital", "Purificador de Ar", "Kit Churrasco", "Caixa Organizadora"
            );
        };
    }

    private String getDescription(ProductType type, int index) {
        return switch (type) {
            case FOOD -> switch (index) {
                case 0 -> "Café especial 100% arábica torrado em pequenos lotes, com notas de chocolate e caramelo.";
                case 1 -> "Chocolate artesanal com 70% de cacau, sem adição de açúcares refinados.";
                case 2 -> "Azeite extra virgem prensado a frio, acidez máxima de 0,2%, origem portuguesa.";
                case 3 -> "Mix selecionado de castanhas do Pará, amêndoas, nozes e avelãs tostadas.";
                case 4 -> "Queijo Brie cremoso produzido com leite pasteurizado, maturação de 30 dias.";
                case 5 -> "Vinho tinto reserva da região do Douro, envelhecido em barris de carvalho por 18 meses.";
                case 6 -> "Pasta de amendoim integral, sem adição de açúcar ou conservantes.";
                case 7 -> "Geleia artesanal de frutas vermelhas frescas, baixo teor de açúcar.";
                case 8 -> "Mel orgânico de flores silvestres, não pasteurizado e sem aditivos.";
                case 9 -> "Biscoitos artesanais de aveia e passas, receita tradicional.";
                default -> "Alimento artesanal de alta qualidade.";
            };
            case BOOK -> switch (index) {
                case 0 -> "Charles Duhigg explora como os hábitos funcionam e como podem ser transformados.";
                case 1 -> "Yuval Noah Harari traça uma narrativa fascinante sobre a história da humanidade.";
                case 2 -> "Morgan Housel apresenta lições atemporais sobre dinheiro e felicidade.";
                case 3 -> "Robert Kiyosaki conta a história de seus dois pais e as diferentes visões sobre dinheiro.";
                case 4 -> "Clássico de Paulo Coelho sobre um jovem pastor que segue seus sonhos.";
                case 5 -> "Carol Dweck revela como o sucesso pode ser influenciado pela nossa mentalidade.";
                case 6 -> "Mark Manson apresenta uma abordagem contracultural para viver uma vida melhor.";
                case 7 -> "Distopia de George Orwell que retrata um futuro totalitário.";
                case 8 -> "Obra-prima de Gabriel García Márquez que narra a história da família Buendía.";
                case 9 -> "Fábula de George Orwell sobre totalitarismo e revoluções corrompidas.";
                default -> "Livro best-seller premiado pela crítica.";
            };
            // Mais cases para outros tipos...
            default -> "Produto de alta qualidade, desenvolvido com os melhores materiais e tecnologia avançada.";
        };
    }
}