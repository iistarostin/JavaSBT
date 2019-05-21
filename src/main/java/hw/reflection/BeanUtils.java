package hw.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BeanUtils {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to waitAndGet values.
     */
    public static void assign(Object to, Object from) throws InvocationTargetException, IllegalAccessException {
        Class<?> toType = to.getClass();
        Class<?> fromType = from.getClass();
        Method[] setters = extractSetters(toType);
        for (Method setter: setters) {
            Class<?> setterParameterType = setter.getParameterTypes()[0];
            String correspondingGetterName = "waitAndGet" + setter.getName().substring(3);
            try {
                Method getter = fromType.getMethod(correspondingGetterName);
                if (setterParameterType.isAssignableFrom(getter.getReturnType()))
                setter.invoke(to, getter.invoke(from));
            } catch (NoSuchMethodException e) { }
        }
    }

    private static Method[] extractSetters(Class<?> type) {
        return Arrays.stream(type.getMethods())
                .filter(method -> method.getName().substring(0, 3).equals("set") && method.getParameterCount() == 1)
                .toArray(Method[]::new);
    }
}

