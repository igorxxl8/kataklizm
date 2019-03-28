package parser.ast.expressions;

import stdlib.Functions;
import stdlib.UserDeclaratedFunction;
import stdlib.Value;
import stdlib.Variables;

import java.util.List;

public class FunctionalExpression implements Expression{

    private final String name;
    private final List<Expression> args;

    public FunctionalExpression(String name) {
        this.name = name;
        this.args = new CustomArrayList<>();
    }

    public void addArgument(Expression arg) {
        args.add(arg);
    }

    @Override
    public Value eval() {
        final var size = args.size();
        final var values = new Value[size];
        for (var i = 0; i < size; i++) {
            values[i] = args.get(i).eval();
        }
        final var function = Functions.get(name);
        if (function instanceof UserDeclaratedFunction) {
            final var userFunction = (UserDeclaratedFunction) function;
            if (size != userFunction.getArgsCount()) {
                throw new RuntimeException("Args count mismatch!");
            }

            Variables.INSTANCE.push();
            for (var i = 0; i < size; i++) {
                Variables.INSTANCE.set(userFunction.getArgsName(i), values[i]);
            }
            final var result = userFunction.execute(values);
            Variables.INSTANCE.pop();
            return result;
        }
        return function.execute(values);
    }

    @Override
    public String toString() {
        return String.format("<FunctionalExpression name=\"%s\"><Args>%s</Args></FunctionalExpression>",name, args.toString());
    }
}
