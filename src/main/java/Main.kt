import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import parser.Lexer
import parser.LexicalError
import parser.Parser
import parser.SemanticError
import parser.ast.statements.Statement

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths


object Main {
    @Throws(IOException::class, ParserConfigurationException::class, SAXException::class, TransformerException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            println("Usage: katazm source_code_file mode(Debug,Basic)")
        }

        val filename = args[0]
        val input = String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8)

        // LR1
        val tokens = Lexer(input).tokenize()

        // lR2
        val lexical = LexicalError(tokens, filename)

        // LR3
        val parser = Parser(tokens)
        val program = parser.parse()
        val tree = buildTree(program, filename)

        // LR4
        val semantic = SemanticError(tree)

        if (args.size > 1) {
            lexical.Analyze()
            System.err.println(parser.errors)
            println(convertXmlDocumentToString(tree))
            semantic.analyze()
            System.err.println(semantic.errors)
        }

        // LR5
        program.execute()
    }

    @Throws(IOException::class, ParserConfigurationException::class, SAXException::class)
    private fun buildTree(program: Statement, name: String): Document {
        val tree = String.format("<Program name=\"%s\">", name) + program.toString() + "</Program>"
        val db = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        return db.parse(InputSource(StringReader(tree)))
    }

    @Throws(TransformerException::class)
    private fun convertXmlDocumentToString(document: Document): String {
        val tf = TransformerFactory.newInstance()
        val transformer = tf.newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        val sw = StringWriter()
        transformer.transform(DOMSource(document), StreamResult(sw))
        return sw.toString()
    }


}
