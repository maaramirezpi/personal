package com.ramirez.personal.domain.port;

import com.ramirez.personal.domain.entity.Customer;

public interface MessagePort {

	Customer notifyNewCustomer(Customer customer);

}
