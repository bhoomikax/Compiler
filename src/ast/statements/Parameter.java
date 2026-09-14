package ast.statements;

import ast.ASTNode;

public class Parameter extends ASTNode {

    private final String type;
    private final String name;

    public Parameter(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}