package cc.whohow.ffi.provider.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.*;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @see java.lang.foreign.Linker#downcallHandle(MemorySegment, FunctionDescriptor, Linker.Option...)
 */
public interface DocumentFunctionFactory extends DocumentMemoryLayoutFactory {
    XPathExpression RETURN = compileXPathExpression("return");
    XPathExpression ARGUMENT = compileXPathExpression("arguments/argument");
    XPathExpression TYPE = compileXPathExpression("*");

    static XPath newXPath() {
        return XPathFactory.newInstance().newXPath();
    }

    static XPathExpression compileXPathExpression(String expression) {
        try {
            return newXPath().compile(expression);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static Object evaluateXPathExpression(String expression, Object item, QName returnType) {
        try {
            return newXPath().evaluate(expression, item, returnType);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static Node queryNode(String expression, Object item) {
        return (Node) evaluateXPathExpression(expression, item, XPathConstants.NODE);
    }

    static NodeList queryNodeList(String expression, Object item) {
        return (NodeList) evaluateXPathExpression(expression, item, XPathConstants.NODESET);
    }

    MethodHandle createDowncallHandle(Element function);

    default Optional<MemoryLayout> createReturnLayout(Element function) {
        try {
            var ret = (Element) RETURN.evaluate(function, XPathConstants.NODE);
            if (ret == null) {
                return Optional.empty();
            }
            var type = (Element) TYPE.evaluate(ret, XPathConstants.NODE);
            return Optional.of(createMemoryLayout(type));
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    default List<MemoryLayout> createArgumentLayouts(Element function) {
        try {
            var args = (NodeList) ARGUMENT.evaluate(function, XPathConstants.NODESET);
            if (args == null) {
                return List.of();
            }
            var length = args.getLength();
            if (length == 0) {
                return List.of();
            }
            var argumentLayouts = new ArrayList<MemoryLayout>(length);
            for (int i = 0; i < length; i++) {
                var arg = (Element) args.item(i);
                var name = arg.getAttribute("name");
                var type = (Element) TYPE.evaluate(arg, XPathConstants.NODE);
                if (name.isEmpty()) {
                    argumentLayouts.add(createMemoryLayout(type));
                } else {
                    argumentLayouts.add(createMemoryLayout(type).withName(name));
                }
            }
            return argumentLayouts;
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
