package com.ramirez.personal.application.kafka;

import com.ramirez.personal.aspect.LogExecutionTime;
import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.port.MessagePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@Slf4j
public class KafkaProducerAdapter implements MessagePort {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value(value = "${spring.kafka.topic}")
  private String topic;

  public KafkaProducerAdapter(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  @LogExecutionTime
  public Customer sendCustomer(Customer customer) {
    // TODO: add the key of the message
    ListenableFuture<SendResult<String, String>> send =
        kafkaTemplate.send(topic, customer.toString());

    send.addCallback(result -> log.info("Message sent"), ex -> log.error("Failed", ex));
    return customer;
  }
}
