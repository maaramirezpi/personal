package com.ramirez.personal;

import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.service.CustomerService;
import com.ramirez.personal.generated.api.CustomerApi;
import com.ramirez.personal.generated.model.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CustomerController implements CustomerApi {
  @Autowired private CustomerService customerService;

  @Override
  public ResponseEntity<CustomerDto> createCustomer(CustomerDto apiCustomer) {
    Customer customer =
        new Customer(
            apiCustomer.getId().longValue(), apiCustomer.getFirstName(), apiCustomer.getLastName());

    Customer savedCustomer = customerService.createCustomer(customer);

    return ResponseEntity.ok(domainToApi(savedCustomer));
  }

  @Override
  public ResponseEntity<CustomerDto> getCustomer(Long customerId) {
    return customerService
        .getCustomer(customerId)
        .map(this::domainToApi)
        .map(ResponseEntity::ok)
        .getOrElse(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  private CustomerDto domainToApi(Customer customer) {
    CustomerDto cfd = new CustomerDto();
    cfd.setId(BigDecimal.valueOf(customer.customerId()));
    cfd.setFirstName(customer.firstName());
    cfd.setLastName(customer.lastName());
    return cfd;
  }
}
