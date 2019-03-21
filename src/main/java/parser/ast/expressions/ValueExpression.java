package parser.ast.expressions;

import stdlib.NumberValue;
import stdlib.StringValue;
import stdlib.Value;

public class ValueExpression implements Expression {

    private final Value value;

    public ValueExpression(double value) {
        this.value = new NumberValue(value);
    }

    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }

    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("<ValueExpression value=\"%s\"/>", value.asString().replace("<", "&lt;").replace(">", "&gt;").replace("&&", "and").replace("&", "log_and"));
    }
}
