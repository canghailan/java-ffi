package cc.whohow.ffi;

import cc.whohow.ffi.provider.reflect.ReflectForeignFunctions;
import org.junit.jupiter.api.Test;

public class ReflectForeignFunctionsTest {
    @Test
    public void test() {
        // https://github.com/libvips/build-win64-mxe/releases
        // add libvips to PATH or LD_LIBRARY_PATH
        System.out.println(System.getenv("PATH"));
        System.loadLibrary("libvips-42");

        var vips = ReflectForeignFunctions.newProxyInstance(Vips.class);
        System.out.println(vips.vips_version(0));
        System.out.println(vips.vips_version(1));
        System.out.println(vips.vips_version(2));
    }
}
