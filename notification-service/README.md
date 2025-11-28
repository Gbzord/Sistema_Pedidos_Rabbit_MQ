# 🛒 Mini Arquitetura de Microserviços com RabbitMQ

Sistema de e-commerce com arquitetura de microserviços utilizando comunicação assíncrona via RabbitMQ.

## 📋 Índice

- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar](#-como-executar)
- [Testando o Fluxo](#-testando-o-fluxo)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Explicação da Integração RabbitMQ](#-explicação-da-integração-rabbitmq)

---

## 🏗️ Arquitetura

```
┌─────────────────┐         ┌──────────────┐         ┌──────────────────────┐
│                 │         │              │         │                      │
│  order-service  │ ──────► │   RabbitMQ   │ ──────► │ notification-service │
│   (Producer)    │ publica │              │ consome │     (Consumer)       │
│                 │         │              │         │                      │
└─────────────────┘         └──────────────┘         └──────────────────────┘
     :8080                       :5672                      :8081
                                :15672
                            (Management UI)
```

### Componentes

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| **order-service** | 8080 | API REST para criação de pedidos (Producer) |
| **notification-service** | 8081 | Consumidor de mensagens para notificações (Consumer) |
| **RabbitMQ** | 5672 | Porta AMQP para mensagens |
| **RabbitMQ Management** | 15672 | Painel web de gerenciamento |

### Configuração RabbitMQ

| Componente | Nome | Tipo |
|------------|------|------|
| Exchange | `orders.exchange` | Direct |
| Queue | `orders.created.queue` | Durable |
| Routing Key | `orders.created` | - |

---

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring AMQP** (RabbitMQ)
- **RabbitMQ 3.x** (Docker)
- **Maven**

---

## 📦 Pré-requisitos

- **Java 21** ou superior
- **Maven 3.8+**
- **Docker** e **Docker Compose**

### Verificar instalação:

```bash
java -version    # Deve mostrar Java 21+
mvn -version     # Deve mostrar Maven 3.8+
docker --version # Deve mostrar Docker instalado
```

---

## 🚀 Como Executar

### Passo 1: Subir o RabbitMQ com Docker

Na pasta raiz do projeto, execute:

```bash
docker-compose up -d
```

Aguarde alguns segundos e verifique se está rodando:

```bash
docker ps
```

Você deve ver o container `rabbitmq-ecommerce` em execução.

**Acessar o painel de gerenciamento:**
- URL: http://localhost:15672
- Usuário: `guest`
- Senha: `guest`

### Passo 2: Compilar e executar o order-service

Em um terminal, navegue até a pasta do order-service:

```bash
cd order-service
mvn clean install
mvn spring-boot:run
```

Você verá no console:
```
===========================================
   ORDER SERVICE INICIADO NA PORTA 8080    
   POST /orders para criar um pedido       
===========================================
```

### Passo 3: Compilar e executar o notification-service

Em **outro terminal**, navegue até a pasta do notification-service:

```bash
cd notification-service
mvn clean install
mvn spring-boot:run
```

Você verá no console:
```
====================================================
   NOTIFICATION SERVICE INICIADO NA PORTA 8081      
   Aguardando mensagens do RabbitMQ...              
====================================================
```

---

## 🧪 Testando o Fluxo

### 1. Criar um pedido (POST)

Usando **cURL**:

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "João da Silva",
    "product": "Notebook",
    "amount": 3500.0
  }'
```

Usando **Postman/Insomnia**:

1. Método: `POST`
2. URL: `http://localhost:8080/orders`
3. Body (JSON):
```json
{
    "customerName": "João da Silva",
    "product": "Notebook",
    "amount": 3500.0
}
```

**Resposta esperada (Status 201 Created):**

```json
{
    "orderId": 1,
    "customerName": "João da Silva",
    "product": "Notebook",
    "amount": 3500.0,
    "createdAt": "2024-01-15T10:30:00"
}
```

### 2. Verificar os logs

**No terminal do order-service:**

```
[CONTROLLER] Recebida requisição POST /orders
[PRODUCER] Enviando mensagem para RabbitMQ...
[PRODUCER] Exchange: orders.exchange
[PRODUCER] Routing Key: orders.created
[PRODUCER] Mensagem enviada com sucesso!
```

**No terminal do notification-service:**

```
##################################################
[CONSUMER] Mensagem recebida do RabbitMQ!
[NOTIFICATION] Pedido 1 do cliente João da Silva (R$ 3.500,00) recebido – notificação enviada.
📧 [EMAIL] Enviando e-mail para o cliente...
📱 [SMS] Enviando SMS para o cliente...
🔔 [PUSH] Enviando push notification...
##################################################
```

### 3. Consultar pedidos

**Listar todos os pedidos:**

```bash
curl http://localhost:8080/orders
```

**Buscar pedido por ID:**

```bash
curl http://localhost:8080/orders/1
```

### 4. Verificar no RabbitMQ Management

Acesse http://localhost:15672 e vá em:
- **Queues** → `orders.created.queue` - Ver mensagens processadas
- **Exchanges** → `orders.exchange` - Ver a exchange criada
- **Connections** - Ver conexões dos serviços

---

## 📁 Estrutura do Projeto

```
microservices-rabbitmq/
├── docker-compose.yml              # Configuração do RabbitMQ
├── README.md                       # Este arquivo
│
├── order-service/                  # PRODUCER
│   ├── pom.xml
│   └── src/main/java/br/com/ecommerce/orderservice/
│       ├── OrderServiceApplication.java
│       ├── config/
│       │   └── RabbitMQConfig.java       # Config Exchange, Queue, Binding
│       ├── controller/
│       │   └── OrderController.java      # API REST
│       ├── dto/
│       │   ├── OrderRequest.java         # DTO de entrada
│       │   └── OrderResponse.java        # DTO de saída
│       ├── event/
│       │   └── OrderCreatedEvent.java    # Evento para RabbitMQ
│       ├── model/
│       │   └── Order.java                # Modelo de domínio
│       ├── producer/
│       │   └── OrderMessageProducer.java # Envia mensagens
│       ├── repository/
│       │   └── OrderRepository.java      # Armazenamento em memória
│       └── service/
│           └── OrderService.java         # Lógica de negócio
│
└── notification-service/           # CONSUMER
    ├── pom.xml
    └── src/main/java/br/com/ecommerce/notificationservice/
        ├── NotificationServiceApplication.java
        ├── config/
        │   └── RabbitMQConfig.java       # Config Queue e Listener
        ├── consumer/
        │   └── OrderMessageConsumer.java # @RabbitListener
        ├── event/
        │   └── OrderCreatedEvent.java    # Mesmo evento do producer
        └── service/
            └── NotificationService.java  # Processa notificações
```

---

## 🔗 Explicação da Integração RabbitMQ

### Conceitos Principais

#### 1. Exchange (Roteador)
A **Exchange** recebe mensagens do producer e roteia para as filas corretas.
- Tipo **Direct**: Roteia baseado na routing key exata.

```java
@Bean
public DirectExchange ordersExchange() {
    return new DirectExchange("orders.exchange", true, false);
}
```

#### 2. Queue (Fila)
A **Queue** armazena as mensagens até serem consumidas.

```java
@Bean
public Queue ordersQueue() {
    return new Queue("orders.created.queue", true);
}
```

#### 3. Binding (Ligação)
O **Binding** conecta a Queue à Exchange usando uma Routing Key.

```java
@Bean
public Binding ordersBinding(Queue ordersQueue, DirectExchange ordersExchange) {
    return BindingBuilder
            .bind(ordersQueue)
            .to(ordersExchange)
            .with("orders.created");
}
```

### Fluxo da Mensagem

```
1. Cliente faz POST /orders
        ↓
2. OrderController recebe e chama OrderService
        ↓
3. OrderService salva pedido e cria OrderCreatedEvent
        ↓
4. OrderMessageProducer envia evento para RabbitMQ
        ↓
5. RabbitMQ recebe na Exchange "orders.exchange"
        ↓
6. Exchange roteia para "orders.created.queue" (routing key match)
        ↓
7. OrderMessageConsumer (notification-service) recebe via @RabbitListener
        ↓
8. NotificationService processa e loga a notificação
```

### Producer (order-service)

```java
@Component
public class OrderMessageProducer {
    
    private final RabbitTemplate rabbitTemplate;
    
    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        // Envia para a exchange com a routing key
        rabbitTemplate.convertAndSend("orders.exchange", "orders.created", event);
    }
}
```

### Consumer (notification-service)

```java
@Component
public class OrderMessageConsumer {
    
    @RabbitListener(queues = "orders.created.queue")
    public void consume(OrderCreatedEvent event) {
        // Processa a mensagem recebida
        notificationService.processOrderNotification(event);
    }
}
```

### Serialização JSON

Ambos os serviços usam **Jackson2JsonMessageConverter** para:
- **Producer**: Serializar objetos Java → JSON
- **Consumer**: Deserializar JSON → objetos Java

```java
@Bean
public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
}
```

---

## 🔧 Comandos Úteis

```bash
# Parar o RabbitMQ
docker-compose down

# Ver logs do RabbitMQ
docker logs rabbitmq-ecommerce

# Limpar volumes (remove dados persistidos)
docker-compose down -v

# Rebuild dos serviços
mvn clean install -DskipTests
```

---

## 📝 Autor

Projeto desenvolvido para a disciplina de Arquitetura de Microserviços - SENAC Rio.

---
