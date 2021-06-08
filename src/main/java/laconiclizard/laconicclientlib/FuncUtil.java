package laconiclizard.laconicclientlib;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FuncUtil {

    /**
     * The descriptor of the given class.
     * Array types are supported.
     *
     * @param clazz the class of interest
     * @return type descriptor of the given class
     */
    public static String classDescriptor(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        if (clazz.isPrimitive()) {
            if (clazz == byte.class) {
                return "B";
            } else if (clazz == char.class) {
                return "C";
            } else if (clazz == double.class) {
                return "D";
            } else if (clazz == float.class) {
                return "F";
            } else if (clazz == int.class) {
                return "I";
            } else if (clazz == long.class) {
                return "J";
            } else if (clazz == short.class) {
                return "S";
            } else if (clazz == boolean.class) {
                return "Z";
            } else {
                throw new AssertionError("Unrecognized primitive: " + clazz);
            }
        }
        if (clazz.isArray()) {
            return "[" + classDescriptor(clazz.getComponentType()) + ";";
        } else {
            return clazz.getCanonicalName().replace('.', '/') + ";";
        }
    }

    /**
     * Descriptor fot the given method.
     *
     * @param method method of interest
     * @return descriptor of method
     */
    public static String methodDescriptor(Method method) {
        StringBuilder sb = new StringBuilder(classDescriptor(method.getDeclaringClass()));
        sb.append(method.getName());
        sb.append("(");
        for (Class<?> pt : method.getParameterTypes()) {
            sb.append(classDescriptor(pt));
        }
        sb.append(")");
        Class<?> rt = method.getReturnType();
        if (rt == Void.TYPE) {
            sb.append("V");
        } else {
            sb.append(classDescriptor(rt));
        }
        return sb.toString();
    }

    // ----- ----- getters & setters ----- -----

    /**
     * Create a getter for the named field on the given class.
     *
     * @param clazz     class of interest
     * @param fieldName name of the static field of interest
     * @return getter for the named field on clazz
     * @throws IllegalArgumentException if the field is not present on the given class, or it is present
     *                                  but not static
     */
    public static Supplier<?> staticGetter(Class<?> clazz, String fieldName) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(fieldName);

        Field f;
        try {
            f = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Unrecognized field: " + fieldName, e);
        }
        if (!Modifier.isStatic(f.getModifiers()))
            throw new IllegalArgumentException("Field not static: " + fieldName);
        f.setAccessible(true);
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle getter;
        try {
            getter = lookup.unreflectGetter(f);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Impossible", e);
        }
        return () -> {
            try {
                return getter.invoke();
            } catch (Throwable t) {
                throw new AssertionError("Impossible", t);
            }
        };
    }

    /**
     * An instance getter for the named field.
     *
     * @param clazz     class of interest
     * @param fieldName name of the field of interest
     * @param <T>       type of interest
     * @return a getter that takes an instance of clazz and returns the value of the named field.
     * getter throws ClassCastException if invoked on an instance of the wrong type
     * @throws IllegalArgumentException if field is not present on class, or is static
     */
    public static <T> Function<T, ?> getter(Class<T> clazz, String fieldName) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(fieldName);

        Field f;
        try {
            f = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Unrecognized field: " + fieldName, e);
        }
        if (Modifier.isStatic(f.getModifiers()))
            throw new IllegalArgumentException("Field is static: " + fieldName);
        f.setAccessible(true);
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle getter;
        try {
            getter = lookup.unreflectGetter(f);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Impossible", e);
        }
        return (T instance) -> {
            Objects.requireNonNull(instance);
            if (!clazz.isInstance(instance)) {
                throw new ClassCastException("Attempt to retrieve field from wrong type: "
                        + fieldName + " on " + clazz + " from " + instance);
            }
            try {
                return getter.invoke(instance);
            } catch (Throwable t) {
                throw new AssertionError("Impossible", t);
            }
        };
    }

    /**
     * A setter for the named static field on clazz.
     *
     * @param clazz     class of interest
     * @param fieldName name of the field of interest
     * @return a setter for the named static field on clazz.  Throws ClassCastException if invoked with the wrong type.
     * @throws IllegalArgumentException if field is not present on class, or is not static
     */
    public static Consumer<?> staticSetter(Class<?> clazz, String fieldName) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(fieldName);

        Field f;
        try {
            f = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Unrecognized field: " + fieldName, e);
        }
        if (!Modifier.isStatic(f.getModifiers()))
            throw new IllegalArgumentException("Field is not static: " + fieldName);
        f.setAccessible(true);
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle setter;
        try {
            setter = lookup.unreflectSetter(f);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Impossible", e);
        }
        return (Object value) -> {
            try {
                setter.invoke(value);
            } catch (ClassCastException e) {  // bubble type exceptions up normally
                throw e;
            } catch (Throwable t) {
                throw new AssertionError("Impossible", t);
            }
        };
    }

    /**
     * An instance setter for the named field.
     *
     * @param clazz     class of interest
     * @param fieldName name of the field of interest
     * @param <T>       class of interest
     * @return a setter that accepts two arguments: the instance upon which to set, and the new value for the named
     * field on that instance.  Throws ClassCastException if invoked with either argument the wrong type.
     * @throws IllegalArgumentException if the named field is not present on clazz, or is static
     */
    public static <T> BiConsumer<T, ?> setter(Class<T> clazz, String fieldName) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(fieldName);

        Field f;
        try {
            f = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Unrecognized field: " + fieldName, e);
        }
        if (Modifier.isStatic(f.getModifiers()))
            throw new IllegalArgumentException("Field is static: " + fieldName);
        f.setAccessible(true);
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle setter;
        try {
            setter = lookup.unreflectSetter(f);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Impossible", e);
        }
        return (T instance, Object value) -> {
            Objects.requireNonNull(instance);
            if (!clazz.isInstance(instance)) {
                throw new ClassCastException("Attempt to set field on wrong type: "
                        + fieldName + " on " + clazz + " from " + instance);
            }
            try {
                setter.invoke(instance, value);
            } catch (ClassCastException e) {  // bubble type exceptions up normally
                throw e;
            } catch (Throwable t) {
                throw new AssertionError("Impossible", t);
            }
        };
    }

}
