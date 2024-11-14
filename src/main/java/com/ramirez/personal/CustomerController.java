package com.ramirez.personal;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.service.CustomerService;
import com.ramirez.personal.generated.api.CustomerApi;
import com.ramirez.personal.generated.model.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<CustomerDto> createCustomer(CustomerDto apiCustomer) {
        Customer customer = new Customer(apiCustomer.getId().longValue(), apiCustomer.getFirstName(),
                apiCustomer.getLastName());

        return customerService.createCustomer(customer)
                .fold(domainError -> ResponseEntity.internalServerError().build(),
                        customer1 -> ResponseEntity.ok(domainToApi(customer1)));
    }

    @Override
    public ResponseEntity<CustomerDto> getCustomer(Long customerId) {
        return customerService.getCustomer(customerId)
                .map(this::domainToApi)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());
    }

    private CustomerDto domainToApi(Customer customer) {
        CustomerDto cfd = new CustomerDto();
        cfd.setId(BigDecimal.valueOf(customer.customerId()));
        cfd.setFirstName(customer.firstName());
        cfd.setLastName(customer.lastName());
        return cfd;
    }

}
