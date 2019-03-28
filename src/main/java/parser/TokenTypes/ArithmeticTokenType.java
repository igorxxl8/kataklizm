package parser.TokenTypes;

import parser.interfaces.IArithmeticTokenType;

public enum ArithmeticTokenType implements IArithmeticTokenType {
    PLUS,
    MINUS,
    STAR,
    SLASH;

    @Override
    public String toString() {
        switch (this) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case STAR:
                return "*";
            case SLASH:
                return "/";
        }

        return "";
    }
}
