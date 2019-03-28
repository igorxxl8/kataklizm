package parser.ast.expressions

import stdlib.Value
import stdlib.Variables

class VariableExpression(private val name: String) : Expression {

    override fun eval(): Value {
        if (Variables.isNotExists(name)) {
            throw RuntimeException("Constant does not exists!")
        }

        return Variables[name]!!
    }

    override fun toString(): String {
        return String.format("<VariableExpression name=\"%s\"/>", name)
    }
}
