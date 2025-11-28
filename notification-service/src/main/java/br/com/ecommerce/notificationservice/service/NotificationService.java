package br.com.ecommerce.notificationservice.service;

import br.com.ecommerce.notificationservice.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    public void processOrderNotification(OrderCreatedEvent event) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String valorFormatado = CURRENCY_FORMAT.format(event.getAmount());
        

        logger.info("*****************************************************");
        logger.info("[NOTIFICATION] Pedido {} do cliente {} ({}) recebido – notificação enviada.",
                event.getOrderId(),
                event.getCustomerName(),
                valorFormatado);
        logger.info("*****************************************************");
        

        simulateEmailNotification(event, timestamp, valorFormatado);
        simulateSmsNotification(event, valorFormatado);
        simulatePushNotification(event, valorFormatado);
    }


    private void simulateEmailNotification(OrderCreatedEvent event, String timestamp, String valor) {
        logger.info("Enviando e-mail para o cliente...");
        logger.info("   Assunto: Confirmação do Pedido #{}", event.getOrderId());
        logger.info("   Destinatário: {} <cliente@email.com>", event.getCustomerName());
        logger.info("   Conteúdo: Seu pedido de {} no valor de {} foi confirmado!", 
                event.getProduct(), valor);
        logger.info("   Data/Hora: {}", timestamp);
    }


    private void simulateSmsNotification(OrderCreatedEvent event, String valor) {
        logger.info("Enviando SMS para o cliente...");
        logger.info("   Mensagem: Olá {}! Pedido #{} ({}) confirmado. Valor: {}",
                event.getCustomerName(),
                event.getOrderId(),
                event.getProduct(),
                valor);
    }


    private void simulatePushNotification(OrderCreatedEvent event, String valor) {
        logger.info("Enviando push notification...");
        logger.info("   Título: Pedido Confirmado!");
        logger.info("   Corpo: {} - {} por {}", 
                event.getProduct(), 
                valor,
                event.getCustomerName());
    }
}
