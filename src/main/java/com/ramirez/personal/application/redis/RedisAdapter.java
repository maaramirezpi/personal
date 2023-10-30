package com.ramirez.personal.application.redis;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.error.DomainError;
import com.ramirez.personal.domain.port.PersistencePort;
import io.vavr.control.Either;
import io.vavr.control.Option;

public class RedisAdapter implements PersistencePort {

  @Override
  public Either<DomainError, Customer> saveCustomer(Customer customer) {
    return null;
  }

  @Override
  public Option<Customer> getCustomer(Long customerId) {
    return null;
  }
}
