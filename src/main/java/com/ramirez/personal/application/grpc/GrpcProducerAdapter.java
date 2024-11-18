package com.ramirez.personal.application.grpc;

import com.ramirez.personal.aspect.LogExecutionTime;
import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.port.MessagePort;
import customer.protos.CustomerOuterClass;
import customer.protos.CustomerServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Primary
public class GrpcProducerAdapter implements MessagePort {

	@GrpcClient("hello")
	CustomerServiceGrpc.CustomerServiceBlockingStub stub;

	@Override
	@LogExecutionTime
	public Customer notifyNewCustomer(Customer customer) {
		StreamObserver<CustomerOuterClass.NotifyCustomerResponse> streamObserver = new StreamObserver<>() {
			@Override
			public void onNext(CustomerOuterClass.NotifyCustomerResponse notifyCustomerResponse) {
				log.info("Notify Customer Response: {}", notifyCustomerResponse.getCustomer());
				log.info("Notify Customer Response: {}", notifyCustomerResponse.getNotified());
			}

			@Override
			public void onError(Throwable throwable) {
				log.error("Error occurred", throwable);
			}

			@Override
			public void onCompleted() {
				log.info("Notify customer request finished");
			}
		};

		CustomerOuterClass.Customer customerToSend = toCustomer(customer);
		CustomerOuterClass.NotifyCustomerRequest request = CustomerOuterClass.NotifyCustomerRequest.newBuilder()
			.setCustomer(customerToSend)
			.build();

		CustomerOuterClass.NotifyCustomerResponse notifyCustomerResponse = stub.notifyCustomer(request);

		return toCustomer(notifyCustomerResponse.getCustomer());

	}

	private CustomerOuterClass.Customer toCustomer(Customer customer) {
		return CustomerOuterClass.Customer.newBuilder()
			.setId(customer.customerId())
			.setFirstName(customer.firstName())
			.setLastName(customer.lastName())
			.build();
	}

	private Customer toCustomer(CustomerOuterClass.Customer customer) {
		return new Customer(customer.getId(), customer.getFirstName(), customer.getLastName());
	}

}
