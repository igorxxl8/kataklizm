package parser;

public enum TokenType implements ITokenType {

    NUMBER,

    WORD,
    TEXT,
    ARROW,

    EQ,


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
