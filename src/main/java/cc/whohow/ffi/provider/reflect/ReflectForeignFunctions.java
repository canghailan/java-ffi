package cc.whohow.ffi.provider.reflect;

import cc.whohow.ffi.ForeignFunctionInterface;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectForeignFunctions implements ReflectFunctionFactory, ForeignFunctionInterface {
    protected final Map<String, MethodHandle> downcallHandles = new ConcurrentHashMap<>();
    protected final Linker linker;
    protected final SymbolLookup symbolLookup;
    protected final Class<?> declarations;

    public ReflectForeignFunctions(Class<?> declarations) {
        this(declarations, SymbolLookup.loaderLookup());
    }

    public ReflectForeignFunctions(Class<?> declarations, SymbolLookup symbolLookup) {
        this(declarations, symbolLookup, Linker.nativeLinker());
    }

    public ReflectForeignFunctions(Class<?> declarations, SymbolLookup symbolLookup, Linker linker) {
        this.linker = linker;
        this.symbolLookup = symbolLookup;
        this.declarations = declarations;
        for (var method : declarations.getDeclaredMethods()) {
            var methodHandle = createDowncallHandle(method);
            if (methodHandle != null) {
                downcallHandles.put(method.getName(), methodHandle);
            }
        }
    }

    @Override
    public MethodHandle createDowncallHandle(Method method) {
        var address = symbolLookup.find(method.getName());
        if (address.isEmpty()) {
            return null;
        }
        var returnLayout = createReturnLayout(method);
        var argumentLayouts = createArgumentLayouts(method).toArray(MemoryLayout[]::new);
        //noinspection OptionalIsPresent
        var functionDescriptor = returnLayout.isEmpty() ?
                FunctionDescriptor.ofVoid(argumentLayouts) :
                FunctionDescriptor.of(returnLayout.get(), argumentLayouts);
        return linker.downcallHandle(address.get(), functionDescriptor);
    }

    @Override
    public MethodHandle downcallHandle(String name) {
        return Objects.requireNonNull(downcallHandles.get(name));
    }
}
