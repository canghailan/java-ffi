package cc.whohow.ffi.provider.xml;

import cc.whohow.ffi.ForeignFunctionInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

public class DocumentForeignFunctions implements DocumentFunctionFactory, ForeignFunctionInterface {
    protected final Linker linker;
    protected final SymbolLookup symbolLookup;
    protected final Document declarations;

    public DocumentForeignFunctions(Document declarations) {
        this(declarations, SymbolLookup.loaderLookup());
    }

    public DocumentForeignFunctions(Document declarations, SymbolLookup symbolLookup) {
        this(declarations, symbolLookup, Linker.nativeLinker());
    }

    public DocumentForeignFunctions(Document declarations, SymbolLookup symbolLookup, Linker linker) {
        this.linker = linker;
        this.symbolLookup = symbolLookup;
        this.declarations = declarations;
    }

    public static DocumentBuilder newDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public MethodHandle createDowncallHandle(Element function) {
        if (function == null) {
            throw new IllegalArgumentException();
        }
        var name = function.getAttribute("name");
        var address = symbolLookup.find(name);
        if (address.isEmpty()) {
            throw new IllegalArgumentException();
        }
        var returnLayout = createReturnLayout(function);
        var argumentLayouts = createArgumentLayouts(function).toArray(MemoryLayout[]::new);
        //noinspection OptionalIsPresent
        var functionDescriptor = returnLayout.isEmpty() ?
                FunctionDescriptor.ofVoid(argumentLayouts) :
                FunctionDescriptor.of(returnLayout.get(), argumentLayouts);
        return linker.downcallHandle(address.get(), functionDescriptor);
    }

    public MethodHandle downcallHandle(String name) {
        var byName = "/declarations/function[@name='%s']".formatted(name);
        var function = (Element) DocumentFunctionFactory.queryNode(byName, declarations);
        return createDowncallHandle(function);
    }
}
