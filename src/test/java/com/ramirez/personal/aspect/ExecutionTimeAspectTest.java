package com.ramirez.personal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class ExecutionTimeAspectTest {

  @Mock private ProceedingJoinPoint proceedingJoinPoint;

  private ExecutionTimeAspect executionTimeAspect = new ExecutionTimeAspect();

  @BeforeEach
  void setUp() {
    openMocks(this);
  }

  @Test
  void logExecutionTime() throws Throwable {
    executionTimeAspect.logExecutionTime(proceedingJoinPoint);
    verify(proceedingJoinPoint, times(1)).proceed();
    verify(proceedingJoinPoint, never()).proceed(null);
  }
}
