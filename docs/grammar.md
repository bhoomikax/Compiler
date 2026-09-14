# Arc Grammar

**Language:** Arc
**Compiler:** CipherCompiler
**Version:** 1.0

This document defines the formal grammar of the Arc programming language.

The grammar is written in a simplified BNF-style notation.

---

# 1. Notation

The following notation is used:

```text
::=     means "is defined as"
|       means "or"
*       zero or more repetitions
+       one or more repetitions
?       optional
```

Keywords and symbols appear in quotation marks.

---

# 2. Program

An Arc program consists of one or more function declarations.

```text
program
    ::= function*
```

Example:

```arc
func int main() {
    send 0;
}
```

---

# 3. Functions

A function consists of:

* the `func` keyword
* a return type
* a function name
* a parameter list
* a function body

```text
function
    ::= "func" type identifier "(" parameters? ")" block
```

Examples:

```arc
func int add(int a, int b) {
    send a + b;
}
```

```arc
func void greet() {
    output("Hello");
}
```

---

# 4. Parameters

A function may have zero or more parameters.

```text
parameters
    ::= parameter ("," parameter)*
```

Each parameter consists of a type and identifier.

```text
parameter
    ::= type identifier
```

Example:

```arc
func int add(int a, int b) {
    send a + b;
}
```

---

# 5. Types

Arc supports the following types:

```text
type
    ::= "int"
      | "decimal"
      | "truth"
      | "char"
      | "string"
      | "void"
      | array_type
```

Array types are defined separately.

---

# 6. Array Types

An array type consists of a base type followed by `[]`.

```text
array_type
    ::= primitive_type "[" "]"
```

Primitive types:

```text
primitive_type
    ::= "int"
      | "decimal"
      | "truth"
      | "char"
      | "string"
```

Examples:

```arc
int[] numbers;
string[] names;
truth[] flags;
```

---

# 7. Blocks

A block is a sequence of zero or more statements enclosed in `{` and `}`.

```text
block
    ::= "{" statement* "}"
```

Example:

```arc
{
    int x = 10;
    output(x);
}
```

---

# 8. Statements

A statement can be one of several forms.

```text
statement
    ::= variable_declaration
      | assignment
      | expression_statement
      | return_statement
      | if_statement
      | loop_statement
      | each_statement
      | break_statement
      | skip_statement
      | block
```

---

# 9. Variable Declaration

Variables are declared using a type followed by an identifier.

```text
variable_declaration
    ::= type identifier initializer? ";"
```

An initializer is:

```text
initializer
    ::= "=" expression
```

Examples:

```arc
int x = 10;

decimal price = 3.14;

truth active = yes;

char grade = 'A';

string name = "Arc";
```

A variable may also be initialized using input:

```arc
int age = input();
```

---

# 10. Assignment

An existing variable can be assigned a new value.

```text
assignment
    ::= identifier "=" expression ";"
```

Example:

```arc
x = 20;
```

Expressions can be used on the right-hand side:

```arc
x = x + 10;
```

Array element assignment is also supported:

```text
array_assignment
    ::= identifier "[" expression "]" "=" expression ";"
```

Example:

```arc
numbers[0] = 100;
```

---

# 11. Expression Statements

An expression can be used as a statement.

```text
expression_statement
    ::= expression ";"
```

Example:

```arc
add(10, 20);
```

---

# 12. Return / Send

Arc uses `send` to return a value from a function.

```text
return_statement
    ::= "send" expression? ";"
```

Examples:

```arc
send 10;
```

```arc
send x + y;
```

A `void` function may use:

```arc
send;
```

---

# 13. If Statement

The basic `if` statement is:

```text
if_statement
    ::= "if" "(" expression ")" block
        else_clause?
```

The optional else clause is:

```text
else_clause
    ::= "else" (if_statement | block)
```

This supports:

```arc
if (x > 10) {
    output("Large");
}
```

and:

```arc
if (x > 10) {
    output("Large");
} else {
    output("Small");
}
```

It also supports `else if`:

```arc
if (x > 10) {
    output("Large");
} else if (x > 5) {
    output("Medium");
} else {
    output("Small");
}
```

---

# 14. Loop Statement

Arc's conditional loop uses the `loop` keyword.

```text
loop_statement
    ::= "loop" "(" expression ")" block
```

Example:

```arc
loop (x < 10) {
    x = x + 1;
}
```

The expression must evaluate to a `truth` value.

---

# 15. Each Statement

Arc's counted loop uses `each`.

```text
each_statement
    ::= "each" "(" each_initialization? ";"
                     expression? ";"
                     expression? ")"
                     block
```

Example:

```arc
each (int i = 0; i < 10; i = i + 1) {
    output(i);
}
```

The three components are:

```text
initialization
condition
update
```

---

# 16. Break

The `break` statement terminates the nearest enclosing loop.

```text
break_statement
    ::= "break" ";"
```

Example:

```arc
loop (x < 100) {

    if (x == 50) {
        break;
    }

    x = x + 1;
}
```

Semantic analysis must ensure that `break` occurs inside a loop.

---

# 17. Skip

The `skip` statement skips the current iteration of the nearest enclosing loop.

```text
skip_statement
    ::= "skip" ";"
```

Example:

```arc
each (int i = 0; i < 10; i = i + 1) {

    if (i == 5) {
        skip;
    }

    output(i);
}
```

Semantic analysis must ensure that `skip` occurs inside a loop.

---

# 18. Expressions

Expressions are organized into precedence levels.

```text
expression
    ::= assignment_expression
```

```text
assignment_expression
    ::= logical_or
```

For v1, assignment is handled as a statement rather than as an expression.

---

# 19. Logical OR

```text
logical_or
    ::= logical_and ("||" logical_and)*
```

Example:

```arc
x > 10 || y > 20
```

---

# 20. Logical AND

```text
logical_and
    ::= equality ("&&" equality)*
```

Example:

```arc
x > 10 && y < 20
```

---

# 21. Equality

```text
equality
    ::= relational (("==" | "!=") relational)*
```

Examples:

```arc
x == y
x != y
```

---

# 22. Relational Operators

```text
relational
    ::= additive
        (("<" | ">" | "<=" | ">=") additive)*
```

Examples:

```arc
x < y
x > y
x <= y
x >= y
```

---

# 23. Addition and Subtraction

```text
additive
    ::= multiplicative
        (("+" | "-") multiplicative)*
```

Examples:

```arc
x + y
x - y
```

---

# 24. Multiplication, Division and Modulo

```text
multiplicative
    ::= unary
        (("*" | "/" | "%") unary)*
```

Examples:

```arc
x * y
x / y
x % y
```

---

# 25. Unary Expressions

Arc supports unary negation and logical NOT.

```text
unary
    ::= ("!" | "-") unary
      | primary
```

Examples:

```arc
-x
!active
```

Multiple unary operators are allowed:

```arc
!!active
--x
```

---

# 26. Primary Expressions

```text
primary
    ::= literal
      | identifier
      | function_call
      | array_access
      | input_expression
      | "(" expression ")"
```

---

# 27. Literals

```text
literal
    ::= integer_literal
      | decimal_literal
      | char_literal
      | string_literal
      | truth_literal
```

Truth literals:

```text
truth_literal
    ::= "yes"
      | "no"
```

---

# 28. Function Calls

```text
function_call
    ::= identifier "(" arguments? ")"
```

Arguments:

```text
arguments
    ::= expression ("," expression)*
```

Examples:

```arc
add(10, 20);
```

```arc
int result = multiply(x, y);
```

---

# 29. Input

Input is represented as an expression.

```text
input_expression
    ::= "input" "(" ")"
```

Example:

```arc
int age = input();
```

```arc
string name = input();
```

---

# 30. Output

`output` is treated as a built-in function.

```text
output_statement
    ::= "output" "(" arguments? ")" ";"
```

Examples:

```arc
output("Hello");

output(x);

output("Result:", result);
```

---

# 31. Array Access

Array elements are accessed using zero-based indexing.

```text
array_access
    ::= identifier "[" expression "]"
```

Example:

```arc
numbers[0]
```

```arc
numbers[i]
```

---

# 32. Array Literals

Arrays may be initialized using a list of expressions.

```text
array_literal
    ::= "[" arguments? "]"
```

Examples:

```arc
int[] numbers = [1, 2, 3, 4];
```

```arc
string[] names = ["Alice", "Bob", "Charlie"];
```

All elements must have compatible types.

---

# 33. Identifiers

Identifiers follow these lexical rules:

```text
identifier
    ::= letter (letter | digit | "_")*
```

An identifier may contain letters, digits and underscores but cannot begin with a digit.

Valid:

```text
x
counter
student_name
value1
calculateSum
```

Invalid:

```text
1value
```

Reserved keywords cannot be used as identifiers.

---

# 34. Lexical Elements

The lexer recognizes:

### Keywords

```text
func
send
if
else
loop
each
break
skip
output
input
```

### Types

```text
int
decimal
truth
char
string
void
```

### Truth literals

```text
yes
no
```

### Operators

```text
+
-
*
/
%
=
==
!=
<
>
<=
>=
&&
||
!
```

### Delimiters

```text
(
)
{
}
[
]
,
;
```

---

# 35. Operator Precedence

From highest precedence to lowest:

```text
1.  ()
2.  !, unary -
3.  *, /, %
4.  +, -
5.  <, >, <=, >=
6.  ==, !=
7.  &&
8.  ||
9.  =
```

Example:

```arc
int result = 2 + 3 * 4;
```

is parsed as:

```text
2 + (3 * 4)
```

Parentheses override normal precedence:

```arc
int result = (2 + 3) * 4;
```

---

# 36. Example Program

A complete Arc program:

```arc
func int add(int a, int b) {
    send a + b;
}

func int main() {

    string name = input();

    int age = input();

    if (age >= 18) {
        output("Hello", name);
    } else {
        output("Hello", name);
    }

    int result = add(10, 20);

    each (int i = 0; i < 5; i = i + 1) {
        output(i);
    }

    send result;
}
```

---

# 37. Parser Implementation Mapping

The recursive-descent parser in CipherCompiler will map grammar rules to parsing functions.

For example:

```text
program
    → parseProgram()

function
    → parseFunction()

statement
    → parseStatement()

expression
    → parseExpression()

logical_or
    → parseLogicalOr()

logical_and
    → parseLogicalAnd()

equality
    → parseEquality()

relational
    → parseRelational()

additive
    → parseAdditive()

multiplicative
    → parseMultiplicative()

unary
    → parseUnary()

primary
    → parsePrimary()
```

This structure allows the parser implementation to directly correspond to the formal grammar.

---

# 38. Grammar Design Principle

Arc's grammar is designed so that:

```text
Source Code
     ↓
   Tokens
     ↓
Recursive Descent Parser
     ↓
    AST
```

Each precedence level is represented separately in the grammar.

This avoids ambiguity in arithmetic and logical expressions and makes operator precedence explicit.

The grammar will be extended only when new language features are added to Arc.
