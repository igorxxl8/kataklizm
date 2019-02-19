package parser;

public enum ComparisonTokenType implements IComparisonTokenType {
    EQEQ,
    EXCL,
    EXCLEQ,
    LT,
    LTEQ,
    GT,
    GTEQ;

    @Override
    public String toString() {
        switch (this) {
            case EQEQ:
                return "==";
            case EXCL:
                return "!";
            case EXCLEQ:
                return "!=";
            case LT:
                return "<";
            case LTEQ:
                return "<=";
            case GT:
                return ">";
            case GTEQ:
                return ">=";
        }

        return "";
    }
}
