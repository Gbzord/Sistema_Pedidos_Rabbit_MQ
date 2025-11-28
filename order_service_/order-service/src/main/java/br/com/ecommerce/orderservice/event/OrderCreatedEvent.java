package br.com.ecommerce.orderservice.event;

import java.io.Serializable;

/**
 * Evento que representa um pedido criado.
 * Esta classe é serializada para JSON e enviada ao RabbitMQ.
 * O notification-service irá consumir este evento.
 */
public class OrderCreatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private String customerName;
    private String product;
    private Double amount;

    // Construtor padrão necessário para deserialização
    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, String customerName, String product, Double amount) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.product = product;
        this.amount = amount;
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

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", product='" + product + '\'' +
                ", amount=" + amount +
                '}';
    }
}
