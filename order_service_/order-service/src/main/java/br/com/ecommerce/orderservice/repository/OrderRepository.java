package br.com.ecommerce.orderservice.repository;

import br.com.ecommerce.orderservice.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória para armazenar pedidos.
 * Utiliza ConcurrentHashMap para thread-safety e AtomicLong para gerar IDs únicos.
 * Esta implementação simula um banco de dados para fins didáticos.
 */
@Repository
public class OrderRepository {

    // Map thread-safe para armazenar os pedidos
    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    
    // Gerador de IDs atômico (thread-safe)
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Salva um novo pedido gerando um ID automático.
     * @param order Pedido a ser salvo
     * @return Pedido salvo com ID gerado
     */
    public Order save(Order order) {
        Long id = idGenerator.getAndIncrement();
        order.setId(id);
        orders.put(id, order);
        return order;
    }

    /**
     * Busca um pedido por ID.
     * @param id ID do pedido
     * @return Optional contendo o pedido se encontrado
     */
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    /**
     * Retorna todos os pedidos.
     * @return Lista com todos os pedidos
     */
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    /**
     * Retorna a quantidade de pedidos armazenados.
     * @return Número de pedidos
     */
    public long count() {
        return orders.size();
    }

    /**
     * Remove um pedido por ID.
     * @param id ID do pedido a ser removido
     */
    public void deleteById(Long id) {
        orders.remove(id);
    }

    /**
     * Limpa todos os pedidos (útil para testes).
     */
    public void deleteAll() {
        orders.clear();
    }
}
