package parser.ast.statements;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement {

    private final List<Statement> statements;

    public BlockStatement() {
        this.statements = new ArrayList<>();
    }

    public void add(Statement statement){
        statements.add(statement);
    }

    @Override
    public void execute() {
        for (var statement : statements){
            statement.execute();
        }
    }

    @Override
    public String toString() {
        final var result = new StringBuilder();

        for (var statement: statements){
            result.append(statement.toString()).append(System.lineSeparator());
        }

        return result.toString();
    }
}
