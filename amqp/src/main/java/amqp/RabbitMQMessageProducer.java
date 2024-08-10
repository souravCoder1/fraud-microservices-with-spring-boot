package amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

/**
 * This component is responsible for publishing messages to RabbitMQ.
 */
@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQMessageProducer {

    // The AmqpTemplate is injected, which will be used to send messages to RabbitMQ.
    private final AmqpTemplate amqpTemplate;

    /**
     * Publishes a message to the specified exchange with the given routing key.
     * @param payload The message to be sent.
     * @param exchange The name of the exchange to send the message to.
     * @param routingKey The routing key to use for directing the message.
     */
    public void publish(Object payload, String exchange, String routingKey) {
        // Log the details before sending the message.
        log.info("Publishing to {} using routingKey {}. Payload: {}", exchange, routingKey, payload);

        // Send the message to the specified exchange with the routing key.
        amqpTemplate.convertAndSend(exchange, routingKey, payload);

        // Log the details after the message has been sent.
        log.info("Published to {} using routingKey {}. Payload: {}", exchange, routingKey, payload);
    }
}
