package stdlib

interface Function {
    fun execute(vararg args: Value): Value
}
