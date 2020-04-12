package com.lagou.edu.annotation;

import java.lang.annotation.*;

/**
 *@author Admin
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
