package com.ramirez.personal;

import com.ramirez.personal.api.CustomerApi;
import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.model.CustomerDto;
import com.ramirez.personal.model.CustomerFullDataDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class CustomerController implements CustomerApi {

  private final HashMap<Long, Customer> customers = new HashMap<>();
  private Long index = 0L;

  @Override
  public ResponseEntity<CustomerFullDataDto> createCustomer(CustomerDto apiCustomer) {
    Customer customer = new Customer(index, apiCustomer.getFirstName(), apiCustomer.getLastName());

    customers.put(index, customer);
    index++;

    return ResponseEntity.ok(domainToApi(customer));
  }

  @Override
  public ResponseEntity<CustomerFullDataDto> getCustomer(Long customerId) {
    if (customers.containsKey(customerId)) {
      return ResponseEntity.ok(domainToApi(customers.get(customerId)));
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  private CustomerFullDataDto domainToApi(Customer customer) {
    CustomerFullDataDto cfd = new CustomerFullDataDto();
    cfd.setCustomerId(customer.customerId());
    cfd.setFirstName(customer.firstName());
    cfd.setLastName(customer.lastName());
    return cfd;
  }
}
