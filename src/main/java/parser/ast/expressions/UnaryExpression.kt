package parser.ast.expressions

import stdlib.NumberValue
import stdlib.Value

class UnaryExpression(private val operation: Char, private val expr: Expression) : Expression {

    override fun eval(): Value {
        return when (operation) {
            '-' -> NumberValue(-expr.eval().asNumber())
            '+' -> NumberValue(expr.eval().asNumber())
            else -> NumberValue(expr.eval().asNumber())
        }
    }

    override fun toString(): String {
        return String.format("<UnaryExpression operation=\"%c\">%s</UnaryExpression>)", operation, expr)

    }
}
