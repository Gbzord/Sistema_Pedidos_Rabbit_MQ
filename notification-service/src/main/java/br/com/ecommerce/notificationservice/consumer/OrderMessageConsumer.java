package br.com.ecommerce.notificationservice.consumer;

import br.com.ecommerce.notificationservice.event.OrderCreatedEvent;
import br.com.ecommerce.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderMessageConsumer.class);

    private final NotificationService notificationService;

    public OrderMessageConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(
            queues = "${rabbitmq.queue.name}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consume(OrderCreatedEvent event) {
        logger.info("##################################################");
        logger.info("[CONSUMER] Mensagem recebida do RabbitMQ!");
        logger.info("[CONSUMER] Evento: {}", event);
        
        try {

            notificationService.processOrderNotification(event);
            logger.info("[CONSUMER] Mensagem processada com sucesso!");
        } catch (Exception e) {
            logger.error("[CONSUMER] Erro ao processar mensagem: {}", e.getMessage());

            throw e;
        }
        
        logger.info("##################################################");
    }
}
