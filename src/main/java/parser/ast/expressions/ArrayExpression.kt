package parser.ast.expressions

import stdlib.ArrayValue
import stdlib.Value

class ArrayExpression(private val values: List<Expression>) : Expression {

    override fun eval(): Value {
        val size = values.size
        val array = ArrayValue(size)
        for (i in 0 until size) {
            array.set(i, values[i].eval())
        }

        return array
    }

    override fun toString(): String {
        return "<ArrayExpression><Items>$values</Items></ArrayExpression>"
    }
}
