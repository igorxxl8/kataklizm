package parser.ast.expressions;

import stdlib.NumberValue;
import stdlib.StringValue;
import stdlib.Value;

public final class ConditionalExpression implements Expression {

    public enum Operator {

        EQUALS("=="),
        NOT_EQUALS("!="),

        LT("<"),
        LTEQ("<="),

        GT(">"),
        GTEQ(">="),

        AND("&&"),
        OR("||");

        private final String name;

        Operator(String name) {
            this.name = name;
        }

        public String getName() {
            if (name.equals("<")){
                return "&lt;";
            }
            if (name.equals(">")){
                return "&gt;";
            }
            if (name.equals("<=")){
                return "&lt;=";
            }
            if (name.equals(">=")){
                return "&gt;=";
            }
            if (name.equals("&&")){
                return "and";
            }
            return name;
        }

        @Override
        public String toString() {
            return String.format("<Operator operator=\"%s\"/>", getName());
        }
    }

    private Expression expr1, expr2;
    private Operator operation;

    public ConditionalExpression(Operator operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        final var value1 = expr1.eval();
        final var value2 = expr2.eval();

        double number1, number2;
        if (value1 instanceof StringValue) {
            number1 = value1.asString().compareTo(value2.asString());
            number2 = 0;
        } else {
            number1 = value1.asNumber();
            number2 = value2.asNumber();
        }


        boolean result;
        switch (operation) {
            case LT: result = number1 < number2; break;
            case LTEQ: result = number1 <= number2; break;
            case GT: result = number1 > number2; break;
            case GTEQ: result = number1 >= number2; break;
            case NOT_EQUALS: result = number1 != number2; break;
            case AND: result = (number1 != 0) && (number2 != 0); break;
            case OR: result = (number1 != 0) || (number2 != 0); break;
            case EQUALS:
            default:
                result = number1 == number2; break;
        }
        return new NumberValue(result);
    }

    @Override
    public String toString() {
        return String.format("<ConditionalExpression>%s%s%s</ConditionalExpression>", expr1, operation.toString(), expr2);

    }
}
