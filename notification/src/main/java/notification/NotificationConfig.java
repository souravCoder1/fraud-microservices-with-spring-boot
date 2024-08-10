package notification;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up RabbitMQ components related to notifications.
 * This class defines the necessary beans for queues, exchanges, and bindings.
 */
@Configuration
public class NotificationConfig {

    // Injects the name of the internal exchange from application properties.
    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    // Injects the name of the notification queue from application properties.
    @Value("${rabbitmq.queues.notification}")
    private String notificationQueue;

    // Injects the routing key for internal notifications from application properties.
    @Value("${rabbitmq.routing-keys.internal-notification}")
    private String internalNotificationRoutingKey;

    /**
     * Defines a TopicExchange bean for the internal exchange.
     * @return A TopicExchange configured with the internal exchange name.
     */
    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    /**
     * Defines a Queue bean for the notification queue.
     * @return A Queue configured with the notification queue name.
     */
    @Bean
    public Queue notificationQueue() {
        return new Queue(this.notificationQueue);
    }

    /**
     * Defines a Binding bean that binds the notification queue to the internal exchange
     * using the specified routing key.
     * @return A Binding between the notification queue and the internal exchange.
     */
    @Bean
    public Binding internalToNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())                  // Bind the queue
                .to(internalTopicExchange())                // To the exchange
                .with(this.internalNotificationRoutingKey); // Using the routing key
    }

    /**
     * Getter for the internal exchange name.
     * @return The name of the internal exchange.
     */
    public String getInternalExchange() {
        return internalExchange;
    }

    /**
     * Getter for the notification queue name.
     * @return The name of the notification queue.
     */
    public String getNotificationQueue() {
        return notificationQueue;
    }

    /**
     * Getter for the internal notification routing key.
     * @return The internal notification routing key.
     */
    public String getInternalNotificationRoutingKey() {
        return internalNotificationRoutingKey;
    }
}
