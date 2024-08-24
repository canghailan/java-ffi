package cc.whohow.ffi;

import java.lang.invoke.MethodHandle;

public interface ForeignFunctionInterface {
    MethodHandle downcallHandle(String name);

    default Object downcall(String name, Object... arguments) throws Throwable {
        return downcallHandle(name).invokeWithArguments(arguments);
    }
}
