package parser.ast.statements;

import parser.ast.expressions.Expression;
import stdlib.StringValue;

public class MatchStatement implements Statement {

    private final Expression condition;
    private final Statement matchBody;

    public MatchStatement(Expression condition, Statement matchBody) {
        this.condition = condition;
        this.matchBody = matchBody;
    }

    @Override
    public void execute() {
        final var caseStatements = (BlockStatement) matchBody;
        final var conditionResult = condition.eval();
        if (conditionResult instanceof StringValue){
            for (var stat: caseStatements.getStatements()) {
                final var cstat = (CaseStatement) stat;
                if (cstat.getCaseExpression().eval().asString() == conditionResult.asString()) {
                    cstat.execute();
                    break;
                }
            }
        }

        for (var stat: caseStatements.getStatements()) {
            final var cstat = (CaseStatement) stat;
            if (cstat.getCaseExpression().eval().asNumber() == conditionResult.asNumber()) {
                cstat.execute();
                break;
            }
        }
    }
}
