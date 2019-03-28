package parser

enum class KeywordTokenType : IKeywordTokenType {
    OUT,
    IN,
    IF,
    ELSE,
    LOOP,
    FOR,
    BREAK,
    CONTINUE,
    MATCH,
    CASE,

    FUNC,
    RET;

    val namee: String
        get() = super.toString().toLowerCase()

    override fun toString(): String {
        val s = super.toString().toLowerCase()
        var description = "key word \"$s\""
        when (s) {
            "if" -> description += " is the condition statement"
            "ret" -> description += " is the return statement"
            "loop" -> description += " is the while statement"
            "for" -> description += " is the loop for statement"
            "match" -> description += " is the switch statement"
            "else" -> description += " is the condition statement"

            "break", "continue" -> description += " is the loop life cycle statement"

            "case" -> description += " is the case statement"

            "func" -> description += " is the function implementation"

            "in", "out" -> description += " is the input output stream"
        }


        var separator = "\t"
        if (s.length < 4) {
            separator = "\t\t"
        }
        return String.format("%s%s|%s", s, separator, description)
    }
}
