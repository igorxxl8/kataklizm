import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import parser.*;
import parser.ast.statements.Statement;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;


public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        if (args.length < 1) {
            System.out.println("Usage: katazm source_code_file mode(Debug,Basic)");
        }

        final var filename = args[0];
        final var input = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);

        final var tokens = new Lexer(input).tokenize();


        if (args.length > 1) {
            System.out.println("\nTokens:");
            var keywordList = new HashSet<IKeywordTokenType>();
            var variableList = new HashSet<String>();
            var valueList = new HashMap<String, Token>();
            var operatorsList = new HashSet<String>();
            var constantsList = new HashSet<String>();
            var constantsValueList = new HashMap<String, String>();
            Token previousToken;
            Token currentToken = null;
            int i = 0;
            for (var token : tokens) {
                previousToken = currentToken;
                currentToken = token;

                if (currentToken != null && currentToken.getType() == TokenType.EQ) {
                    if (previousToken != null && previousToken.getType() == TokenType.WORD) {
                        variableList.add(previousToken.getText());
                        valueList.put(previousToken.getText(), tokens.get(i + 1));
                    }
                }

                if (currentToken != null && currentToken.getType() == TokenType.LPAREN) {
                    if (previousToken != null && previousToken.getType() == TokenType.WORD) {
                        for (var a : KeywordTokenType.values()) {
                            var prevText = previousToken.getText();
                            var kwtext = a.getName();
                            if (kwtext.contains(prevText) || prevText.contains(kwtext)) {
                                var b = Lexer.WordPosition.get(prevText);
                                Lexer.Errors.add(String.format("%d:%d: error: implicit declaration of function: \'%s\'", b.a, b.b, prevText));
                            }
                        }
                    }

                } else if (currentToken != null && currentToken.getType() == TokenType.NUMBER) {
                    if (previousToken != null && previousToken.getType() != TokenType.EQ) {
                        var value = currentToken.getText();
                        constantsList.add(value);
                        var t = "";
                        if (value.contains(".")) {
                            t = "double";
                        } else {
                            t = "integer";
                        }
                        constantsValueList.put(value, t);

                    }
                }

                var tokenType = token.getType();
                if (tokenType instanceof IKeywordTokenType) {
                    keywordList.add((IKeywordTokenType) tokenType);
                } else if (tokenType instanceof IArithmeticTokenType) {
                    operatorsList.add((tokenType + " | is arithmetic operator"));
                } else if (tokenType instanceof IComparisonTokenType) {
                    operatorsList.add((tokenType + " | is comparison operator"));
                }

                System.out.println(String.format("\t%s", token));
                i++;
            }

            System.out.println("\n\nVARIABLES TABLE");
            for (var vt :
                    variableList) {
                var token = valueList.get(vt);
                var type = token.getType();
                var t = "";
                var value = valueList.get(vt).getText();
                if (type == TokenType.TEXT) {
                    t = "string";
                } else if (type == TokenType.NUMBER) {
                    if (value.contains(".")) {
                        t = "double";
                    } else {
                        t = "integer";
                    }
                }
                System.out.println(vt + " = " + value + " | " + t);
            }

            System.out.println("\n\nKEY WORDS TABLE");
            for (var tt : keywordList) {
                System.out.print(String.format("%s\n", tt));
            }

            System.out.println("\n\nOPERATORS TABLE");
            for (var ol : operatorsList) {
                System.out.println(ol);
            }

            System.out.println("\n\nCONSTANTS TABLE");
            for (var cl : constantsList) {
                System.out.println(cl + " | " + constantsValueList.get(cl));
            }

            if (!Lexer.Errors.isEmpty()) {
                System.out.println("\nErrors:");
            }
            for (var error :
                    Lexer.Errors) {
                System.out.println(String.format("%s:%s", filename, error));
            }

            System.out.println("\nExecution:");
        }

        final var parser = new Parser(tokens);
        final var program = parser.parse();
        System.err.println(parser.errors);

        var doc = BuildTree(program, filename);
        System.out.println(ConvertXmlDocumentToString(doc));
        //program.execute();
    }

    private static Document BuildTree(Statement program, String name) throws IOException, ParserConfigurationException, SAXException {
        var tree = String.format("<Program name=\"%s\">", name) + program.toString() + "</Program>";
        var db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return db.parse(new InputSource(new StringReader(tree)));
    }

    private static String ConvertXmlDocumentToString(Document document) throws TransformerException {
        var tf = TransformerFactory.newInstance();
        var transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        var sw = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(sw));
        return sw.toString();
    }
}
