package parser.ast.statements;

import parser.ast.expressions.Expression;
import stdlib.Variables;

public final class AssignmentStatement implements Statement{

    private final String variable;
    private final Expression expression;

    public AssignmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute() {
        final var result = expression.eval();
        Variables.INSTANCE.set(variable, result);
    }

    @Override
    public String toString() {
        return String.format("<AssignmentStatement var=\"%s\">%s</AssignmentStatement>", variable, expression);
    }
}
