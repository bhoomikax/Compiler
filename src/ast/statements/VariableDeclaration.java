package ast.statements;

import ast.Expression;
import ast.Statement;

public class VariableDeclaration extends Statement {

    private final String type;
    private final String name;
    private final Expression initializer;

    public VariableDeclaration(
            String type,
            String name,
            Expression initializer
    ) {
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }
}