import ast.Program;
import codegen.CodeGenerator;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import semantic.SemanticAnalyzer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    private static final String DEFAULT_SOURCE = """
            func void main() {
                int a = 10;
                int b = 5;
                int sum = a + b;
                int difference = a - b;
                int product = a * b;
                int quotient = a / b;
                output(sum);
                output(difference);
                output(product);
                output(quotient);
            }
            """;

    public static void main(String[] args) {
        try {
            compile(args);
        } catch (Exception exception) {
            System.err.println("Compilation failed: " + exception.getMessage());
            System.exit(1);
        }
    }

    private static void compile(String[] args) throws Exception {
        if (args.length > 2) {
            throw new IllegalArgumentException(
                    "Usage: java -cp out Main [source.arc] [output.s]"
            );
        }

        String source = args.length == 0
                ? DEFAULT_SOURCE
                : Files.readString(Path.of(args[0]));
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        Program program = parser.parse();

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        semanticAnalyzer.analyze(program);
        if (semanticAnalyzer.hasErrors()) {
            throw new IllegalArgumentException("Semantic analysis failed.");
        }

        CodeGenerator codeGenerator = new CodeGenerator();
        String assembly = codeGenerator.generate(program);

        Path outputPath = args.length == 2
                ? Path.of(args[1])
                : Path.of("out", "program.s");
        Path outputDirectory = outputPath.toAbsolutePath().getParent();
        if (outputDirectory != null) {
            Files.createDirectories(outputDirectory);
        }

        Files.writeString(outputPath, assembly);

        System.out.println("Compilation successful.");
        System.out.println("Assembly written to " + outputPath);
    }
}