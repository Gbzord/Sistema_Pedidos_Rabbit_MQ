package br.com.ecommerce.orderservice.model;

import java.time.LocalDateTime;

/**
 * Modelo que representa um Pedido armazenado em memória.
 * Como não estamos usando banco de dados, os pedidos são
 * mantidos em uma estrutura de dados em memória no OrderRepository.
 */
public class Order {

    private Long id;
    private String customerName;
    private String product;
    private Double amount;
    private LocalDateTime createdAt;

    // Construtor padrão
    public Order() {
    }

    public Order(Long id, String customerName, String product, Double amount) {
        this.id = id;
        this.customerName = customerName;
        this.product = product;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", product='" + product + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}
