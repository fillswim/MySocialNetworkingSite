package com.fillolej.mysns.annotations;

import com.fillolej.mysns.validations.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

// Обработчик аннотации @PasswordMatches, определяющей совпадение паролей
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class) // PasswordMatchesValidator - собственный класс
@Documented
public @interface PasswordMatches {

    String message() default "Password do not match";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};


}
