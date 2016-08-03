package com.wzqj.ordering.common.mongo.context;


import java.lang.reflect.Method;

public final class ConvertableContext<T, V> {
    private final Class<T> type;
    private final Class<V> valueType;
    private final Method valueMethod;
    private final Method ofMethod;

    private final Object[] zeroParameter = new Object[0];

    ConvertableContext(final Class<T> type,
                       final Class<V> valueType,
                       final Method valueMethod,
                       final Method ofMethod) {
        this.type = type;
        this.valueType = valueType;
        this.valueMethod = valueMethod;
        this.ofMethod = ofMethod;
    }

    public Class<T> getType() {
        return type;
    }

    public Class<V> getValueType() {
        return valueType;
    }

    public V value(final T source) throws Exception {
        return valueType.cast(valueMethod.invoke(source, zeroParameter));
    }

    public T of(final V value) throws Exception {
        return type.cast(ofMethod.invoke(null, value));
    }

    /**
     * ConvertableContext.
     */
    @SuppressWarnings("unchecked")
    public static <T, V> ConvertableContext<T, V> build(final String className)
            throws RuntimeException {
        Class<T> convertableClazz;
        try {
            convertableClazz = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("Class not found", cnfe);
        }
        return build(convertableClazz);
    }

    /**
     * ConvertableContext.
     */
    public static <T, V> ConvertableContext<T, V> build(final Class<T> type)
            throws RuntimeException {

        if (!type.isAnnotationPresent(com.wzqj.ordering.common.mongo.annotation.Convertable.class)) {
            throw new RuntimeException(
                    "Class should be annotated by Convertable.");
        }

        com.wzqj.ordering.common.mongo.annotation.Convertable convertable = type
                .getAnnotation(com.wzqj.ordering.common.mongo.annotation.Convertable.class);
        Method valueMethod;
        try {
            valueMethod = type.getMethod(convertable.valueMethod(),
                    new Class[0]);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to obtain method:"
                    + convertable.valueMethod()
                    + "(which should have non-argument).",
                    exception);
        }

        @SuppressWarnings("unchecked")
        Class<V> valueType = (Class<V>) valueMethod.getReturnType();

        Method ofMethod;
        try {
            ofMethod = type.getMethod(convertable.ofMethod(),
                    new Class[]{valueType});
        } catch (Exception exception) {
            throw new RuntimeException("Failed to obtain method:"
                    + convertable.ofMethod() + "(which should have a(n) "
                    + valueType.getName() + " typed argument)", exception);
        }

        return new ConvertableContext<T, V>(type, valueType, valueMethod,
                ofMethod);
    }
}
