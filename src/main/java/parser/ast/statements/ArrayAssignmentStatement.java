package parser.ast.statements;

import parser.ast.expressions.Expression;
import stdlib.ArrayValue;
import stdlib.Variables;

public class ArrayAssignmentStatement implements Statement {

    private final String variable;
    private final Expression index;
    private final Expression expression;

    public ArrayAssignmentStatement(String variable, Expression index, Expression expression) {
        this.variable = variable;
        this.index = index;
        this.expression = expression;
    }



    @Override
    public void execute() {
        final var variable = Variables.get(this.variable);
        if (variable instanceof ArrayValue) {
            final ArrayValue array = (ArrayValue) variable;
            array.set((int)index.eval().asNumber(), expression.eval());
        } else {
            throw new RuntimeException("Array expected!");
        }
    }

    @Override
    public String toString() {
        return String.format("ArrayAssignmentStatement(%s[%s] = %s)", variable, index, expression);
    }
}
