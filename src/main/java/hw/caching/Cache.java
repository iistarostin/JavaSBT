package hw.caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    CacheLocation location() default CacheLocation.RAM;
    boolean zip() default false;
    int[] argsIndices() default {};
    long maxListLength() default -1;
    String cacheID() default "";
}
