package parser.ast.statements;

import parser.ast.expressions.Expression;

public class ForStatement implements Statement {

    private final Statement init;
    private final Expression term;
    private final Statement inc;
    private final Statement stat;

    public ForStatement(Statement init, Expression term, Statement inc, Statement statement) {
        this.init = init;
        this.term = term;
        this.inc = inc;
        this.stat = statement;
    }

    @Override
    public void execute() {
        for (init.execute(); term.eval().asNumber() != 0; inc.execute()) {
            try {
                stat.execute();
            } catch (BreakStatement breakStatement){
                break;
            } catch (ContinueStatement continueStatement){
                //continue;
            }

        }
    }

    @Override
    public String toString() {
        return String.format("<ForStatement><Init>%s</Init><Condition>%s</Condition><Increment>%s</Increment>%s</ForStatement>", init, term, inc, stat);
    }
}
