package parser.ast.statements;

import parser.ast.expressions.Expression;
import stdlib.NumberValue;
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

        if (conditionResult.asNumber() == Integer.MAX_VALUE){
            for (var stat: caseStatements.getStatements()) {
                final var cstat = (CaseStatement) stat;
                if (cstat.getCaseExpression().eval().asString().equals(conditionResult.asString())) {
                    cstat.execute();
                    break;
                }
            }
        }else{
            for (var stat: caseStatements.getStatements()) {
                final var cstat = (CaseStatement) stat;
                if (cstat.getCaseExpression().eval().asNumber() == conditionResult.asNumber() ||
                        cstat.getCaseExpression().eval().asString().equals(conditionResult.asString())) {
                    cstat.execute();
                    break;
                }
            }
        }


    }
}
