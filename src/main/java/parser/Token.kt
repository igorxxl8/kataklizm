package parser

/**
 * @author Igor Turcevich
 */
class Token internal constructor(val type: ITokenType, val text: String, posstr: Int, posfile: Int) {

    var posstr: Int = 0
        internal set
    var posfile: Int = 0
        internal set

    init {
        this.posstr = posstr
        this.posfile = posfile
    }

    override fun toString(): String {
        return if (!text.isEmpty()) "Token($type, \"$text\")" else "Token($type)"

    }
}
