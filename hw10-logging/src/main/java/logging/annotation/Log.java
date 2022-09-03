package logging.annotation;

import java.lang.annotation.*;

/**
 * Метод помеченный этой аннотацией логирует его параметры.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}
