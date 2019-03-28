package parser.TokenTypes;

import parser.interfaces.ITokenType;

/**
 * @author Igor Turcevich
 */
public final class Token {

    private ITokenType type;
    private String text;

    public Token(ITokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        if (!text.isEmpty())
            return "Token(" + type + ", \"" + text + "\")";

        return "Token(" + type + ')';
    }

    public ITokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
