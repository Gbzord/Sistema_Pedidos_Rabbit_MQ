package br.com.ecommerce.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para receber os dados do pedido na requisição HTTP.
 * Contém validações para garantir que os dados obrigatórios sejam enviados.
 */
public class OrderRequest {

    @NotBlank(message = "O nome do cliente é obrigatório")
    private String customerName;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String product;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private Double amount;

    // Construtor padrão necessário para deserialização JSON
    public OrderRequest() {
    }

    public OrderRequest(String customerName, String product, Double amount) {
        this.customerName = customerName;
        this.product = product;
        this.amount = amount;
    }

    // Getters e Setters
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
        return "OrderRequest{" +
                "customerName='" + customerName + '\'' +
                ", product='" + product + '\'' +
                ", amount=" + amount +
                '}';
    }
}
