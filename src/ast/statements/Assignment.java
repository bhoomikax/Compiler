package ast.statements;

import ast.Expression;
import ast.Statement;

public class Assignment extends Statement {

    private final String name;
    private final Expression value;

    public Assignment(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }
}