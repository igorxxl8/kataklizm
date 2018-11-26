package parser.ast.expressions;

import stdlib.*;

import java.util.List;

public class ArrayExpression implements Expression{

    private final List<Expression> values;

    public ArrayExpression(List<Expression> values) {
        this.values = values;
    }

    @Override
    public Value eval() {
        final int size = values.size();
        final ArrayValue array = new ArrayValue(size);
        for (int i = 0; i < size; i++) {
            array.set(i, values.get(i).eval());
        }

        return array;
    }

    @Override
    public String toString() {
        return String.format("ArrayExpression(%s)", values.toString());
    }
}
