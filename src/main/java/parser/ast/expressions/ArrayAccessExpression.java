package parser.ast.expressions;

import stdlib.ArrayValue;
import stdlib.Value;
import stdlib.Variables;

public class ArrayAccessExpression implements Expression {

    private final String var;
    private final Expression ind;

    public ArrayAccessExpression(String var, Expression ind) {
        this.var = var;
        this.ind = ind;
    }

    @Override
    public Value eval() {
        final var variable = Variables.get(this.var);
        if (variable instanceof ArrayValue) {
            final ArrayValue array = (ArrayValue) variable;
            return array.get((int)ind.eval().asNumber());
        } else {
            throw new RuntimeException("Array expected!");
        }
    }

    @Override
    public String toString() {
        return String.format("ArrayAccessExpression(%s[%s] = %s)", var, ind);
    }
}
