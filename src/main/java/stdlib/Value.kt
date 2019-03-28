package stdlib

interface Value {
    fun asNumber(): Double

    fun asString(): String

    fun asLink(): Any
}
