package com.fillolej.mysns.common.validation;

import com.fillolej.mysns.common.annotations.PasswordMatches;
import com.fillolej.mysns.adapter.resource.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// Обработчик аннотации @PasswordMatches, определяющей совпадение паролей
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {}

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {

        SignupRequest signupRequest = (SignupRequest) object;

        String password = signupRequest.getPassword();
        String confirmPassword = signupRequest.getConfirmPassword();

        return password.equals(confirmPassword);
    }
}
