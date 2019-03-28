package parser.ast.expressions

import stdlib.NumberValue
import stdlib.StringValue
import stdlib.Value

class ConditionalExpression(private val operation: Operator, private val expr1: Expression, private val expr2: Expression) : Expression {

    enum class Operator private constructor(private val namee: String) {

        EQUALS("=="),
        NOT_EQUALS("!="),

        LT("<"),
        LTEQ("<="),

        GT(">"),
        GTEQ(">="),

        AND("&&"),
        OR("||");

        fun getNamee(): String {
            return when (namee) {
                "<" -> "&lt;"
                ">" -> "&gt;"
                "<=" -> "&lt;="
                ">=" -> "&gt;="
                "&&" -> "and"
                else -> namee
            }
        }

        override fun toString(): String {
            return String.format("<Operator operator=\"%s\"/>", getNamee())
        }
    }

    override fun eval(): Value {
        val value1 = expr1.eval()
        val value2 = expr2.eval()

        val number1: Double
        val number2: Double
        if (value1 is StringValue) {
            number1 = value1.asString().compareTo(value2.asString()).toDouble()
            number2 = 0.0
        } else {
            number1 = value1.asNumber()
            number2 = value2.asNumber()
        }


        val result: Boolean
        result = when (operation) {
            ConditionalExpression.Operator.LT -> number1 < number2
            ConditionalExpression.Operator.LTEQ -> number1 <= number2
            ConditionalExpression.Operator.GT -> number1 > number2
            ConditionalExpression.Operator.GTEQ -> number1 >= number2
            ConditionalExpression.Operator.NOT_EQUALS -> number1 != number2
            ConditionalExpression.Operator.AND -> number1 != 0.0 && number2 != 0.0
            ConditionalExpression.Operator.OR -> number1 != 0.0 || number2 != 0.0
            ConditionalExpression.Operator.EQUALS -> number1 == number2
        }
        return NumberValue(result)
    }

    override fun toString(): String {
        return String.format("<ConditionalExpression>%s%s%s</ConditionalExpression>", expr1, operation.toString(), expr2)

    }
}
