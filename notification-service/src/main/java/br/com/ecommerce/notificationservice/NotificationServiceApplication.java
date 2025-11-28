package br.com.ecommerce.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
        System.out.println("====================================================");
        System.out.println("   NOTIFICATION SERVICE INICIADO NA PORTA 9070      ");
        System.out.println("   Aguardando mensagens do RabbitMQ...              ");
        System.out.println("====================================================");
    }
}
