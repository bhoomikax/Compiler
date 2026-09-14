package ast.statements;

import ast.ASTNode;
import ast.Statement;

import java.util.List;

public class FunctionDeclaration extends ASTNode {

    private final String returnType;
    private final String name;
    private final List<Parameter> parameters;
    private final List<Statement> body;

    public FunctionDeclaration(
            String returnType,
            String name,
            List<Parameter> parameters,
            List<Statement> body
    ) {
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public List<Statement> getBody() {
        return body;
    }
}