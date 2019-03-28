package parser.ast.statements;

import stdlib.StringValue;
import stdlib.Variables;

import java.util.Scanner;

public class InputStatement implements Statement {
    private final String variable;

    public InputStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public void execute() {
        final var input = new Scanner(System.in).nextLine();
        Variables.INSTANCE.set(variable, new StringValue(input));
    }

    @Override
    public String toString() {
        return String.format("<InputStatement var=\"%s\"/>", variable);
    }
}
