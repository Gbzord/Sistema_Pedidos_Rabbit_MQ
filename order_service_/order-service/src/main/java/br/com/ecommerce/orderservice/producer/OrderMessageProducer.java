package br.com.ecommerce.orderservice.producer;

import br.com.ecommerce.orderservice.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Producer responsável por publicar mensagens no RabbitMQ.
 * 
 * Este componente encapsula a lógica de envio de mensagens,
 * abstraindo os detalhes do RabbitMQ do resto da aplicação.
 */
@Component
public class OrderMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderMessageProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public OrderMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publica um evento de pedido criado no RabbitMQ.
     * 
     * O evento é automaticamente convertido para JSON pelo MessageConverter
     * configurado no RabbitMQConfig.
     * 
     * @param event Evento contendo os dados do pedido
     */
    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        logger.info("=================================================");
        logger.info("[PRODUCER] Enviando mensagem para RabbitMQ...");
        logger.info("[PRODUCER] Exchange: {}", exchangeName);
        logger.info("[PRODUCER] Routing Key: {}", routingKey);
        logger.info("[PRODUCER] Evento: {}", event);
        
        // Envia a mensagem para a exchange com a routing key especificada
        // O RabbitMQ irá rotear para a fila correta baseado no binding
        rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
        
        logger.info("[PRODUCER] Mensagem enviada com sucesso!");
        logger.info("=================================================");
    }
}
