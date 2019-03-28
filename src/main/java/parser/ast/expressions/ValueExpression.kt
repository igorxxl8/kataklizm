package parser.ast.expressions

import stdlib.LinkValue
import stdlib.NumberValue
import stdlib.StringValue
import stdlib.Value

class ValueExpression : Expression {

    private val value: Value

    constructor(value: Double) {
        this.value = NumberValue(value)
    }

    constructor(value: String) {
        this.value = StringValue(value)
    }

    override fun eval(): Value {
        return value
    }

    override fun toString(): String {
        var type: String? = null
        when (value) {
            is StringValue -> type = "String"
            is NumberValue -> type = "Number"
            is LinkValue -> type = "Link"
        }
        return String.format("<ValueExpression type=\"%s\" value=\"%s\"/>", type, value.asString().replace("<", "&lt;").replace(">", "&gt;").replace("&&", "and").replace("&", "log_and"))
    }
}
