package stdlib

import java.util.HashMap
import java.util.Stack

object Variables {

    private var variables: MutableMap<String, Value>? = null
    private val stack: Stack<Map<String, Value>> = Stack()

    init {
        variables = HashMap()
        variables!!["PI"] = NumberValue(Math.PI)
        variables!!["E"] = NumberValue(Math.E)
    }

    fun push() {
        stack.push(HashMap(variables!!))
    }

    fun pop() {
        variables = stack.pop() as MutableMap<String, Value>?
    }

    fun isNotExists(name: String): Boolean {
        return !variables!!.containsKey(name)
    }

    operator fun get(name: String): Value? {
        return if (isNotExists(name)) {
            NumberValue.NULL
        } else variables!![name]

    }

    operator fun set(name: String, value: Value) {
        variables!![name] = value
    }

}
