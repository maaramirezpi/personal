package com.ramirez.personal.application.kafka;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;

class KafkaProducerAdapterTest {

	@Mock
	private KafkaTemplate template;

	@InjectMocks
	private KafkaProducerAdapter producerAdapter;

	@BeforeEach
	void setUp() {
		openMocks(this);
	}

	@Test
	void sendCustomer() {
		// TODO lets think about this later
		/*
		 * new SendResult<String, String>(new ProducerRecord("topic", "value"), new
		 * RecordMetadata()) new ListenableFutureTask() when(template.send(any(),
		 * any())).thenReturn()
		 */
	}

}
