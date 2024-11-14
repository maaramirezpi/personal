package com.ramirez.personal.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class DomainError {
  private String code;
  private String message;
}
