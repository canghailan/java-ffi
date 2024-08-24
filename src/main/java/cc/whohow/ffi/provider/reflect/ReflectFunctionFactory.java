package cc.whohow.ffi.provider.reflect;

import java.lang.foreign.MemoryLayout;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ReflectFunctionFactory extends ReflectMemoryLayoutFactory {
    static boolean isVoid(Type type) {
        return type == void.class || type == Void.class;
    }

    MethodHandle createDowncallHandle(Method method);

    default Optional<MemoryLayout> createReturnLayout(Method method) {
        if (isVoid(method.getReturnType())) {
            return Optional.empty();
        } else {
            return Optional.of(createMemoryLayout(method.getGenericReturnType()));
        }
    }

    default List<MemoryLayout> createArgumentLayouts(Method method) {
        if (method.getParameterCount() == 0) {
            return List.of();
        }
        return Arrays.stream(method.getGenericParameterTypes())
                .map(this::createMemoryLayout)
                .toList();
    }
}
