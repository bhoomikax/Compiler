package ast.statements;

import ast.Expression;
import ast.Statement;

import java.util.List;

public class WhileStatement extends Statement {

    private final Expression condition;
    private final List<Statement> body;

    public WhileStatement(
            Expression condition,
            List<Statement> body
    ) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getBody() {
        return body;
    }
}