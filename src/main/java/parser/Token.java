package parser;

/**
 * @author Igor Turcevich
 */
public final class Token {

    private TokenType type;
    private String text;

    public Token(){}

    Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        if (!text.isEmpty())
            return "Token(" + type + ", " + text + ')';

        return "Token(" + type + ')';
    }

    TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
