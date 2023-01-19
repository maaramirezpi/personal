package com.ramirez.personal.domain.service;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.port.PersistencePort;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  @Autowired private PersistencePort persistencePort;

  public Customer createCustomer(Customer customer) {
    return persistencePort.saveCustomer(customer);
  }

  public Option<Customer> getCustomer(Long customerId) {
    return persistencePort.getCustomer(customerId);
  }
}
