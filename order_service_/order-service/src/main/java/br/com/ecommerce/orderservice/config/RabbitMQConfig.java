package br.com.ecommerce.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ para o Order Service.
 * 
 * Esta classe configura:
 * - Exchange do tipo Direct: roteia mensagens baseado na routing key exata
 * - Fila para receber as mensagens de pedidos criados
 * - Binding: liga a fila à exchange usando a routing key
 * - MessageConverter: converte objetos Java para JSON automaticamente
 */
@Configuration
public class RabbitMQConfig {

    // Valores lidos do application.properties
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    /**
     * Cria a Exchange do tipo Direct.
     * Exchange Direct: roteia mensagens para filas onde a routing key
     * da mensagem corresponde exatamente à routing key do binding.
     */
    @Bean
    public DirectExchange ordersExchange() {
        // Parâmetros: nome, durable (sobrevive restart), autoDelete
        return new DirectExchange(exchangeName, true, false);
    }

    /**
     * Cria a Fila que receberá as mensagens de pedidos.
     * Durable = true: a fila sobrevive ao restart do RabbitMQ
     */
    @Bean
    public Queue ordersQueue() {
        return new Queue(queueName, true);
    }

    /**
     * Cria o Binding que conecta a Fila à Exchange.
     * A routing key determina quais mensagens vão para esta fila.
     */
    @Bean
    public Binding ordersBinding(Queue ordersQueue, DirectExchange ordersExchange) {
        return BindingBuilder
                .bind(ordersQueue)
                .to(ordersExchange)
                .with(routingKey);
    }

    /**
     * Configura o conversor de mensagens para JSON.
     * Isso permite enviar objetos Java que serão automaticamente
     * serializados para JSON antes de enviar ao RabbitMQ.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura o RabbitTemplate com o conversor JSON.
     * RabbitTemplate é o componente usado para enviar mensagens.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
