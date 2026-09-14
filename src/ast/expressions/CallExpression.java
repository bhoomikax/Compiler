package ast.expressions;

import ast.Expression;

import java.util.List;

public class CallExpression extends Expression {

    private final String name;
    private final List<Expression> arguments;

    public CallExpression(
            String name,
            List<Expression> arguments
    ) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArguments() {
        return arguments;
    }
}