import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import parser.Lexer;
import parser.LexicalError;
import parser.Parser;
import parser.SemanticError;
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


public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        if (args.length < 1) {
            System.out.println("Usage: katazm source_code_file mode(Debug,Basic)");
        }

        final var filename = args[0];
        final var input = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);

        // LR1
        final var tokens = new Lexer(input).tokenize();

        // lR2
        final var lexical = new LexicalError(tokens, filename);

        // LR3
        final var parser = new Parser(tokens);
        final var program = parser.parse();
        final var tree = BuildTree(program, filename);

        // LR4
        final var semantic = new SemanticError(tree);

        if (args.length > 1) {
            lexical.Analyze();
            System.err.println(parser.errors);
            System.out.println(ConvertXmlDocumentToString(tree));
            semantic.Analyze();
            System.err.println(semantic.getErrors());
        }

        // LR5
        program.execute();
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
