package com.vpsair.common.validator;

import com.vpsair.common.exception.AirException;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 */
public class ValidatorUtils {
    private static Validator validator;

    public static void require(String str,String fieldName){
        if(StringUtils.isEmpty(str)){
            throw new AirException((fieldName+"不能为空"));
        }
    }

    public static void require(Long str,String fieldName){
        if(str==null){
            throw new AirException((fieldName+"不能为空"));
        }
    }

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws AirException  校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws AirException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for(ConstraintViolation<Object> constraint:  constraintViolations){
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new AirException(msg.toString());
        }
    }
}
