package stdlib

import parser.ast.statements.ReturnStatement
import parser.ast.statements.Statement

class UserDeclaratedFunction(private val argNames: List<String>, private val instructions: Statement) : Function {

    val argsCount: Int
        get() = argNames.size

    fun getArgsName(index: Int): String {
        return if (index < 0 || index >= argsCount) {
            ""
        } else argNames[index]
    }

    override fun execute(vararg args: Value): Value {
        return try {
            instructions.execute()
            NumberValue.NULL

        } catch (returnStatement: ReturnStatement) {
            returnStatement.result
        }

    }
}
