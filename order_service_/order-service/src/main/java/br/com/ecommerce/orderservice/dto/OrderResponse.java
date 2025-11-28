package br.com.ecommerce.orderservice.dto;

import java.time.LocalDateTime;

/**
 * DTO para retornar os dados do pedido criado na resposta HTTP.
 * Inclui o ID gerado e a data/hora de criação.
 */
public class OrderResponse {

    private Long orderId;
    private String customerName;
    private String product;
    private Double amount;
    private LocalDateTime createdAt;

    // Construtor padrão
    public OrderResponse() {
    }

    public OrderResponse(Long orderId, String customerName, String product, Double amount, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.product = product;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    // Getters e Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
        return "OrderResponse{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", product='" + product + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}
