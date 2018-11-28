package parser.ast.expressions;

import stdlib.ArrayValue;
import stdlib.NumberValue;
import stdlib.StringValue;
import stdlib.Value;

public final class BinaryExpression implements Expression {

    private Expression expr1, expr2;
    private char operation;

    public BinaryExpression(char operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        final var value1 = expr1.eval();
        final var value2 = expr2.eval();
        if ((value1 instanceof StringValue) || (value1 instanceof ArrayValue)){
            final var string1 = value1.asString();
            switch (operation){
                case '*' : {
                    final var iterations = (int) value2.asNumber();
                    final var buffer = new StringBuilder();
                    for (var i = 0; i < iterations; i++){
                        buffer.append(string1);
                    }
                    return new StringValue(buffer.toString());
                }
                case '+' :
                default:
                    return new StringValue(string1 + value2.asString());
            }
        }

        final var number1 = value1.asNumber();
        final var number2 = value2.asNumber();
        switch (operation){
            case '-' : return new NumberValue(number1 - number2);
            case '*' : return new NumberValue(number1 * number2);
            case '/' : return new NumberValue(number1 / number2);
            case '+' :
            default: return new NumberValue(number1 + number2);
        }
    }

    @Override
    public String toString() {
        return String.format("BinaryExpression(%s %c %s)", expr1, operation, expr2);

    }
}
