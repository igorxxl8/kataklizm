package parser;

/**
 * @author Igor Turcevich
 */
public final class Token {

    private ITokenType type;
    private String text;
    int posstr;
    int posfile;

    Token(ITokenType type, String text, int posstr, int posfile) {
        this.type = type;
        this.text = text;
        this.posstr = posstr;
        this.posfile = posfile;
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
