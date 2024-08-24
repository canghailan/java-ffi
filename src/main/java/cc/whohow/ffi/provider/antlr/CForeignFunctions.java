package cc.whohow.ffi.provider.antlr;

import cc.whohow.ffi.provider.xml.DocumentForeignFunctions;
import org.antlr.v4.runtime.CharStream;

import java.io.InputStream;
import java.io.Reader;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.nio.file.Path;

public class CForeignFunctions extends DocumentForeignFunctions {
    public CForeignFunctions(CharStream declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CForeignFunctions(CharStream declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CForeignFunctions(CharStream declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }

    public CForeignFunctions(String declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CForeignFunctions(String declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CForeignFunctions(String declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }

    public CForeignFunctions(Reader declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CForeignFunctions(Reader declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CForeignFunctions(Reader declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }

    public CForeignFunctions(InputStream declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CForeignFunctions(InputStream declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CForeignFunctions(InputStream declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }

    public CForeignFunctions(Path declarations) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()));
    }

    public CForeignFunctions(Path declarations, SymbolLookup symbolLookup) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup);
    }

    public CForeignFunctions(Path declarations, SymbolLookup symbolLookup, Linker linker) {
        super(new CHeaderParser().parse(declarations, newDocumentBuilder()), symbolLookup, linker);
    }
}
