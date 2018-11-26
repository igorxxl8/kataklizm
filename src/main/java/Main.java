import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.statements.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        final String filename = args[0];
        final String mode = args[1];
        final String input = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");

        final List<Token> tokens = new Lexer(input).tokenize();
        final var program = new Parser(tokens).parse();

        if (mode.equals("Debug")) {
            System.out.println("\nTokens:");
            for (Token token : tokens) {
                System.out.println(String.format("\t%s", token));
            }
            System.out.println("\nExecution:");
        }
        program.execute();
    }
}
