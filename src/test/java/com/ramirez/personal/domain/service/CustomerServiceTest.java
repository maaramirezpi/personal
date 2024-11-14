package com.ramirez.personal.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.error.DomainError;
import com.ramirez.personal.domain.port.MessagePort;
import com.ramirez.personal.domain.port.PersistencePort;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class CustomerServiceTest {

	@Mock
	private PersistencePort persistencePort;

	@Mock
	private MessagePort messagePort;

	@InjectMocks
	private CustomerService customerService;

	@BeforeEach
	void setUp() {
		openMocks(this);
	}

	@Test
	void createCustomer() {
		Customer customer = new Customer(0l, "Manuel", "Ramirez");
		when(persistencePort.saveCustomer(any())).thenReturn(Either.right(customer));

		Either<DomainError, Customer> serviceCustomer = customerService.createCustomer(customer);

		assertTrue(serviceCustomer.isRight());

		assertEquals(customer.customerId(), serviceCustomer.getOrNull().customerId());
	}

	@Test
	void getCustomer() {
		Customer customer = new Customer(0l, "Manuel", "Ramirez");
		when(persistencePort.getCustomer(any())).thenReturn(Option.of(customer));

		Option<Customer> serviceCustomer = customerService.getCustomer(0l);

		assertTrue(serviceCustomer.isDefined());
		assertThat(serviceCustomer).extracting(Customer::customerId).contains(customer.customerId());
		assertThat(serviceCustomer).extracting(Customer::firstName).contains(customer.firstName());
		assertThat(serviceCustomer).extracting(Customer::lastName).contains(customer.lastName());
	}

	@Test
	void getCustomer_notFound() {
		when(persistencePort.getCustomer(any())).thenReturn(Option.none());

		Option<Customer> serviceCustomer = customerService.getCustomer(0l);

		assertTrue(serviceCustomer.isEmpty());
	}

}
