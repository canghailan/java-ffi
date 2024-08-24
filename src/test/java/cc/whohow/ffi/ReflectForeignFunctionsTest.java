package cc.whohow.ffi;

import cc.whohow.ffi.provider.reflect.ReflectForeignFunctions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class ReflectForeignFunctionsTest {
    @Test
    public void test() {
        // https://github.com/libvips/build-win64-mxe/releases
        // add libvips to PATH or LD_LIBRARY_PATH
        System.out.println(System.getenv("PATH"));
        System.loadLibrary("libvips-42");

        var ffi = new ReflectForeignFunctions(Vips.class);
        var vips = (Vips) Proxy.newProxyInstance(
                Vips.class.getClassLoader(),
                new Class[]{Vips.class},
                new ForeignFunctionInvocationHandler(ffi)
        );
        System.out.println(vips.vips_version(0));
        System.out.println(vips.vips_version(1));
        System.out.println(vips.vips_version(2));
    }
}
