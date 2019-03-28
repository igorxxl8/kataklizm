package parser.ast.expressions

import stdlib.ArrayValue
import stdlib.NumberValue
import stdlib.StringValue
import stdlib.Value

class BinaryExpression(private val operation: Char, private val expr1: Expression, private val expr2: Expression, private val _opPosStr: Int, private val _opPosLine: Int) : Expression {

    override fun eval(): Value {
        val value1 = expr1.eval()
        val value2 = expr2.eval()
        if (value1 !is StringValue && value1 !is ArrayValue) {
            val number1 = value1.asNumber()
            val number2 = value2.asNumber()
            return when (operation) {
                '-' -> NumberValue(number1 - number2)
                '*' -> NumberValue(number1 * number2)
                '/' -> NumberValue(number1 / number2)
                '+' -> NumberValue(number1 + number2)
                else -> NumberValue(number1 + number2)
            }
        }
        val string1 = value1.asString()
        return when (operation) {
                '*' -> {
                    val iterations = value2.asNumber().toInt()
                    val buffer = StringBuilder()
                    for (i in 0 until iterations) {
                        buffer.append(string1)
                    }
                    StringValue(buffer.toString())
                }
                '+' -> StringValue(string1 + value2.asString())
                else -> StringValue(string1 + value2.asString())
            }
    }

    override fun toString(): String {
        return String.format("<BinaryExpression>%s<OperationExpression operation=\"%c\" str=\"%d\" line=\"%d\"/>%s</BinaryExpression>", expr1, operation, _opPosStr, _opPosLine, expr2)

    }
}
