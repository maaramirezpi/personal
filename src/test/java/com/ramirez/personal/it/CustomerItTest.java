package com.ramirez.personal.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.port.MessagePort;
import com.ramirez.personal.domain.port.PersistencePort;
import com.ramirez.personal.generated.model.CustomerDto;
import io.vavr.control.Option;
import java.math.BigDecimal;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
class CustomerItTest extends AbstractIntegrationTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper mapper;

  // TODO: when redis is implemented how to chose one or the other
  @Autowired private PersistencePort persistencePort;

  @Autowired private Consumer<String, String> testConsumer;
  private String topicName = "personal-topic";

  @Test
  void createCustomer() throws Exception {
    CustomerDto customerDto = new CustomerDto();
    customerDto.setId(BigDecimal.valueOf(0));
    customerDto.setFirstName("Manuel");
    customerDto.setLastName("Ramirez");

    MvcResult result =
        mvc.perform(
                post("/customer")
                    .content(mapper.writeValueAsString(customerDto))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    CustomerDto resultDto =
        mapper.readValue(result.getResponse().getContentAsString(), CustomerDto.class);

    assertEquals(customerDto.getId(), resultDto.getId());
    assertEquals(customerDto.getFirstName(), resultDto.getFirstName());
    assertEquals(customerDto.getLastName(), resultDto.getLastName());

    // TODO: Eventual consistency, add retry mechanism in case is not created yet
    Option<Customer> customer = persistencePort.getCustomer(customerDto.getId().longValue());

    Customer theCustomer = customer.getOrElse(Assertions::fail);
    assertEquals(customerDto.getId().longValue(), theCustomer.customerId());
    assertEquals(customerDto.getFirstName(), theCustomer.firstName());
    assertEquals(customerDto.getLastName(), theCustomer.lastName());

    ConsumerRecord<String, String> resultRecord = KafkaTestUtils.getSingleRecord(testConsumer, topicName, 10000);

    //Customer readCustomer = mapper.readValue(resultRecord.value(), Customer.class);
    // TODO: ugly but works, check serialization
    assertEquals(customer.get().toString(), resultRecord.value());
  }
}
