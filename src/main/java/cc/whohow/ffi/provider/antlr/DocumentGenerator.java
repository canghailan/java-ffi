package cc.whohow.ffi.provider.antlr;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.Rule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayDeque;
import java.util.Deque;

public class DocumentGenerator implements ParseTreeVisitor<Element> {
    protected final Grammar grammar;
    protected final Document document;
    protected Element declarations;
    protected String type;
    protected Deque<Element> tokens;

    public DocumentGenerator(Document document, Grammar grammar) {
        this.grammar = grammar;
        this.document = document;
        this.tokens = new ArrayDeque<>();
    }

    protected Rule getRule(RuleNode node) {
        return grammar.getRule(node.getRuleContext().getRuleIndex());
    }

    protected String getRuleName(RuleNode node) {
        return getRule(node).name;
    }

    protected String getTokenSymbolicName(TerminalNode node) {
        return grammar.getTokenSymbolicNames()[node.getSymbol().getType()];
    }

    @Override
    public Element visit(ParseTree tree) {
        declarations = document.createElement("declarations");
        tree.accept(this);
        document.appendChild(declarations);
        return declarations;
    }

    @Override
    public Element visitChildren(RuleNode node) {
        var rule = getRule(node);
        switch (rule.name) {
            case "externalDeclaration" -> {
                type = null;
                tokens.clear();
                for (int i = 0; i < node.getChildCount(); i++) {
                    node.getChild(i).accept(this);
                }
                switch (type) {
                    case "function" -> {
                        var function = document.createElement("function");

                        var returnTypeNode = tokens.removeFirst();
                        var returnNode = document.createElement("return");
                        returnNode.appendChild(returnTypeNode);
                        function.appendChild(returnNode);

                        var identifierNode = tokens.removeFirst();
                        function.setAttribute("name", identifierNode.getTextContent());

                        var argumentsNode = document.createElement("arguments");
                        while (!tokens.isEmpty()) {
                            var argumentType = tokens.removeFirst();
                            var argumentName = tokens.removeFirst();
                            var argumentNode = document.createElement("argument");
                            argumentNode.setAttribute("name", argumentName.getTextContent());
                            argumentNode.appendChild(argumentType);
                            argumentsNode.appendChild(argumentNode);
                        }
                        function.appendChild(argumentsNode);

                        declarations.appendChild(function);
                        return function;
                    }
                }
                return null;
            }
            case "parameterTypeList" -> {
                if (type == null) {
                    type = "function";
                }
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            var token = node.getChild(i).accept(this);
            if (token != null) {
                tokens.add(token);
            }
        }
        return null;
    }

    protected Element visitParameterTypeList(RuleNode node) {
        var arguments = document.createElement("arguments");
        for (int i = 0; i < node.getChildCount(); i++) {
            var argument = node.getChild(i).accept(this);
            arguments.appendChild(argument);
        }
        return arguments;
    }

    private Element visitTypeSpecifier(RuleNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            var declaration = node.getChild(i).accept(this);
            if (declaration != null) {
                return declaration;
            }
        }
        return null;
    }

    @Override
    public Element visitTerminal(TerminalNode node) {
        if (node.getSymbol().getType() == Token.EOF) {
            return null;
        }
        switch (getTokenSymbolicName(node)) {
            case "Identifier" -> {
                var element = document.createElement("identifier");
                element.setTextContent(node.getText());
                return element;
            }
            case "Void" -> {
                return document.createElement("void");
            }
            case "Bool" -> {
                return document.createElement("boolean");
            }
            case "Char" -> {
                return document.createElement("byte");
            }
            case "Short" -> {
                return document.createElement("short");
            }
            case "Int" -> {
                return document.createElement("int");
            }
            case "Long" -> {
                return document.createElement("long");
            }
            case "Float" -> {
                return document.createElement("float");
            }
            case "Double" -> {
                return document.createElement("double");
            }
        }
        return null;
    }

    @Override
    public Element visitErrorNode(ErrorNode node) {
        throw new IllegalStateException(node.getText());
    }
}
