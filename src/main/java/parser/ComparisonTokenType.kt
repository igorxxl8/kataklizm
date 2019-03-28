package parser

enum class ComparisonTokenType : IComparisonTokenType {
    EQEQ,
    EXCL,
    EXCLEQ,
    LT,
    LTEQ,
    GT,
    GTEQ;

    override fun toString(): String {
        return when (this) {
            EQEQ -> "=="
            EXCL -> "!"
            EXCLEQ -> "!="
            LT -> "<"
            LTEQ -> "<="
            GT -> ">"
            GTEQ -> ">="
        }
    }
}
