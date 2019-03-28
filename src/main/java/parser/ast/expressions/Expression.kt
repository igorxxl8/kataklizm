package parser.ast.expressions

import stdlib.Value

interface Expression {
    fun eval(): Value
}
