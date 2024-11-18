package com.ramirez.personal.domain.service;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.error.DomainError;
import com.ramirez.personal.domain.port.MessagePort;
import com.ramirez.personal.domain.port.PersistencePort;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	private final PersistencePort persistencePort;

	private final MessagePort messagePort;

	public CustomerService(PersistencePort persistencePort, MessagePort messagePort) {
		this.persistencePort = persistencePort;
		this.messagePort = messagePort;
	}

	public Either<DomainError, Customer> createCustomer(Customer customer) {
		messagePort.notifyNewCustomer(customer);
		return persistencePort.saveCustomer(customer);
	}

	public Option<Customer> getCustomer(Long customerId) {
		return persistencePort.getCustomer(customerId);
	}

}
