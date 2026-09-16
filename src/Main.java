import ast.Program;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String source = """
                func int add(int a, int b) {
                    send a + b;
                }

                func int main() {

                    int x = input();
                    int y = 20;

                    if (x > y) {
                        output("x is greater");
                    } else if (x == y) {
                        output("x equals y");
                    } else {
                        output("y is greater");
                    }

                    loop (x < 100) {
                        x = x + 1;

                        if (x == 50) {
                            skip;
                        }

                        if (x == 80) {
                            break;
                        }
                    }

                    each (int i = 0; i < 5; i = i + 1) {
                        output(i);
                    }

                    int result = add(x, y);

                    send result;
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

        for (var function : program.getFunctions()) {
            System.out.println(
                    "Function: "
                            + function.getName()
            );
        }
    }
}