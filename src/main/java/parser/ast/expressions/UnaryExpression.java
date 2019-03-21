package parser.ast.expressions;

import stdlib.NumberValue;
import stdlib.Value;

public final class UnaryExpression implements Expression {

    private final char operation;
    private final Expression expr;

    public UnaryExpression(char operation, Expression expr) {
        this.operation = operation;
        this.expr = expr;
    }

    @Override
    public Value eval() {
        switch (operation){
            case '-' : return new NumberValue(-expr.eval().asNumber());
            case '+' :
            default: return new NumberValue(expr.eval().asNumber());
        }
    }

    @Override
    public String toString() {
        return String.format("<UnaryExpression operation=\"%c\">%s</UnaryExpression>)", operation, expr);

    }
}
