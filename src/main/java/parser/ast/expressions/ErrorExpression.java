package parser.ast.expressions;

import stdlib.Value;

public class ErrorExpression implements Expression {
    @Override
    public Value eval() {
        return null;
    }
}
