package app.bola.flywell.broker;

import app.bola.flywell.data.model.enums.Gender;
import app.bola.flywell.data.model.persons.Customer;
import app.bola.flywell.data.model.persons.UserBioData;
import app.bola.flywell.dtos.Response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlyWellPublisher {

    final PulsarTemplate<Customer> pulsarTemplate;
    final ModelMapper mapper;
    @Value("${spring.pulsar.producer.topic-name}")
    String topicName;

    public CustomerResponse sendMessage(){
        Customer customer = Customer.builder()
                .bioData(
                        UserBioData.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .gender(Gender.FEMALE)
                                .phoneNumber("07036174617")
                                .build()
                )
               .build();

        pulsarTemplate.sendAsync(topicName, customer);
        log.info("Published customer: {}", customer);
        return mapper.map(customer.getBioData(), CustomerResponse.class);
    }
}
