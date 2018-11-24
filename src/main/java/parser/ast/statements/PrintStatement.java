package parser.ast.statements;


import parser.ast.expressions.Expression;

public final class PrintStatement implements Statement {

    private final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        System.out.print(expression.eval());
    }

    @Override
    public String toString() {
        return "PrintStatement{" +
                "expression=" + expression +
                '}';
    }
}
