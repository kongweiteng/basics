package com.enn.energy.system.common.valida;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyConstriantValidator implements ConstraintValidator<NoBoolean,Object> {  //两个泛型分别为自定义注解和要校验的类型
    /*@Autowired  //可自由注入Spring工厂的bean
    private HelloService helloService;*/

    /**
     * 初始化
     * @param noBoolean
     */
    @Override
    public void initialize(NoBoolean noBoolean) {
    }

    /**
     * 校验逻辑
     * @param o  //校验的参数
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if(o.equals("false")){
            return false;
        }else if(o.equals("true")){
            return false;
        }
        return true;
    }
}
