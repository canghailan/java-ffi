# Java [Foreign Function & Memory API](https://openjdk.org/jeps/454) 工具

1. 加载动态链接库
2. 查找函数地址，构造函数签名 FunctionDescriptor
3. 生成 MethodHandle
4. 构造参数，调用 MethodHandle，读取返回值

其中构造函数签名工作量较大，JDK 提供了 [jextract](https://jdk.java.net/jextract/) 工具，
可以通过 C 头文件 自动生成 Java 代码。
但生成的文件可读性太差，且不易维护，
因此考虑用其他方式实现。

1. 配置文件，比如 XML： 可与 MemoryLayout 构造方法一一对应，实现简单
2. C 头文件：通过 ANTLR 等工具解析，生成配置文件
3. 反射：通过反射获取方法返回类型及参数类型，配合注解，生成 FunctionDescriptor

## XML 配置文件
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<declarations>
    <function name="vips_version">
        <return>
            <int/>
        </return>
        <arguments>
            <argument name="flag">
                <int/>
            </argument>
        </arguments>
    </function>
</declarations>
```

```java
public interface Vips {
    int vips_version(int flag);
}

public static void main(String[] args) {
    // https://github.com/libvips/build-win64-mxe/releases
    // add libvips to PATH or LD_LIBRARY_PATH
    System.loadLibrary("libvips-42");
    
    var ffi = new CachingForeignFunctionInterface(new DocumentForeignFunctions(document));

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
```

## C 头文件
```c
int vips_version(int flag);
```

```java
public static void main(String[] args) {
    var ffi = new CHeaderForeignFunctions(source);
}
```

## 反射
```java
public static void main(String[] args) {
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
```