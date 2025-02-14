package carrotmoa.carrotmoa.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// https://www.youtube.com/watch?v=JCu0NOab5mU
// 어노테이션 선언
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.METHOD})
public @interface RouteDataSource {

    DataSourceType dataSourceType();

    enum DataSourceType {
        MASTER,
        SLAVE
    }
}
