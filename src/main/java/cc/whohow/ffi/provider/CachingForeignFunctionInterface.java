package cc.whohow.ffi.provider;

import cc.whohow.ffi.ForeignFunctionInterface;

import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachingForeignFunctionInterface implements ForeignFunctionInterface {
    protected final Map<String, MethodHandle> downcallHandles = new ConcurrentHashMap<>();
    protected final ForeignFunctionInterface foreignFunctionInterface;

    public CachingForeignFunctionInterface(ForeignFunctionInterface foreignFunctionInterface) {
        this.foreignFunctionInterface = foreignFunctionInterface;
    }

    @Override
    public MethodHandle downcallHandle(String name) {
        return downcallHandles.computeIfAbsent(name, foreignFunctionInterface::downcallHandle);
    }
}
