package com.ramirez.personal.application.postgres;

import com.ramirez.personal.domain.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CustomerEntity {
  @Id private Long id;
  private String firstName;
  private String lastName;

  public Customer toDomainEntity() {
    return new Customer(this.getId(), this.getFirstName(), this.getLastName());
  }
}
