package parser.ast.expressions;

import stdlib.LinkValue;
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
        String type = null;
        if (value instanceof StringValue){
            type = "String";
        }
        else if (value instanceof NumberValue){
            type = "Number";
        }
        else if (value instanceof LinkValue){
            type = "Link";
        }
        return String.format("<ValueExpression type=\"%s\" value=\"%s\"/>", type, value.asString().replace("<", "&lt;").replace(">", "&gt;").replace("&&", "and").replace("&", "log_and"));
    }
}
