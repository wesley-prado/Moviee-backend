package com.codemages.Moviee.cinema.session.validation;

import jakarta.validation.ConstraintValidator;
import org.springframework.beans.BeanWrapperImpl;

import java.time.ZonedDateTime;
import java.util.Objects;

public class ConsistentSessionTimesValidator
  implements ConstraintValidator<ConsistentSessionTimes, Object> {
  private String startTimeFieldName;
  private String endTimeFieldName;
  private String message;

  @Override
  public void initialize(ConsistentSessionTimes constraintAnnotation) {
    this.startTimeFieldName = constraintAnnotation.startTime();
    this.endTimeFieldName = constraintAnnotation.endTime();
    this.message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(Object value, jakarta.validation.ConstraintValidatorContext context) {
    BeanWrapperImpl beanWrapper = new BeanWrapperImpl( value );
    ZonedDateTime startTime = (ZonedDateTime) beanWrapper.getPropertyValue( "startTime" );
    ZonedDateTime endTime = (ZonedDateTime) beanWrapper.getPropertyValue( "endTime" );

    if ( Objects.isNull( startTime ) || Objects.isNull( endTime ) ) {
      return true;
    }

    if ( endTime.isAfter( startTime ) ) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate( message )
      .addPropertyNode( endTimeFieldName )
      .addConstraintViolation();

    return false;
  }
}
