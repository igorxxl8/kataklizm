package parser.ast.statements;

public class ErrorStatement implements Statement {
    @Override
    public void execute() {

    }

    @Override
    public String toString() {
        return "<ErrorStatement/>";
    }
}
