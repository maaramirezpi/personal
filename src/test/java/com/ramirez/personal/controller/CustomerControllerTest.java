package com.ramirez.personal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramirez.personal.CustomerController;
import com.ramirez.personal.domain.entity.Customer;
import com.ramirez.personal.domain.error.DomainError;
import com.ramirez.personal.domain.service.CustomerService;
import com.ramirez.personal.generated.model.CustomerDto;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CustomerService customerService;

	@Autowired
	private ObjectMapper mapper;

	@Test
	void createCustomer() throws Exception {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(BigDecimal.valueOf(0));
		customerDto.setFirstName("Manuel");
		customerDto.setLastName("Ramirez");

		when(customerService.createCustomer(any())).thenReturn(Either.right(
				new Customer(customerDto.getId().longValue(), customerDto.getFirstName(), customerDto.getLastName())));

		MvcResult result = mvc
			.perform(post("/customer").content(mapper.writeValueAsString(customerDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		CustomerDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), CustomerDto.class);

		assertEquals(customerDto.getId(), resultDto.getId());
		assertEquals(customerDto.getFirstName(), resultDto.getFirstName());
		assertEquals(customerDto.getLastName(), resultDto.getLastName());
	}

	@Test
	void createCustomer_domainError() throws Exception {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(BigDecimal.valueOf(0));
		customerDto.setFirstName("Manuel");
		customerDto.setLastName("Ramirez");

		when(customerService.createCustomer(any()))
			.thenReturn(Either.left(new DomainError("E-001", "Exception while trying to save customer")));

		mvc.perform(post("/customer").content(mapper.writeValueAsString(customerDto))
			.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError()).andReturn();
	}

	@Test
	void createCustomer_validationError() throws Exception {
		mvc.perform(post("/customer").content("malformed").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

		/*
		 * CustomerDto customerDto = new CustomerDto();
		 *
		 * mvc.perform( post("/customer") .content(mapper.writeValueAsString(customerDto))
		 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isBadRequest());
		 */
	}

	@Test
	void getCustomer() throws Exception {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(BigDecimal.valueOf(0));
		customerDto.setFirstName("Manuel");
		customerDto.setLastName("Ramirez");

		when(customerService.getCustomer(any())).thenReturn(Option
			.of(new Customer(customerDto.getId().longValue(), customerDto.getFirstName(), customerDto.getLastName())));

		MvcResult result = mvc.perform(get("/customer/0").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		CustomerDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), CustomerDto.class);

		assertEquals(customerDto.getId(), resultDto.getId());
		assertEquals(customerDto.getFirstName(), resultDto.getFirstName());
		assertEquals(customerDto.getLastName(), resultDto.getLastName());
	}

	@Test
	void getCustomer_NotFound() throws Exception {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(BigDecimal.valueOf(0));
		customerDto.setFirstName("Manuel");
		customerDto.setLastName("Ramirez");

		when(customerService.getCustomer(any())).thenReturn(Option.none());

		mvc.perform(get("/customer/0").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn();
	}

}
