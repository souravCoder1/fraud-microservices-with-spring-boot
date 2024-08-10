package amqp;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class sets up RabbitMQ with Spring AMQP.
 * It defines the necessary beans for sending and receiving messages.
 */
@Configuration
@AllArgsConstructor
public class RabbitMQConfig {

    // The ConnectionFactory is injected, which will be used to create connections to the RabbitMQ broker.
    private final ConnectionFactory connectionFactory;

    /**
     * Defines the AmqpTemplate bean, which is used for sending messages to RabbitMQ.
     * @return AmqpTemplate configured with the connection factory and JSON message converter.
     */
    @Bean
    public AmqpTemplate amqpTemplate() {
        // Create a RabbitTemplate instance using the provided connection factory.
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // Set the message converter to JSON using Jackson.
        rabbitTemplate.setMessageConverter(jacksonConverter());
        return rabbitTemplate;
    }

    /**
     * Configures the SimpleRabbitListenerContainerFactory bean, which is used to create
     * listener containers for processing messages from RabbitMQ.
     * @return SimpleRabbitListenerContainerFactory configured with the connection factory and JSON message converter.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        // Create a factory for listener containers using the provided connection factory.
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // Set the connection factory for the listener containers.
        factory.setConnectionFactory(connectionFactory);
        // Set the message converter to JSON using Jackson.
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }

    /**
     * Defines a Jackson2JsonMessageConverter bean, which is used to convert messages
     * to and from JSON format.
     * @return Jackson2JsonMessageConverter for JSON message conversion.
     */
    @Bean
    public MessageConverter jacksonConverter() {
        // Create a new Jackson2JsonMessageConverter instance.
        return new Jackson2JsonMessageConverter();
    }
}
