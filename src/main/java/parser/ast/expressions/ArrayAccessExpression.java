package parser.ast.expressions;

import stdlib.ArrayValue;
import stdlib.Value;
import stdlib.Variables;

import java.util.List;

public class ArrayAccessExpression implements Expression {

    private final String variable;
    private final List<Expression> indexes;

    public ArrayAccessExpression(String var, List<Expression> indexes) {
        this.variable = var;
        this.indexes = indexes;
    }


    public ArrayValue getArray() {
        var array = consumeArray(Variables.get(variable));
        final var last = indexes.size() - 1;
        for (var i = 0; i < last; i++) {
            array = consumeArray(array.get(index(i)));
        }
        return array;
    }

    public int lastIndex(){
        return index(indexes.size() - 1);
    }

    private int index(int index){
        return (int) indexes.get(index).eval().asNumber();
    }

    private ArrayValue consumeArray(Value var) {
        if (var instanceof ArrayValue) {
            return (ArrayValue) var;
        } else {
            throw new RuntimeException("Array expected!");
        }
    }

    @Override
    public Value eval() {
        return getArray().get(lastIndex());
    }

    @Override
    public String toString() {

        return String.format("<ArrayAccessExpression name=\"%s\"><Indexes>%s</Indexes></ArrayAccessExpression>", variable, indexes);
    }
}
