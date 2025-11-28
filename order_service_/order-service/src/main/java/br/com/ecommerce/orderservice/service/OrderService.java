package br.com.ecommerce.orderservice.service;

import br.com.ecommerce.orderservice.dto.OrderRequest;
import br.com.ecommerce.orderservice.dto.OrderResponse;
import br.com.ecommerce.orderservice.event.OrderCreatedEvent;
import br.com.ecommerce.orderservice.model.Order;
import br.com.ecommerce.orderservice.producer.OrderMessageProducer;
import br.com.ecommerce.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço que contém a lógica de negócio para pedidos.
 * 
 * Responsabilidades:
 * - Criar pedidos e salvar no repositório em memória
 * - Publicar eventos de pedidos criados no RabbitMQ
 * - Consultar pedidos existentes
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderMessageProducer messageProducer;

    public OrderService(OrderRepository orderRepository, OrderMessageProducer messageProducer) {
        this.orderRepository = orderRepository;
        this.messageProducer = messageProducer;
    }

    /**
     * Cria um novo pedido, salva em memória e publica evento no RabbitMQ.
     * 
     * @param request Dados do pedido recebidos na requisição
     * @return Resposta com os dados do pedido criado
     */
    public OrderResponse createOrder(OrderRequest request) {
        logger.info("Criando novo pedido para cliente: {}", request.getCustomerName());

        // 1. Cria o modelo Order a partir do request
        Order order = new Order(
                null, // ID será gerado pelo repository
                request.getCustomerName(),
                request.getProduct(),
                request.getAmount()
        );

        // 2. Salva o pedido no repositório em memória
        Order savedOrder = orderRepository.save(order);
        logger.info("Pedido salvo com ID: {}", savedOrder.getId());

        // 3. Cria o evento para publicar no RabbitMQ
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getProduct(),
                savedOrder.getAmount()
        );

        // 4. Publica o evento no RabbitMQ (comunicação assíncrona)
        messageProducer.sendOrderCreatedEvent(event);

        // 5. Retorna a resposta com os dados do pedido criado
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getProduct(),
                savedOrder.getAmount(),
                savedOrder.getCreatedAt()
        );
    }

    /**
     * Busca um pedido por ID.
     * 
     * @param id ID do pedido
     * @return Optional com o pedido se encontrado
     */
    public Optional<OrderResponse> findById(Long id) {
        return orderRepository.findById(id)
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getCustomerName(),
                        order.getProduct(),
                        order.getAmount(),
                        order.getCreatedAt()
                ));
    }

    /**
     * Lista todos os pedidos.
     * 
     * @return Lista com todos os pedidos
     */
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getCustomerName(),
                        order.getProduct(),
                        order.getAmount(),
                        order.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Retorna a quantidade de pedidos.
     * 
     * @return Número total de pedidos
     */
    public long count() {
        return orderRepository.count();
    }
}
