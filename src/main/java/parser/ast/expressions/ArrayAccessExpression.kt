package parser.ast.expressions

import stdlib.ArrayValue
import stdlib.Value
import stdlib.Variables

class ArrayAccessExpression(private val variable: String, private val indexes: List<Expression>, private val _posStr: Int, private val _posLine: Int) : Expression {


    val array: ArrayValue
        get() {
            var array = consumeArray(Variables[variable]!!)
            val last = indexes.size - 1
            for (i in 0 until last) {
                array = consumeArray(array.get(index(i)))
            }
            return array
        }

    fun lastIndex(): Int {
        return index(indexes.size - 1)
    }

    private fun index(index: Int): Int {
        return indexes[index].eval().asNumber().toInt()
    }

    private fun consumeArray(`var`: Value): ArrayValue {
        return `var` as? ArrayValue ?: throw RuntimeException("Array expected!")
    }

    override fun eval(): Value {
        return array.get(lastIndex())
    }

    override fun toString(): String {
        return "<ArrayAccessExpression name=\"$variable\" line=\"$_posLine\" str=\"$_posStr\"><Indexes>$indexes</Indexes></ArrayAccessExpression>"
    }
}
