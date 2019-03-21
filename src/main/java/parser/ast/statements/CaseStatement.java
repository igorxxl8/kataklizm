package parser.ast.statements;

import parser.ast.expressions.Expression;

public class CaseStatement implements Statement {

    Expression getCaseExpression() {
        return caseExpression;
    }

    private final Expression caseExpression;
    private final Statement body;

    public CaseStatement(Expression caseExpression, Statement body) {
        this.caseExpression = caseExpression;
        this.body = body;
    }

    @Override
    public String toString() {
        return "<CaseStatement>" + caseExpression  + body + "</CaseStatement>";
    }

    @Override
    public void execute() {
        body.execute();
    }
}
