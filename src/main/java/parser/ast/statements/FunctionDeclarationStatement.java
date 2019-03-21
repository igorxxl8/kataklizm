package parser.ast.statements;

import stdlib.Functions;
import stdlib.UserDeclaratedFunction;

import java.util.List;

public class FunctionDeclarationStatement implements Statement {

    private final String name;
    private final List<String> argNames;
    private final Statement instructions;

    public FunctionDeclarationStatement(String name, List<String> argNames, Statement instructions) {
        this.name = name;
        this.argNames = argNames;
        this.instructions = instructions;
    }


    @Override
    public void execute() {
        Functions.set(name, new UserDeclaratedFunction(argNames, instructions));
    }

    @Override
    public String toString() {
        return String.format("<FunctionDeclarationStatement name=\"%s\"><Args>%s</Args><Instructions>%s</Instructions></FunctionDeclarationStatement>", name, argNames.toString(), instructions.toString());
    }
}
