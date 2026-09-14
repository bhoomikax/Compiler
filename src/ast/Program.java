package ast;

import java.util.List;
import ast.statements.FunctionDeclaration;

public class Program extends ASTNode {

    private final List<FunctionDeclaration> functions;

    public Program(List<FunctionDeclaration> functions) {
        this.functions = functions;
    }

    public List<FunctionDeclaration> getFunctions() {
        return functions;
    }
}