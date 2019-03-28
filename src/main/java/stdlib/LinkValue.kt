package stdlib

class LinkValue internal constructor(private val value: Any) : Value {

    override fun asNumber(): Double {
        return 0.0
    }

    override fun asString(): String {
        return value.toString()
    }

    override fun asLink(): Any {
        return value
    }

    override fun toString(): String {
        return asString()
    }
}
