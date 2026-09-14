import lexer.Lexer;
import lexer.Token;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String source = """
                func int main() {
                    int x = 10;
                    decimal price = 99.50;
                    truth active = yes;
                    string name = "Arc";

                    if (x >= 10 && active) {
                        output("Hello", name);
                    }

                    send x;
                }
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}