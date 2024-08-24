package cc.whohow.ffi;

import cc.whohow.ffi.provider.CachingForeignFunctionInterface;
import cc.whohow.ffi.provider.xml.DocumentForeignFunctions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class DocumentForeignFunctionsTest {
    public Path source() {
        try {
            var url = getClass().getResource("/test.xml");
            var path = Path.of(Objects.requireNonNull(url).toURI());
            return path.toAbsolutePath().normalize();
        } catch (URISyntaxException e) {
            throw new UncheckedIOException(new IOException(e));
        }
    }

    public Document functions() {
        try (var stream = Files.newInputStream(source())) {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ParserConfigurationException | SAXException e) {
            throw new UncheckedIOException(new IOException(e));
        }
    }

    @Test
    public void test() throws Throwable {
        // https://github.com/libvips/build-win64-mxe/releases
        // add libvips to PATH or LD_LIBRARY_PATH
        System.out.println(System.getenv("PATH"));
        System.loadLibrary("libvips-42");

        var ffi = new CachingForeignFunctionInterface(new DocumentForeignFunctions(functions()));

        var vipsVersion = ffi.downcallHandle("vips_version");
        System.out.println(vipsVersion.invoke(0));
        System.out.println(vipsVersion.invoke(1));
        System.out.println(vipsVersion.invoke(2));

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
