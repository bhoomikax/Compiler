import ast.Program;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String source = """
                func int main() {
                    int x = 10;
                    decimal price = 99.50;
                    truth active = yes;

                    x = x + 5;

                    send x;
                }
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        Program program = parser.parse();

        System.out.println(
                "Parsed functions: "
                        + program.getFunctions().size()
        );
    }
}