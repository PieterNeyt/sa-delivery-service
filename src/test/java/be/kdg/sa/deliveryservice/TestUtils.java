package be.kdg.sa.deliveryservice;

import java.lang.reflect.Field;

final class TestUtils {
    static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TestUtils() {}
}