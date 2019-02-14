import parser.Lexer;
import parser.Parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: katazm source_code_file mode(Debug,Basic)");
        }
        final var filename = args[0];
        final var input = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);

        final var tokens = new Lexer(input).tokenize();
        final var program = new Parser(tokens).parse();

        if (args.length > 1) {
            System.out.println("\nTokens:");
            for (var token : tokens) {
                System.out.println(String.format("\t%s", token));
            }
            System.out.println("\nExecution:");
        }
        program.execute();
    }
}
