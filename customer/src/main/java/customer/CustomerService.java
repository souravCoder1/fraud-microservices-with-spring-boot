package customer;

import fraud.FraudCheckResponse;
import lombok.AllArgsConstructor;
import fraud.FraudClient;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;

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
    }
}
