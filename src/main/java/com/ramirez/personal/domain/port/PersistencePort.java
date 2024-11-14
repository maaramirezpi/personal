package com.ramirez.personal.domain.port;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.error.DomainError;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface PersistencePort {

    Either<DomainError, Customer> saveCustomer(Customer customer);

    Option<Customer> getCustomer(Long customerId);

}
