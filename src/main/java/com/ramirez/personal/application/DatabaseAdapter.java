package com.ramirez.personal.application;

import com.ramirez.personal.aspect.LogExecutionTime;
import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.port.PersistencePort;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseAdapter implements PersistencePort {

  @Autowired private CustomerRepository customerRepository;

  @Override
  @LogExecutionTime
  public Customer saveCustomer(Customer customer) {
    CustomerEntity customerEntity =
        CustomerEntity.builder()
            .id(customer.customerId())
            .firstName(customer.firstName())
            .lastName(customer.lastName())
            .build();

    CustomerEntity savedCustomer = customerRepository.save(customerEntity);
    return savedCustomer.toDomainEntity();
  }

  @Override
  @LogExecutionTime
  public Option<Customer> getCustomer(Long customerId) {
    return Option.ofOptional(
        customerRepository.findById(customerId).map(CustomerEntity::toDomainEntity));
  }
}
