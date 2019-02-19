package parser;

import org.parboiled.common.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Igor Turcevich
 */
public final class Lexer {

    public static List<String> Errors;
    public static HashMap<String, Tuple2<Integer, Integer>> WordPosition = new HashMap<>();

    private static final String OPERATORS_CHARS = "+-*/(){}[]=<>&!|;,";

    private static final Map<String, ITokenType> OPERATORS;

    static {
        Errors = new ArrayList<>();
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", ArithmeticTokenType.PLUS);
        OPERATORS.put("-", ArithmeticTokenType.MINUS);
        OPERATORS.put("*", ArithmeticTokenType.STAR);
        OPERATORS.put("/", ArithmeticTokenType.SLASH);
        OPERATORS.put("{", TokenType.LBRACE);
        OPERATORS.put("}", TokenType.RBRACE);
        OPERATORS.put("[", TokenType.LBRACKET);
        OPERATORS.put("]", TokenType.RBRACKET);
        OPERATORS.put("(", TokenType.LPAREN);
        OPERATORS.put(")", TokenType.RPAREN);
        OPERATORS.put("=", TokenType.EQ);
        OPERATORS.put("<", ComparisonTokenType.LT);
        OPERATORS.put(">", ComparisonTokenType.GT);
        OPERATORS.put(";", TokenType.SEMI_COLON);
        OPERATORS.put(",", TokenType.COMMA);
        OPERATORS.put("!", ComparisonTokenType.EXCL);
        OPERATORS.put("&", TokenType.AMP);
        OPERATORS.put("|", TokenType.BAR);
        OPERATORS.put("==", ComparisonTokenType.EQEQ);
        OPERATORS.put("!=", ComparisonTokenType.EXCLEQ);
        OPERATORS.put("<=", ComparisonTokenType.LTEQ);
        OPERATORS.put("->", TokenType.ARROW);
        OPERATORS.put(">=", ComparisonTokenType.GTEQ);
        OPERATORS.put("&&", TokenType.AMPAMP);
        OPERATORS.put("||", TokenType.BARBAR);
    }

    private String input;
    private List<Token> tokens;
    private int pos;
    private int strnum;
    private int posinstr;
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

            } else if (current == '"') {
                tokenizeText();
            } else if (OPERATORS_CHARS.indexOf(current) != -1) {
                tokenizeOperator();
            } else if (current != ' ' && current != '\n' && current != '\r') {
                Errors.add(String.format("%d:%d: error: Illegal character: \'%s\'", strnum / 2, posinstr, current));
                next();
            } else {
                // whitespaces
                next();
            }
        }

        return tokens;
    }

    private void tokenizeText() {
        next();
        final var buffer = new StringBuilder();
        var current = peek(0);
        while (true) {
            if (current == '\\') {
                current = next();
                switch (current) {
                    case '"':
                        current = next();
                        buffer.append('"');
                        continue;
                    case 'n':
                        current = next();
                        buffer.append('\n');
                        continue;
                    case 't':
                        current = next();
                        buffer.append('\t');
                        continue;
                }
                buffer.append('\\');
                continue;
            }

            if (current == '"') {
                break;
            }

            buffer.append(current);
            current = next();
        }
        next();
        addToken(TokenType.TEXT, buffer.toString());

    }

    private void tokenizeWord() {
        final var buffer = new StringBuilder();
        var current = peek(0);
        while (Character.isLetterOrDigit(current) || (current == '_') || (current == '$')) {
            buffer.append(current);
            current = next();
        }

        final var word = buffer.toString();
        Token prevToken = new Token(ArithmeticTokenType.MINUS, "-");
        if (tokens.size() - 1 >= 0)
            prevToken = tokens.get(tokens.size() - 1);
        switch (word) {
            case "out":
                addToken(KeywordTokenType.OUT);
                break;

            case "in":
                addToken(KeywordTokenType.IN);
                break;

            case "if":
                addToken(KeywordTokenType.IF);
                break;

            case "match":
                addToken(KeywordTokenType.MATCH);
                break;

            case "case":
                addToken(KeywordTokenType.CASE);
                break;

            case "else":
                addToken(KeywordTokenType.ELSE);
                break;
            case "loop":
                addToken(KeywordTokenType.LOOP);
                break;

            case "for":
                addToken(KeywordTokenType.FOR);
                break;

            case "continue":
                addToken(KeywordTokenType.CONTINUE);
                break;

            case "break":
                addToken(KeywordTokenType.BREAK);
                break;

            case "func":
                addToken(KeywordTokenType.FUNC);
                break;

            case "ret":
                addToken(KeywordTokenType.RET);
                break;

            default:
                WordPosition.put(word, new Tuple2<>(strnum / 2, posinstr));
                addToken(TokenType.WORD, word);
                break;
        }
        var curToken = tokens.get(tokens.size() - 1);
        if (prevToken.getType() instanceof IKeywordTokenType && curToken.getType() instanceof IKeywordTokenType){
            System.out.println(curToken.getType());
            Errors.add(String.format("%d:%d: error: extra keyword on position: \'%s\'", strnum / 2, posinstr, curToken.getType()));
        }
    }

    private void tokenizeOperator() {
        var current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                next();
                next();
                tokenizeComment();
                return;
            }
        }

        final var buffer = new StringBuilder();
        while (true) {
            final var text = buffer.toString();
            if (!OPERATORS.containsKey(text + current) && !text.isEmpty()) {
                var prevToken = tokens.get(tokens.size() - 1);
                addToken(OPERATORS.get(text));
                var curToken = tokens.get(tokens.size() - 1);
                if (prevToken.getType() instanceof IArithmeticTokenType && curToken.getType() instanceof IArithmeticTokenType){
                    System.out.println(curToken.getType());
                    Errors.add(String.format("%d:%d: error: extra arithmetic expression: \'%s\'", strnum / 2, posinstr, curToken.getType()));
                }
                if (prevToken.getType() instanceof IComparisonTokenType && curToken.getType() instanceof IComparisonTokenType){
                    System.out.println(curToken.getType());
                    Errors.add(String.format("%d:%d: error: extra logical expression: \'%s\'", strnum / 2, posinstr, curToken.getType()));
                }
                return;
            }
            buffer.append(current);
            current = next();
        }

    }

    private void tokenizeComment() {
        var current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private void tokenizeNumber() {
        final var buffer = new StringBuilder();
        var current = peek(0);
        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) {
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


    private char next() {
        pos++;
        posinstr++;
        return peek(0);
    }

    private char peek(int relativePosition) {
        final var position = pos + relativePosition;

        if (position >= length)
            return '\0';

        final var ch = input.charAt(position);
        if (ch == '\n') {
            strnum++;
            posinstr = 0;
        }
        return ch;
    }

    private void addToken(ITokenType type) {
        addToken(type, "");
    }

    private void addToken(ITokenType type, String text) {
        tokens.add(new Token(type, text));
    }
}
