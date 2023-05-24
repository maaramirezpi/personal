package com.ramirez.personal.application.postgres;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.error.DomainError;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class DatabaseAdapterTest {

  @Mock private CustomerRepository customerRepository;
  @InjectMocks private DatabaseAdapter databaseAdapter;

  @BeforeEach
  void setUp() {
    openMocks(this);
  }

  @Test
  void saveCustomer() {
    CustomerEntity customerEntity = getCustomerEntity();
    Customer customer = getDomainCustomer();
    //TODO: we can add a captor here

    when(customerRepository.save(any())).thenReturn(customerEntity);

    Either<DomainError, Customer> customerResponse = databaseAdapter.saveCustomer(customer);

    assertTrue(customerResponse.isRight());
    assertEquals(customerEntity.getId(), customerResponse.get().customerId());
    assertEquals(customerEntity.getLastName(), customerResponse.get().lastName());
    assertEquals(customerEntity.getFirstName(), customerResponse.get().firstName());
  }

  @Test
  void getCustomer() {
    CustomerEntity customerEntity = getCustomerEntity();
    when(customerRepository.findById(any())).thenReturn(Optional.of(customerEntity));

    Option<Customer> customer = databaseAdapter.getCustomer(10l);

    assertTrue(customer.isDefined());
    assertEquals(customerEntity.getId(), customer.get().customerId());
    assertEquals(customerEntity.getFirstName(), customer.get().firstName());
    assertEquals(customerEntity.getLastName(), customer.get().lastName());
  }

  @NotNull
  private static Customer getDomainCustomer() {
    return new Customer(10l, "Manuel", "Ramirez");
  }

  private static CustomerEntity getCustomerEntity() {
    return CustomerEntity.builder().id(10l).firstName("Manuel").lastName("Ramirez").build();
  }
}
