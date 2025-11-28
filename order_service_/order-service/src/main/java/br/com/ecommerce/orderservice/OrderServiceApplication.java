package br.com.ecommerce.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("===========================================");
        System.out.println("   ORDER SERVICE INICIADO NA PORTA 8080    ");
        System.out.println("   POST /orders para criar um pedido       ");
        System.out.println("===========================================");
    }
}
