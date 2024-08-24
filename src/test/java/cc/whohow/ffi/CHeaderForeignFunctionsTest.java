package cc.whohow.ffi;

import cc.whohow.ffi.provider.antlr.CHeaderForeignFunctions;
import cc.whohow.ffi.provider.antlr.CHeaderParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class CHeaderForeignFunctionsTest {
    @Test
    public void test() throws Throwable {
        // https://github.com/libvips/build-win64-mxe/releases
        // add libvips to PATH or LD_LIBRARY_PATH
        System.out.println(System.getenv("PATH"));
        System.loadLibrary("libvips-42");

        var ffi = new CHeaderForeignFunctions("""
                int vips_version(int flag);
                """);

        System.out.println(ffi.downcall("vips_version", 0));
        System.out.println(ffi.downcall("vips_version", 1));
        System.out.println(ffi.downcall("vips_version", 2));
    }

    @Test
    public void testParse() throws Throwable {
        var source = CHeaderParser.Source.readClasspath("/test.h");
        var parser = new CHeaderParser();
        var grammar = parser.grammar();
        var ast = parser.parse(source);
        new ParseTreeVisitor<>() {
            @Override
            public Object visit(ParseTree tree) {
                tree.accept(this);
                return null;
            }

            @Override
            public Object visitChildren(RuleNode node) {
                var rule = grammar.getRule(node.getRuleContext().getRuleIndex());
                System.out.println("rule: " + rule.name);

                for (int i = 0; i < node.getChildCount(); i++) {
                    node.getChild(i).accept(this);
                }

                return null;
            }

            @Override
            public Object visitTerminal(TerminalNode node) {
                if (node.getSymbol().getType() == Token.EOF) {
                    return null;
                }

                var tokenName = grammar.getTokenSymbolicNames()[node.getSymbol().getType()];

                System.out.println("terminal: " + node + " : " + tokenName);
                return null;
            }

            @Override
            public Object visitErrorNode(ErrorNode node) {
                throw new IllegalStateException(node.getText());
            }
        }.visit(ast);
    }

    @Test
    public void testParesAsXml() throws Throwable {
        var source = CHeaderParser.Source.readClasspath("/test.h");
        var parser = new CHeaderParser();
        var document = parser.parse(source, DocumentBuilderFactory.newInstance().newDocumentBuilder());

        var buffer = new StringWriter();
        TransformerFactory.newInstance().newTransformer()
                .transform(new DOMSource(document), new StreamResult(buffer));
        System.out.println(buffer);
    }
}
