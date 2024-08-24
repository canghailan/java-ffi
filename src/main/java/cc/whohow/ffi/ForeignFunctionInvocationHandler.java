package cc.whohow.ffi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ForeignFunctionInvocationHandler implements InvocationHandler {
    protected final ForeignFunctionInterface foreignFunctionInterface;

    public ForeignFunctionInvocationHandler(ForeignFunctionInterface foreignFunctionInterface) {
        this.foreignFunctionInterface = foreignFunctionInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return foreignFunctionInterface.downcall(method.getName(), args);
    }
}
