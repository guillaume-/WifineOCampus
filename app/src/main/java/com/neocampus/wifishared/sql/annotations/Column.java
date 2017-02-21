package com.neocampus.wifishared.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    boolean Primary() default false;

    SqlType Type();

    boolean Auto() default false;

    boolean Nullable();

    String value() default "";

}
