package com.ramirez.personal.domain;

import com.ramirez.personal.domain.entity.Customer;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CustomerService {

  private final HashMap<Long, Customer> customers = new HashMap<>();

  public Customer createCustomer(Customer customer) {
    customers.put(customer.customerId(), customer);
    return customer;
  }

  public Option<Customer> getCustomer(Long customerId) {
    return Option.of(customers.get(customerId));
  }
}
