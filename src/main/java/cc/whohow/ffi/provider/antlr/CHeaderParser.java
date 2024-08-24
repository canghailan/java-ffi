package cc.whohow.ffi.provider.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.v4.Tool;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.Grammar;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public class CHeaderParser {
    protected final Grammar grammar;

    public CHeaderParser() {
        this(Source.C);
    }

    public CHeaderParser(String source) {
        var tool = new Tool();
        var ast = tool.parse("C.g4", new ANTLRStringStream(source));
        var grammar = tool.createGrammar(ast);
        grammar.fileName = ast.fileName;
        tool.process(grammar, false);
        this.grammar = grammar;
    }

    public CHeaderParser(Grammar grammar) {
        this.grammar = grammar;
    }

    public Grammar grammar() {
        return grammar;
    }

    public ParseTree parse(CharStream charStream) {
        var rule = grammar.getRule("compilationUnit");
        var lexer = grammar.createLexerInterpreter(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = grammar.createParserInterpreter(tokenStream);
        return parser.parse(rule.index);
    }

    public ParseTree parse(String string) {
        return parse(CharStreams.fromString(string));
    }

    public ParseTree parse(Reader reader) {
        try {
            return parse(CharStreams.fromReader(reader));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public ParseTree parse(InputStream stream) {
        try {
            return parse(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public ParseTree parse(Path path) {
        try {
            return parse(CharStreams.fromPath(path, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Document parse(CharStream charStream, DocumentBuilder documentBuilder) {
        var document = documentBuilder.newDocument();
        new DocumentGenerator(document, grammar).visit(parse(charStream));
        return document;
    }

    public Document parse(String string, DocumentBuilder documentBuilder) {
        return parse(CharStreams.fromString(string), documentBuilder);
    }

    public Document parse(Reader reader, DocumentBuilder documentBuilder) {
        try {
            return parse(CharStreams.fromReader(reader), documentBuilder);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Document parse(InputStream stream, DocumentBuilder documentBuilder) {
        try {
            return parse(CharStreams.fromStream(stream, StandardCharsets.UTF_8), documentBuilder);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Document parse(Path path, DocumentBuilder documentBuilder) {
        try {
            return parse(CharStreams.fromPath(path, StandardCharsets.UTF_8), documentBuilder);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static class Source {
        public static final String C = readClasspath("/C.g4");

        public static String readClasspath(String classpath) {
            try (var stream = Objects.requireNonNull(Source.class.getResourceAsStream(classpath))) {
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
