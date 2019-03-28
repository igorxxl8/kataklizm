package parser

enum class ArithmeticTokenType : IArithmeticTokenType {
    PLUS,
    MINUS,
    STAR,
    SLASH;

    override fun toString(): String {
        return when (this) {
            PLUS -> "+"
            MINUS -> "-"
            STAR -> "*"
            SLASH -> "/"
        }
    }
}
