package com.microcommerice.order.service;

import com.microcommerice.order.entity.Order;
import com.microcommerice.order.entity.OrderItem;
import com.microcommerice.order.enums.OrderStatus;
import com.microcommerice.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final CatalogueClient catalogueClient;
    // Descomente quando os clientes estiverem definidos
    // private final ShippingClient shippingClient;
    // private final PaymentClient paymentClient;

    @Transactional
    public OrderDTO createOrder(String userId, CreateOrderRequestDTO requestDTO) {
        log.info("Iniciando criação de pedido para usuário: {} com request: {}", userId, requestDTO);

        // 1. Buscar itens do carrinho
        CartDTO cart = cartClient.getMyCart(); // O token é propagado pelo interceptor
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            log.warn("Carrinho vazio ou não encontrado para o usuário: {}", userId);
            throw new IllegalStateException("Seu carrinho está vazio.");
        }
        log.debug("Carrinho obtido para usuário {}: {}", userId, cart);

        // 2. Buscar detalhes dos produtos e verificar estoque
        Set<String> productIdsInCart = cart.getItems().stream()
                .map(CartDTO.CartItemExternalDTO::getProductId)
                .collect(Collectors.toSet());
        List<ProductDetailsDTO> productDetailsList = catalogueClient.getProductDetailsBatch(productIdsInCart);
        Map<String, ProductDetailsDTO> productDetailsMap = productDetailsList.stream()
                .collect(Collectors.toMap(ProductDetailsDTO::getId, p -> p));

        log.debug("Detalhes dos produtos obtidos: {}", productDetailsMap);

        Order newOrder = new Order();
        newOrder.setCustomerId(userId);
        newOrder.setStatus(OrderStatus.PENDING_PAYMENT); // Status inicial
        newOrder.setShippingAddressId(requestDTO.getShippingAddressId());
        newOrder.setShippingAmount(requestDTO.getSelectedShippingCost());
        if (requestDTO.getDiscountApplied() != null) {
            newOrder.setDiscountAmount(requestDTO.getDiscountApplied());
        }


        // 3. Processar itens do pedido
        for (CartDTO.CartItemExternalDTO cartItem : cart.getItems()) {
            ProductDetailsDTO productDetails = productDetailsMap.get(cartItem.getProductId());
            if (productDetails == null) {
                log.error("Produto com ID {} não encontrado no catálogo.", cartItem.getProductId());
                throw new IllegalStateException("Produto " + cartItem.getProductId() + " não está mais disponível.");
            }
            if (productDetails.getStockQuantity() == null || productDetails.getStockQuantity() < cartItem.getQuantity()) {
                log.error("Estoque insuficiente para produto ID {}. Solicitado: {}, Disponível: {}",
                        cartItem.getProductId(), cartItem.getQuantity(), productDetails.getStockQuantity());
                throw new IllegalStateException("Estoque insuficiente para o produto: " + productDetails.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productDetails.getId());
            orderItem.setProductNameSnapshot(productDetails.getName()); // Snapshot do nome
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(productDetails.getPrice()); // Preço atual do catálogo
            // orderItem.calculateSubtotal(); // Será chamado no @PrePersist da entidade
            newOrder.addItem(orderItem);
        }

        // newOrder.calculateTotals(); // Será chamado no @PrePersist da entidade

        // 4. (Simplificado) Processar Pagamento - Em um cenário real, isso seria mais complexo
        // Aqui, estamos apenas simulando que o paymentMethodNonce seria usado para chamar o payment-service
        // e obter um paymentTransactionId.
        // PaymentRequestDTO paymentRequest = new PaymentRequestDTO(newOrder.getGrandTotalAmount(), requestDTO.getPaymentMethodNonce());
        // PaymentConfirmationDTO paymentConfirmation = paymentClient.processPayment(paymentRequest);
        // if (!"APPROVED".equals(paymentConfirmation.getStatus())) {
        //     log.error("Pagamento falhou para pedido do usuário {}: {}", userId, paymentConfirmation.getMessage());
        //     newOrder.setStatus(OrderStatus.FAILED);
        //     // Salvar o pedido mesmo com falha no pagamento para referência, ou lançar exceção
        //     orderRepository.save(newOrder);
        //     throw new IllegalStateException("Pagamento falhou: " + paymentConfirmation.getMessage());
        // }
        // newOrder.setPaymentTransactionId(paymentConfirmation.getTransactionId());
        // newOrder.setStatus(OrderStatus.AWAITING_SHIPMENT); // Pagamento aprovado

        // Placeholder para ID de transação de pagamento, já que o cliente não está implementado
        newOrder.setPaymentTransactionId(requestDTO.getPaymentMethodNonce()); // Usando o nonce como ID temporário
        newOrder.setStatus(OrderStatus.AWAITING_SHIPMENT); // Assumindo pagamento bem-sucedido por agora

        // 5. Salvar o pedido
        Order savedOrder = orderRepository.save(newOrder);
        log.info("Pedido {} salvo com sucesso para o usuário {}", savedOrder.getId(), userId);

        // 6. Limpar o carrinho
        try {
            cartClient.clearMyCart();
            log.info("Carrinho limpo para o usuário {}", userId);
        } catch (Exception e) {
            log.error("Falha ao limpar o carrinho para o usuário {} após criar pedido {}. Erro: {}", userId, savedOrder.getId(), e.getMessage());
            // Considerar uma lógica de compensação ou notificação aqui
        }

        return mapEntityToOrderDTO(savedOrder);
    }

    public OrderDTO getOrderByIdAndUserId(UUID orderId, String userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado ou não pertence ao usuário.")); // Criar exceção customizada
        return mapEntityToOrderDTO(order);
    }

    public List<OrderDTO> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        return orders.stream().map(this::mapEntityToOrderDTO).collect(Collectors.toList());
    }

    private OrderDTO mapEntityToOrderDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productNameSnapshot(item.getProductNameSnapshot())
                        .quantity(item.getQuantity())
                        .priceAtPurchase(item.getPriceAtPurchase())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .itemsTotalAmount(order.getItemsTotalAmount())
                .shippingAmount(order.getShippingAmount())
                .discountAmount(order.getDiscountAmount())
                .grandTotalAmount(order.getGrandTotalAmount())
                .shippingAddressId(order.getShippingAddressId())
                .paymentTransactionId(order.getPaymentTransactionId())
                .shippingTrackingId(order.getShippingTrackingId())
                .items(itemDTOs)
                .build();
    }