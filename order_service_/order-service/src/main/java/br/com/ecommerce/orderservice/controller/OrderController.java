package br.com.ecommerce.orderservice.controller;

import br.com.ecommerce.orderservice.dto.OrderRequest;
import br.com.ecommerce.orderservice.dto.OrderResponse;
import br.com.ecommerce.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de pedidos.
 * 
 * Endpoints disponíveis:
 * - POST /orders: Cria um novo pedido
 * - GET /orders: Lista todos os pedidos
 * - GET /orders/{id}: Busca pedido por ID
 */
@RestController
@RequestMapping("/orders")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Endpoint para criar um novo pedido.
     * 
     * Ao receber a requisição:
     * 1. Valida os dados recebidos
     * 2. Salva o pedido em memória
     * 3. Publica mensagem no RabbitMQ
     * 4. Retorna o pedido criado com status 201
     * 
     * @param request Dados do pedido (customerName, product, amount)
     * @return Pedido criado com ID gerado
     */
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido e publica mensagem no RabbitMQ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        logger.info("=================================================");
        logger.info("[CONTROLLER] Recebida requisição POST /orders");
        logger.info("[CONTROLLER] Request: {}", request);
        
        OrderResponse response = orderService.createOrder(request);
        
        logger.info("[CONTROLLER] Pedido criado com sucesso: {}", response);
        logger.info("=================================================");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para listar todos os pedidos.
     * 
     * @return Lista com todos os pedidos armazenados em memória
     */
    @Operation(summary = "Listar pedidos", description = "Retorna todos os pedidos cadastrados")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        logger.info("[CONTROLLER] Recebida requisição GET /orders");
        List<OrderResponse> orders = orderService.findAll();
        logger.info("[CONTROLLER] Retornando {} pedidos", orders.size());
        return ResponseEntity.ok(orders);
    }

    /**
     * Endpoint para buscar um pedido por ID.
     * 
     * @param id ID do pedido
     * @return Pedido encontrado ou 404 se não existir
     */
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        logger.info("[CONTROLLER] Recebida requisição GET /orders/{}", id);
        
        return orderService.findById(id)
                .map(order -> {
                    logger.info("[CONTROLLER] Pedido encontrado: {}", order);
                    return ResponseEntity.ok(order);
                })
                .orElseGet(() -> {
                    logger.warn("[CONTROLLER] Pedido não encontrado com ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Endpoint para verificar a quantidade de pedidos.
     * Útil para testes e monitoramento.
     * 
     * @return Número total de pedidos
     */
    @Operation(summary = "Contar pedidos", description = "Retorna o total de pedidos cadastrados")
    @GetMapping("/count")
    public ResponseEntity<Long> getOrderCount() {
        long count = orderService.count();
        logger.info("[CONTROLLER] Total de pedidos: {}", count);
        return ResponseEntity.ok(count);
    }
}
