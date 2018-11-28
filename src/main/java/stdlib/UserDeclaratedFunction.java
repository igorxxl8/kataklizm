package stdlib;

import parser.ast.statements.ReturnStatement;
import parser.ast.statements.Statement;

import java.util.List;

public class UserDeclaratedFunction implements Function {

    private final List<String> argNames;
    private final Statement instructions;

    public UserDeclaratedFunction(List<String> argNames, Statement instructions) {
        this.argNames = argNames;
        this.instructions = instructions;
    }

    public int getArgsCount() {
        return argNames.size();
    }

    public String getArgsName(int index) {
        if (index < 0 || index >= getArgsCount()) {
            return "";
        }
        return argNames.get(index);
    }

    @Override
    public Value execute(Value... args) {
        try {
            instructions.execute();
            return NumberValue.ZERO;

        } catch (ReturnStatement returnStatement) {
            return returnStatement.getResult();
        }
    }
}
