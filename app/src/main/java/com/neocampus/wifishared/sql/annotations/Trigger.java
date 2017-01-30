package com.neocampus.wifishared.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Hirochi ☠ on 09/01/17.
 */

@Target(ElementType.METHOD )
@Retention(RetentionPolicy.RUNTIME)
public @interface Trigger {

    /**
     * @return Name of table in database
     */
    String name();

}