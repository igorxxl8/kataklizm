package parser.ast.expressions

import stdlib.NumberValue
import stdlib.Value

class ErrorExpression : Expression {
    override fun eval(): Value {
        return NumberValue(0.0)
    }
}
