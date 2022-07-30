package annotations;

import java.lang.annotation.*;

/**
 * После метода с аннотацией @Test.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
}
