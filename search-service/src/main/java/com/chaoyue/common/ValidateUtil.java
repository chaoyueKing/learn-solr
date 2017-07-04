package com.chaoyue.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by chaoyue on 2017/5/24.
 */
public class ValidateUtil {

    private static final Logger logger = LoggerFactory.getLogger(ValidateUtil.class);

    /**
     * 校验入参
     *
     * @param t
     * @param <T>
     * @return boolean
     */
    public static <T> boolean validateParam(T t) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<T>> set = validator.validate(t);
        if (set != null && !set.isEmpty()) {
            for (ConstraintViolation constraintViolation : set) {
                String errorMessage = constraintViolation.getMessage();
                logger.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
        return true;
    }
}
