package lexer;

public enum TokenType {
    FUNC,
    SEND,
    IF,
    ELSE,
    LOOP,
    EACH,
    BREAK,
    SKIP,

    OUTPUT,
    INPUT,

    INT,
    DECIMAL,
    TRUTH,
    CHAR,
    STRING,
    VOID,

    YES,
    NO,

    INTEGER_LITERAL,
    DECIMAL_LITERAL,
    CHAR_LITERAL,
    STRING_LITERAL,

    IDENTIFIER,

    PLUS,
    MINUS,
    STAR,
    SLASH,
    PERCENT,

    ASSIGN,

    EQUAL_EQUAL,
    NOT_EQUAL,
    LESS,
    GREATER,
    LESS_EQUAL,
    GREATER_EQUAL,

    AND_AND,
    OR_OR,
    NOT,

    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_BRACKET,
    RIGHT_BRACKET,

    COMMA,
    SEMICOLON,

    EOF
}