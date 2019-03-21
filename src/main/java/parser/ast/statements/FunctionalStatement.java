package parser.ast.statements;

import parser.ast.expressions.Expression;
import parser.ast.expressions.FunctionalExpression;

public class FunctionalStatement implements Statement {

    private final FunctionalExpression function;

    public FunctionalStatement(FunctionalExpression function) {
        this.function = function;
    }

    @Override
    public void execute() {
        function.eval();
    }

    @Override
    public String toString() {
        return String.format("<FunctionalStatement>%s</FunctionalStatement>", function.toString());
    }
}
