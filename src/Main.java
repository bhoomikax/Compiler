import ast.Program;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import semantic.SemanticAnalyzer;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String source = """
                func int add(int a, int b) {
                    send a + b;
                }

                func int main() {

                    int x = 10;
                    int y = 20;

                    int result = add(x , y);

                    output(result);

                    send result;
                }
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        Program program = parser.parse();

        SemanticAnalyzer analyzer =
                new SemanticAnalyzer();

        analyzer.analyze(program);

        System.out.println(
                "Semantic analysis completed."
        );
    }
}