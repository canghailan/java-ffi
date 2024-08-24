package cc.whohow.ffi.provider.xml;

import org.w3c.dom.Element;

import java.lang.foreign.*;

/**
 * @see java.lang.foreign.MemoryLayout
 */
public interface DocumentMemoryLayoutFactory {
    default MemoryLayout createMemoryLayout(Element type) {
        switch (type.getTagName()) {
            case "sequence" -> {
                return createSequenceLayout(type);
            }
            case "struct" -> {
                return createStructLayout(type);
            }
            case "union" -> {
                return createUnionLayout(type);
            }
            case "address" -> {
                return createAddressLayout(type);
            }
            case "padding" -> {
                return createPaddingLayout(type);
            }
            default -> {
                return createValueLayout(type);
            }
        }
    }

    default ValueLayout createValueLayout(Element type) {
        switch (type.getTagName()) {
            case "boolean" -> {
                return ValueLayout.JAVA_BOOLEAN;
            }
            case "byte" -> {
                return ValueLayout.JAVA_BYTE;
            }
            case "short" -> {
                return ValueLayout.JAVA_SHORT;
            }
            case "int" -> {
                return ValueLayout.JAVA_INT;
            }
            case "long" -> {
                return ValueLayout.JAVA_LONG;
            }
            case "float" -> {
                return ValueLayout.JAVA_FLOAT;
            }
            case "double" -> {
                return ValueLayout.JAVA_DOUBLE;
            }
            default -> {
                throw new IllegalArgumentException(type.getTagName());
            }
        }
    }

    default SequenceLayout createSequenceLayout(Element type) {
        throw new UnsupportedOperationException("NotImplemented");
    }

    default StructLayout createStructLayout(Element type) {
        throw new UnsupportedOperationException("NotImplemented");
    }

    default UnionLayout createUnionLayout(Element type) {
        throw new UnsupportedOperationException("NotImplemented");
    }

    default AddressLayout createAddressLayout(Element type) {
        throw new UnsupportedOperationException("NotImplemented");
    }

    default PaddingLayout createPaddingLayout(Element type) {
        throw new UnsupportedOperationException("NotImplemented");
    }
}
