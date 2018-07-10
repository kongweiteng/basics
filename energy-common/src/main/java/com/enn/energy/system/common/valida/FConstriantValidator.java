package com.enn.energy.system.common.valida;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FConstriantValidator implements ConstraintValidator<NoFloat,Object> {  //两个泛型分别为自定义注解和要校验的类型
    /*@Autowired  //可自由注入Spring工厂的bean
    private HelloService helloService;*/

    /**
     * 初始化
     * @param noFloat
     */
    @Override
    public void initialize(NoFloat noFloat) {
    }

    /**
     * 校验逻辑
     * @param o  //校验的参数
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String str= (String)o;
        if(!str.contains(".")){//没有小数点，不是float
            return true;
        }else {
            try {
                Float aFloat = Float.valueOf(str);
                //没有转换异常，表示是float
                return false;
            } catch (NumberFormatException e) {
                //有转换异常，不是float
                return true;
            }
        }
    }
}
