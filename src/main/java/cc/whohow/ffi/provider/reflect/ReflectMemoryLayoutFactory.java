package cc.whohow.ffi.provider.reflect;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.ValueLayout;
import java.lang.reflect.Type;

public interface ReflectMemoryLayoutFactory {
    default MemoryLayout createMemoryLayout(Type type) {
        if (type == int.class) {
            return ValueLayout.JAVA_INT;
        }
        if (type == long.class) {
            return ValueLayout.JAVA_LONG;
        }
        if (type == float.class) {
            return ValueLayout.JAVA_FLOAT;
        }
        if (type == double.class) {
            return ValueLayout.JAVA_DOUBLE;
        }
        if (type == boolean.class) {
            return ValueLayout.JAVA_BOOLEAN;
        }
        if (type == byte.class) {
            return ValueLayout.JAVA_BYTE;
        }
        if (type == short.class) {
            return ValueLayout.JAVA_SHORT;
        }
        if (type == char.class) {
            return ValueLayout.JAVA_CHAR;
        }
        throw new UnsupportedOperationException();
    }
}
