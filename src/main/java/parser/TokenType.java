package parser;

public enum TokenType {

    NUMBER,

    WORD,
    TEXT,

    PRINT,
    IF,
    ELSE,
    LOOP,
    FOR,
    BREAK,
    CONTINUE,
    MATCH,
    CASE,
    ARROW,

    FUNCTION,
    RETURN,

    PLUS,
    MINUS,
    STAR,
    SLASH,
    EQ,
    EQEQ,
    EXCL,
    EXCLEQ,
    LT,
    LTEQ,
    GT,
    GTEQ,

    BAR,
    BARBAR,
    AMP,
    AMPAMP,

    LBRACE,
    RBRACE,

    LBRACKET,
    RBRACKET,

    LPAREN,
    RPAREN,

    SEMI_COLON,
    COMMA,

    EOF
}
