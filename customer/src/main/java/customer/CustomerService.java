package customer;

import clients.fraud.FraudCheckResponse;
import clients.notification.NotificationClient;
import lombok.AllArgsConstructor;
import clients.fraud.FraudClient;
import clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;
    private FraudClient fraudClient;
    private final NotificationClient notificationClient;

    // RestTemplate restTemplate;
    public void register(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .email(customerRequest.email())
                .build();

        customerRepository.saveAndFlush(customer);
        //customerRepository.save(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        
        /*FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
               // "http://localhost:8081/api/v1/fraud-check/{customerId}",
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );*/



        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("Fraudster");
        }

        // todo: send notification
        // todo: make it async. i.e add to queue
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Amigoscode...",
                                customer.getFirstName())
                )
        );
    }
}
