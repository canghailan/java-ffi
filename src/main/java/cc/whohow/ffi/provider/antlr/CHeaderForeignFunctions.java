package cc.whohow.ffi.provider.antlr;

import cc.whohow.ffi.provider.xml.DocumentForeignFunctions;
import org.antlr.v4.runtime.CharStream;

import java.io.InputStream;
import java.io.Reader;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.nio.file.Path;

public class CHeaderForeignFunctions extends DocumentForeignFunctions {
    public CHeaderForeignFunctions(CharStream declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CHeaderForeignFunctions(CharStream declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CHeaderForeignFunctions(CharStream declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }

    public CHeaderForeignFunctions(String declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CHeaderForeignFunctions(String declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CHeaderForeignFunctions(String declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }

    public CHeaderForeignFunctions(Reader declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CHeaderForeignFunctions(Reader declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CHeaderForeignFunctions(Reader declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }

    public CHeaderForeignFunctions(InputStream declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CHeaderForeignFunctions(InputStream declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CHeaderForeignFunctions(InputStream declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }

    public CHeaderForeignFunctions(Path declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CHeaderForeignFunctions(Path declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CHeaderForeignFunctions(Path declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }
}
