package parser;

import ast.Program;
import ast.Statement;
import ast.Expression;
import ast.expressions.BinaryExpression;
import ast.expressions.LiteralExpression;
import ast.expressions.UnaryExpression;
import ast.expressions.VariableExpression;
import ast.statements.Assignment;
import ast.statements.ExpressionStatement;
import ast.statements.FunctionDeclaration;
import ast.statements.Parameter;
import ast.statements.ReturnStatement;
import ast.statements.VariableDeclaration;
import lexer.Token;
import lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Program parse() {
        List<FunctionDeclaration> functions = new ArrayList<>();

        while (!isAtEnd()) {
            functions.add(functionDeclaration());
        }

        return new Program(functions);
    }

    private FunctionDeclaration functionDeclaration() {
        consume(TokenType.FUNC, "Expected 'func'.");

        String returnType = typeName();
        String name = consume(
                TokenType.IDENTIFIER,
                "Expected function name."
        ).getLexeme();

        consume(TokenType.LEFT_PAREN, "Expected '('.");

        List<Parameter> parameters = new ArrayList<>();

        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                String type = typeName();

                String parameterName = consume(
                        TokenType.IDENTIFIER,
                        "Expected parameter name."
                ).getLexeme();

                parameters.add(new Parameter(type, parameterName));

            } while (match(TokenType.COMMA));
        }

        consume(TokenType.RIGHT_PAREN, "Expected ')'.");

        consume(TokenType.LEFT_BRACE, "Expected '{'.");

        List<Statement> body = new ArrayList<>();

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            body.add(statement());
        }

        consume(TokenType.RIGHT_BRACE, "Expected '}'.");

        return new FunctionDeclaration(
                returnType,
                name,
                parameters,
                body
        );
    }

    private Statement statement() {

        if (match(TokenType.SEND)) {
            return returnStatement();
        }

        if (isType(peek().getType())) {
            return variableDeclaration();
        }

        if (check(TokenType.IDENTIFIER) && checkNext(TokenType.ASSIGN)) {
            return assignment();
        }

        return expressionStatement();
    }

    private Statement returnStatement() {
        Expression value = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after return value."
        );

        return new ReturnStatement(value);
    }

    private Statement variableDeclaration() {
        String type = typeName();

        String name = consume(
                TokenType.IDENTIFIER,
                "Expected variable name."
        ).getLexeme();

        consume(TokenType.ASSIGN, "Expected '='.");

        Expression initializer = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after variable declaration."
        );

        return new VariableDeclaration(
                type,
                name,
                initializer
        );
    }

    private Statement assignment() {
        String name = advance().getLexeme();

        consume(TokenType.ASSIGN, "Expected '='.");

        Expression value = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after assignment."
        );

        return new Assignment(name, value);
    }

    private Statement expressionStatement() {
        Expression expression = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after expression."
        );

        return new ExpressionStatement(expression);
    }

    private Expression expression() {
        return equality();
    }

    private Expression equality() {
        Expression expression = comparison();

        while (match(
                TokenType.EQUAL_EQUAL,
                TokenType.NOT_EQUAL
        )) {
            TokenType operator = previous().getType();
            Expression right = comparison();

            expression = new BinaryExpression(
                    expression,
                    operator,
                    right
            );
        }

        return expression;
    }

    private Expression comparison() {
        Expression expression = term();

        while (match(
                TokenType.LESS,
                TokenType.LESS_EQUAL,
                TokenType.GREATER,
                TokenType.GREATER_EQUAL
        )) {
            TokenType operator = previous().getType();
            Expression right = term();

            expression = new BinaryExpression(
                    expression,
                    operator,
                    right
            );
        }

        return expression;
    }

    private Expression term() {
        Expression expression = factor();

        while (match(
                TokenType.PLUS,
                TokenType.MINUS
        )) {
            TokenType operator = previous().getType();
            Expression right = factor();

            expression = new BinaryExpression(
                    expression,
                    operator,
                    right
            );
        }

        return expression;
    }

    private Expression factor() {
        Expression expression = unary();

        while (match(
                TokenType.STAR,
                TokenType.SLASH,
                TokenType.PERCENT
        )) {
            TokenType operator = previous().getType();
            Expression right = unary();

            expression = new BinaryExpression(
                    expression,
                    operator,
                    right
            );
        }

        return expression;
    }

    private Expression unary() {
        if (match(
                TokenType.NOT,
                TokenType.MINUS
        )) {
            TokenType operator = previous().getType();
            Expression right = unary();

            return new UnaryExpression(
                    operator,
                    right
            );
        }

        return primary();
    }

    private Expression primary() {

        if (match(TokenType.INTEGER_LITERAL)) {
            return new LiteralExpression(
                    Integer.parseInt(previous().getLexeme())
            );
        }

        if (match(TokenType.DECIMAL_LITERAL)) {
            return new LiteralExpression(
                    Double.parseDouble(previous().getLexeme())
            );
        }

        if (match(TokenType.STRING_LITERAL)) {
            String value = previous().getLexeme();

            return new LiteralExpression(
                    value.substring(1, value.length() - 1)
            );
        }

        if (match(TokenType.CHAR_LITERAL)) {
            String value = previous().getLexeme();

            return new LiteralExpression(
                    value.charAt(1)
            );
        }

        if (match(TokenType.YES)) {
            return new LiteralExpression(true);
        }

        if (match(TokenType.NO)) {
            return new LiteralExpression(false);
        }

        if (match(TokenType.IDENTIFIER)) {
            return new VariableExpression(
                    previous().getLexeme()
            );
        }

        if (match(TokenType.LEFT_PAREN)) {
            Expression expression = expression();

            consume(
                    TokenType.RIGHT_PAREN,
                    "Expected ')'."
            );

            return expression;
        }

        throw error(peek(), "Expected expression.");
    }

    private String typeName() {
        if (match(
                TokenType.INT,
                TokenType.DECIMAL,
                TokenType.TRUTH,
                TokenType.CHAR,
                TokenType.STRING,
                TokenType.VOID
        )) {
            return previous().getLexeme();
        }

        throw error(peek(), "Expected type.");
    }

    private boolean isType(TokenType type) {
        return type == TokenType.INT
                || type == TokenType.DECIMAL
                || type == TokenType.TRUTH
                || type == TokenType.CHAR
                || type == TokenType.STRING;
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return type == TokenType.EOF;
        }

        return peek().getType() == type;
    }

    private boolean checkNext(TokenType type) {
        if (current + 1 >= tokens.size()) {
            return false;
        }

        return tokens.get(current + 1).getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private RuntimeException error(Token token, String message) {
        return new RuntimeException(
                "Parser error at "
                        + token.getLine()
                        + ":"
                        + token.getColumn()
                        + ": "
                        + message
        );
    }
}