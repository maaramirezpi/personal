package com.ramirez.personal.domain.service;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.port.PersistencePort;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CustomerServiceTest {

  @Mock private PersistencePort persistencePort;

  @InjectMocks private CustomerService customerService;

  @BeforeEach
  void setUp() {
    openMocks(this);
  }

  @Test
  void createCustomer() {
    Customer customer = new Customer(0l, "Manuel", "Ramirez");
    when(persistencePort.saveCustomer(any())).thenReturn(customer);

    Customer serviceCustomer = customerService.createCustomer(customer);

    assertEquals(customer.customerId(), serviceCustomer.customerId());
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
