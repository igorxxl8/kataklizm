package parser.ast.expressions;

import stdlib.Function;
import stdlib.Functions;
import stdlib.Value;

import java.util.ArrayList;
import java.util.List;

public class FunctionalExpression implements Expression{

    private final String name;
    private final List<Expression> args;

    public FunctionalExpression(String name) {
        this.name = name;
        this.args = new ArrayList<>();
    }

    public FunctionalExpression(String name, List<Expression> args) {
        this.name = name;
        this.args = args;
    }

    public void addArgument(Expression arg) {
        args.add(arg);
    }

    @Override
    public Value eval() {
        final int size = args.size();
        final var values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = args.get(i).eval();
        }

        return Functions.get(name).execute(values);
    }

    @Override
    public String toString() {
        return String.format("FunctionalExpression(%s)",args.toString());
    }
}
