package parser.TokenTypes;

import parser.interfaces.IKeywordTokenType;

public enum KeywordTokenType implements IKeywordTokenType {
    OUT,
    IN,
    IF,
    ELSE,
    LOOP,
    FOR,
    BREAK,
    CONTINUE,
    MATCH,
    CASE,

    FUNC,
    RET;

    public String getName(){
        return super.toString().toLowerCase();
    }

    @Override
    public String toString() {
        var s = super.toString().toLowerCase();
        var description = "key word \"" + s + "\"";
        switch (s) {
            case "if":
                description += " is the condition statement";
                break;
            case "ret":
                description += " is the return statement";
                break;
            case "loop":
                description += " is the while statement";
                break;
            case "for":
                description += " is the loop for statement";
                break;
            case "match":
                description += " is the switch statement";
                break;
            case "else":
                description += " is the condition statement";
                break;

            case "break":
            case "continue":
                description += " is the loop life cycle statement";
                break;

            case "case":
                description += " is the case statement";
                break;

            case "func":
                description += " is the function implementation";
                break;

            case "in":
            case "out":
                description += " is the input output stream";
                break;
        }


        var separator = "\t";
        if (s.length() < 4) {
            separator = "\t\t";
        }
        return String.format("%s%s|%s", s, separator, description);
    }
}
