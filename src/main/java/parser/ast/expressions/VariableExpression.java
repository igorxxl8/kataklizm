package parser.ast.expressions;

import stdlib.Value;
import stdlib.Variables;

public final class VariableExpression implements Expression {

    private final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (Variables.isNotExists(name)){
            throw new RuntimeException("Constant does not exists!");
        }

        return Variables.get(name);
    }

    @Override
    public String toString() {
        return String.format("<VariableExpression name=\"%s\"/>", name);
    }
}
