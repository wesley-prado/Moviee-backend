package com.codemages.Moviee.cinema.session.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConsistentSessionTimesValidator.class)
@Documented
public @interface ConsistentSessionTimes {
  String message() default "O horário de término deve ser posterior ao de início.";

  Class<?>[] groups() default { };

  Class<? extends jakarta.validation.Payload>[] payload() default { };

  String startTime();

  String endTime();

  @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
  @Retention(RetentionPolicy.RUNTIME)
  @interface List {
    ConsistentSessionTimes[] value();
  }
}
