package com.ramirez.personal.application.postgres;

import com.ramirez.personal.aspect.LogExecutionTime;
import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.error.DomainError;
import com.ramirez.personal.domain.port.PersistencePort;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

@Component
public class DatabaseAdapter implements PersistencePort {

    private final CustomerRepository customerRepository;

    public DatabaseAdapter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @LogExecutionTime
    public Either<DomainError, Customer> saveCustomer(Customer customer) {
        return Try.of(() -> {
            CustomerEntity customerEntity = CustomerEntity.builder()
                    .id(customer.customerId())
                    .firstName(customer.firstName())
                    .lastName(customer.lastName())
                    .build();

            CustomerEntity savedCustomer = customerRepository.save(customerEntity);
            return savedCustomer.toDomainEntity();
        }).toEither(new DomainError("E-001", "Exception while trying to save customer"));
    }

    @Override
    @LogExecutionTime
    public Option<Customer> getCustomer(Long customerId) {
        return Option.ofOptional(customerRepository.findById(customerId).map(CustomerEntity::toDomainEntity));
    }

}
