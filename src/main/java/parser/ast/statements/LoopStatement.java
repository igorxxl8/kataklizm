package parser.ast.statements;

import parser.ast.expressions.Expression;

public class LoopStatement implements Statement {

    private final Expression condition;
    private final Statement statement;

    public LoopStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void execute() {
        while(condition.eval().asNumber() != 0) {
            statement.execute();
        }
    }

    @Override
    public String toString() {
        return String.format("LoopStatement(%s) {%s}", condition, statement);
    }
}
