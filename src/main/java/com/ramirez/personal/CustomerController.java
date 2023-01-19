package com.ramirez.personal;

import com.ramirez.personal.api.CustomerApi;
import com.ramirez.personal.domain.CustomerService;
import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.model.CustomerDto;
import com.ramirez.personal.model.CustomerFullDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomerApi {

  private Long index = 0L;

  @Autowired private CustomerService customerService;

  @Override
  public ResponseEntity<CustomerFullDataDto> createCustomer(CustomerDto apiCustomer) {
    Customer customer = new Customer(index, apiCustomer.getFirstName(), apiCustomer.getLastName());

    customerService.createCustomer(customer);
    index++;

    return ResponseEntity.ok(domainToApi(customer));
  }

  @Override
  public ResponseEntity<CustomerFullDataDto> getCustomer(Long customerId) {
    return customerService
        .getCustomer(customerId)
        .map(this::domainToApi)
        .map(ResponseEntity::ok)
        .getOrElse(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  private CustomerFullDataDto domainToApi(Customer customer) {
    CustomerFullDataDto cfd = new CustomerFullDataDto();
    cfd.setCustomerId(customer.customerId());
    cfd.setFirstName(customer.firstName());
    cfd.setLastName(customer.lastName());
    return cfd;
  }
}
