package stdlib

class StringValue(private val value: String) : Value {

    override fun asNumber(): Double {

        return try {
            java.lang.Double.parseDouble(value)
        } catch (e: NumberFormatException) {
            Integer.MAX_VALUE.toDouble()
        }

    }

    override fun asString(): String {
        return value
    }

    override fun asLink(): Any {
        return asString()
    }

    override fun toString(): String {
        return asString()
    }
}
