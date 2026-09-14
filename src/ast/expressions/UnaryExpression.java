package ast.expressions;

import ast.Expression;
import lexer.TokenType;

public class UnaryExpression extends Expression {

    private final TokenType operator;
    private final Expression expression;

    public UnaryExpression(
            TokenType operator,
            Expression expression
    ) {
        this.operator = operator;
        this.expression = expression;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }
}