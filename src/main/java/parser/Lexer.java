package parser;



import java.util.ArrayList;
import java.util.List;


/**
 * @author Igor Turcevich
 */
public final class Lexer {

    private static final String OPERATORS_CHARS = "+-*/()=";
    private static final TokenType[] OPERATOR_TOKENS = {
            TokenType.PLUS,
            TokenType.MINUS,
            TokenType.STAR,
            TokenType.SLASH,
            TokenType.LPAREN,
            TokenType.RPAREN,
            TokenType.EQ
    };
    private String input;
    private List<Token> tokens;
    private int pos;
    private final int length;

    public Lexer(String input) {
        this.input = input;
        length = input.length();
        tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        while (pos < length) {
            final char current = peek(0);
            if (Character.isDigit(current)) {
                tokenizeNumber();
            } else if (Character.isLetter(current)) {
                tokenizeWord();

            } else if (current ==  '"') {
                tokenizeText();
            } else if(OPERATORS_CHARS.indexOf(current) != -1){
                tokenizeOperator();
            } else{
                // whitespaces
                next();
            }
        }

        return tokens;
    }

    private void tokenizeText() {
        next();
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '\\'){
                current = next();
                switch (current){
                    case '"': current = next(); buffer.append('"'); continue;
                    case 'n': current = next(); buffer.append('\n'); continue;
                    case 't': current = next(); buffer.append('\t'); continue;
                }
                buffer.append('\\');
                continue;
            }

            if (current == '"'){
                break;
            }

            buffer.append(current);
            current = next();
        }
        next();
        addToken(TokenType.TEXT, buffer.toString());

    }

    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isLetterOrDigit(current) || (current == '_') || (current == '$')) {
            buffer.append(current);
            current = next();
        }

        final String toString = buffer.toString();
        if (toString.endsWith("out")){
            addToken(TokenType.PRINT);
        } else {
            addToken(TokenType.WORD, toString);
        }
    }

    private void tokenizeOperator() {
        final int position = OPERATORS_CHARS.indexOf(peek(0));
        addToken(OPERATOR_TOKENS[position]);
        next();
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true){
            if (current == '.'){
                if (buffer.indexOf(".") != -1){
                    throw new RuntimeException("Invalid float number!");
                }
            } else if (!Character.isDigit(current)) {
                break;
            }
            buffer.append(current);
            current = next();
        }

        addToken(TokenType.NUMBER, buffer.toString());
    }


    private char next(){
        pos++;
        return peek(0);
    }

    private char peek(int relativePosition){
        final int position = pos + relativePosition;

        if (position >= length)
            return '\0';

        return input.charAt(position);
    }

    private void addToken(TokenType type) {
        addToken(type, "");
    }

    private void addToken(TokenType type, String text){
        tokens.add(new Token(type, text));
    }
}
