package com.ramirez.personal.it;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Collections;

@Configuration
public class TestBeansConfigurer {

	@Autowired
	KafkaProperties properties;

	private static final String TOPIC_NAME = "personal-topic";

	@Bean
	Consumer<String, String> testConsumer() {
		final Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<>(
				properties.buildConsumerProperties(), StringDeserializer::new, StringDeserializer::new)
			.createConsumer();

		consumer.subscribe(Collections.singletonList(TOPIC_NAME));
		return consumer;
	}

}