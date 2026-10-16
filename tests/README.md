# Arc integration tests

Run `powershell -ExecutionPolicy Bypass -File .\tests\run-tests.ps1` from the
`Compiler` directory. Each positive test is passed through the Java compiler,
the x86-64 MinGW assembler/linker, and the resulting Windows executable. The
runner compares exact standard output. Syntax and semantic-error cases pass
only when compilation exits unsuccessfully and does not write assembly.

| Test | Coverage |
| --- | --- |
| `integer_test.arc` | Pre-existing integer arithmetic smoke test |
| `integer_arithmetic.arc` | Integer operations, remainder, grouping, precedence, associativity |
| `decimal_arithmetic.arc` | Decimal operations, remainder, mixed integer/decimal promotion |
| `strings.arc` | String literals, variables, assignment, and equality |
| `variables.arc` | Declarations, uninitialized declarations, assignments, block shadowing |
| `if_else.arc` | Both conditional branches |
| `while_loop.arc` | While condition and repeated body |
| `each_loop.arc` | Initialization, condition, update |
| `break_skip.arc` | Loop exit and `skip` update behavior |
| `functions.arc` | Arguments, returns, nested calls, decimal parameters/results |
| `input_output.arc` | Integer standard input and output |
| `char_truth.arc` | Character equality, truth values, logical operators |
| `invalid_syntax.arc` | Parser rejects malformed syntax |
| `invalid_semantic.arc` | Semantic analyzer rejects an incompatible assignment |
| `invalid_loop_control.arc` | Semantic analyzer rejects `break` outside a loop |
| `invalid_function_arity.arc` | Semantic analyzer rejects more than four function parameters |

## Unsupported or limited features

- Arrays are not implemented despite appearing in the design grammar.
- `input()` reads an integer only.
- Functions support at most four parameters/arguments; later Windows x64
  stack-passed arguments are not implemented.
- `output(char)` prints the character's integer code point; `truth` prints
  `1` or `0`.
- String and character escape sequences are not implemented; the language has no array/indexing support.
- Definite-return analysis is incomplete: a non-void function is not checked to return a value on every control-flow path.
