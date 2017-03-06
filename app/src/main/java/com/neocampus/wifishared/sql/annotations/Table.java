package com.neocampus.wifishared.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;


@Target(ElementType.TYPE )
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * @return Name of table in database
     */
    String TableName();

    /**
     * @return Order to create and drop table
     */
    int Order();


    /**
     * @return if true then table is created
     */
    boolean enabled() default true;

    class IComparator implements Comparator<Class<?>> {
        @Override
        public int compare(Class<?> aClass1, Class<?> aClass2) {
            Table table1 = aClass1.getAnnotation(Table.class);
            Table table2 = aClass2.getAnnotation(Table.class);
            return table1.Order() - table2.Order();
        }
    }
}