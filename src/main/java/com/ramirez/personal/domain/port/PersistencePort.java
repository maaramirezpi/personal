package com.ramirez.personal.domain.port;

import com.ramirez.personal.domain.entity.Customer;
import io.vavr.control.Option;

public interface PersistencePort {

  Customer saveCustomer(Customer customer);

  Option<Customer> getCustomer(Long customerId);
}
