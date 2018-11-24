import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.statements.Statement;
import stdlib.Variables;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        final String input = new String(Files.readAllBytes(Paths.get("examples/1.kat")), "UTF-8");
        final List<Token> tokens = new Lexer(input).tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }

        final List<Statement> statements = new Parser(tokens).parse();

        for (Statement stat : statements) {
            System.out.println(stat);
        }

        for (Statement stat : statements) {
            stat.execute();
        }


    }
}
