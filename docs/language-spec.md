# Arc Programming Language Specification

**Version:** 1.0
**Compiler:** CipherCompiler
**Language:** Arc

---

## 1. Introduction

Arc is a small, statically typed, procedural programming language developed as part of a Compiler Design project.

Arc is designed to demonstrate the fundamental stages of compiler construction:

```text
Arc Source Code
      ↓
    Lexer
      ↓
   Tokens
      ↓
    Parser
      ↓
     AST
      ↓
Semantic Analysis
      ↓
 Code Generation
      ↓
 x86-64 Assembly
      ↓
 Executable
```

The language provides basic data types, variables, expressions, control flow, functions, arrays, and input/output.

---

# 2. Design Goals

Arc is designed with the following goals:

* Simple and readable syntax
* Static type checking
* Procedural programming support
* Block-based scope
* Familiar operators and expressions
* Simple input/output
* Easy compilation to x86-64 assembly
* Clear compiler error messages

Arc intentionally has a limited feature set so that the complete compiler can be implemented and demonstrated within the scope of the project.

---

# 3. Source Code

Arc source files use the `.arc` extension.

Example:

```arc
func int main() {
    int x = 10;
    send x;
}
```

---

# 4. Comments

Arc supports single-line and multi-line comments.

## 4.1 Single-line comments

Single-line comments begin with `//`.

```arc
// This is a comment
int x = 10;
```

Everything after `//` until the end of the line is ignored by the compiler.

## 4.2 Multi-line comments

Multi-line comments begin with `/*` and end with `*/`.

```arc
/*
   This is a
   multi-line comment.
*/

int x = 10;
```

Comments do not produce tokens during lexical analysis.

---

# 5. Data Types

Arc supports the following data types:

| Type      | Description                   | Example             |
| --------- | ----------------------------- | ------------------- |
| `int`     | Integer values                | `42`                |
| `decimal` | Decimal/floating-point values | `3.14`              |
| `truth`   | Boolean values                | `yes`, `no`         |
| `char`    | Single character              | `'A'`               |
| `string`  | Sequence of characters        | `"Hello"`           |
| `void`    | No return value               | `func void greet()` |

---

# 6. Boolean Values

The `truth` type represents Boolean values.

Arc uses:

```arc
yes
no
```

instead of `true` and `false`.

Example:

```arc
truth isStudent = yes;
truth isFinished = no;
```

Boolean expressions can be used in conditional and loop statements.

---

# 7. Variables

Variables are declared using the following syntax:

```text
datatype variable = value;
```

Examples:

```arc
int age = 20;

decimal price = 99.50;

truth active = yes;

char grade = 'A';

string name = "Arc";
```

A variable may also be initialized using input:

```arc
int age = input();

string name = input();
```

---

# 8. Variable Assignment

Existing variables can be assigned new values.

```arc
int x = 10;

x = 20;
```

Expressions can also be used:

```arc
x = x + 5;
```

The assigned value must be compatible with the variable's declared type.

Invalid example:

```arc
int x = 10;

x = "hello";
```

The compiler must report a type error.

---

# 9. Constants

Arc v1 does not provide a separate constant declaration mechanism.

All declared variables are mutable.

---

# 10. Literals

Arc supports the following literal types.

## 10.1 Integer literals

```arc
0
10
42
1000
```

## 10.2 Decimal literals

```arc
3.14
10.5
0.25
```

## 10.3 Character literals

```arc
'A'
'b'
'7'
```

## 10.4 String literals

```arc
"Hello"
"CipherCompiler"
"Arc Programming Language"
```

## 10.5 Truth literals

```arc
yes
no
```

---

# 11. Arithmetic Operators

Arc supports:

| Operator | Operation      |
| -------- | -------------- |
| `+`      | Addition       |
| `-`      | Subtraction    |
| `*`      | Multiplication |
| `/`      | Division       |
| `%`      | Modulo         |

Examples:

```arc
int a = 10 + 5;

int b = 20 - 8;

int c = 4 * 5;

int d = 20 / 4;

int e = 17 % 5;
```

---

# 12. Comparison Operators

Arc supports:

```text
<
>
<=
>=
==
!=
```

Examples:

```arc
x < y
x > y
x <= y
x >= y
x == y
x != y
```

Comparison expressions produce a `truth` value.

Example:

```arc
truth result = x > 10;
```

---

# 13. Logical Operators

Arc supports:

```text
&&
||
!
```

Where:

| Operator | Meaning     |   |            |
| -------- | ----------- | - | ---------- |
| `&&`     | Logical AND |   |            |
| `        |             | ` | Logical OR |
| `!`      | Logical NOT |   |            |

Example:

```arc
truth result = age >= 18 && active == yes;
```

Example:

```arc
if (age >= 18 || hasPermission == yes) {
    output("Allowed");
}
```

---

# 14. Operator Precedence

Operators are evaluated according to the following precedence, from highest to lowest:

| Priority | Operators            |   |   |
| -------: | -------------------- | - | - |
|        1 | `()`                 |   |   |
|        2 | `!`, unary `-`       |   |   |
|        3 | `*`, `/`, `%`        |   |   |
|        4 | `+`, `-`             |   |   |
|        5 | `<`, `>`, `<=`, `>=` |   |   |
|        6 | `==`, `!=`           |   |   |
|        7 | `&&`                 |   |   |
|        8 | `                    |   | ` |
|        9 | `=`                  |   |   |

Example:

```arc
int result = 2 + 3 * 4;
```

is interpreted as:

```text
2 + (3 * 4)
```

and not:

```text
(2 + 3) * 4
```

Parentheses can be used to explicitly change evaluation order:

```arc
int result = (2 + 3) * 4;
```

---

# 15. Input

Arc provides input using the `input()` expression.

The variable's declared type determines the expected input type.

Examples:

```arc
int age = input();

decimal cgpa = input();

char grade = input();

string name = input();

truth student = input();
```

For `truth`, valid input values are:

```text
yes
no
```

The compiler/runtime is responsible for converting the input into the required type.

---

# 16. Output

Arc provides output using the `output()` function.

Examples:

```arc
output("Hello");

output(x);

output("Age:", age);
```

Multiple expressions may be passed to `output()`.

---

# 17. Conditional Statements

Arc supports `if`, `else if`, and `else`.

## 17.1 If

```arc
if (x > 10) {
    output("Greater");
}
```

## 17.2 If-else

```arc
if (x > 10) {
    output("Greater");
} else {
    output("Smaller or equal");
}
```

## 17.3 Else-if

```arc
if (x > 10) {
    output("Large");
} else if (x > 5) {
    output("Medium");
} else {
    output("Small");
}
```

The condition must evaluate to a `truth` value.

---

# 18. Loops

Arc uses `loop` for conditional repetition.

Syntax:

```arc
loop (condition) {
    statements
}
```

Example:

```arc
int x = 0;

loop (x < 10) {
    output(x);
    x = x + 1;
}
```

The condition is evaluated before every iteration.

---

# 19. Each Loop

Arc provides `each` for counted iteration.

Syntax:

```arc
each (initialization; condition; update) {
    statements
}
```

Example:

```arc
each (int i = 0; i < 10; i = i + 1) {
    output(i);
}
```

The `each` statement is conceptually similar to a traditional `for` loop.

---

# 20. Break

`break` immediately terminates the nearest enclosing loop.

Example:

```arc
loop (x < 100) {

    if (x == 50) {
        break;
    }

    x = x + 1;
}
```

`break` may only appear inside a loop.

---

# 21. Skip

`skip` skips the remainder of the current loop iteration and proceeds to the next iteration.

Example:

```arc
each (int i = 0; i < 10; i = i + 1) {

    if (i % 2 == 0) {
        skip;
    }

    output(i);
}
```

`skip` may only appear inside a loop.

---

# 22. Functions

Functions are declared using the `func` keyword.

Syntax:

```text
func return_type function_name(parameters) {
    statements
}
```

Example:

```arc
func int add(int a, int b) {
    send a + b;
}
```

---

# 23. Function Parameters

Parameters must have an explicitly declared type.

Example:

```arc
func int multiply(int a, int b) {
    send a * b;
}
```

A function can have zero or more parameters.

Example:

```arc
func void greet() {
    output("Hello");
}
```

---

# 24. Return Statement

Arc uses `send` instead of `return`.

Example:

```arc
func int square(int x) {
    send x * x;
}
```

A non-`void` function must return a value compatible with its declared return type.

A `void` function does not return a value.

---

# 25. Function Calls

Functions are called using their name followed by arguments.

Example:

```arc
func int add(int a, int b) {
    send a + b;
}

func int main() {
    int result = add(10, 20);

    output(result);

    send 0;
}
```

The number and types of arguments must match the function's parameter list.

---

# 26. Main Function

Every executable Arc program must contain:

```arc
func int main() {
    ...
}
```

The `main` function is the entry point of the program.

Its return value represents the program's exit status.

Example:

```arc
func int main() {
    output("Hello, Arc!");
    send 0;
}
```

---

# 27. Arrays

Arc supports basic arrays.

Syntax:

```text
datatype[] variable = [values];
```

Example:

```arc
int[] numbers = [10, 20, 30, 40];
```

Elements are accessed using zero-based indexing:

```arc
output(numbers[0]);
output(numbers[2]);
```

Array elements can be modified:

```arc
numbers[1] = 50;
```

All elements of an array must have the same type.

Example:

```arc
int[] numbers = [1, 2, 3, 4];
```

Invalid:

```arc
int[] numbers = [1, "hello", 3];
```

---

# 28. Scope

Arc uses lexical block scope.

A variable declared inside a block is only accessible within that block.

Example:

```arc
func int main() {

    int x = 10;

    if (x > 5) {

        int y = 20;

        output(y);
    }

    // y is not accessible here

    send x;
}
```

Nested blocks may access variables declared in their outer scopes.

---

# 29. Type Rules

Arc is statically typed.

The compiler checks type correctness before generating executable code.

Examples of valid operations:

```arc
int x = 10;
int y = x + 5;
```

```arc
decimal x = 3.14;
decimal y = x + 2.5;
```

Invalid operations must produce compile-time errors.

Example:

```arc
int x = 10;

x = "hello";
```

The compiler should report an error similar to:

```text
Type Error:
Cannot assign value of type string to variable of type int.
```

---

# 30. Type Conversion

Arc v1 keeps implicit type conversion limited.

An `int` may be converted to a `decimal` where required.

Example:

```arc
int x = 10;
decimal y = x;
```

Other incompatible conversions are rejected unless explicitly supported by a future version of Arc.

---

# 31. Statements

The following are valid Arc statements:

```text
Variable declaration
Assignment
Function call
Output
If / else
Loop
Each
Break
Skip
Send
```

Most statements are terminated using a semicolon.

Example:

```arc
int x = 10;
x = x + 1;
output(x);
send x;
```

Block statements use braces:

```arc
{
    int x = 10;
    output(x);
}
```

---

# 32. Semicolons

Arc uses semicolons to terminate statements.

Example:

```arc
int x = 10;

x = 20;

output(x);

send x;
```

Missing semicolons should result in a syntax error.

---

# 33. Identifiers

Identifiers are used for variables and function names.

An identifier:

* may contain letters
* may contain digits
* may contain `_`
* cannot begin with a digit
* cannot be a reserved keyword

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

# 34. Reserved Keywords

The following words are reserved in Arc v1:

```text
int
decimal
truth
char
string
void

yes
no

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

---

# 35. Example Arc Program

The following program demonstrates several Arc features:

```arc
func int add(int a, int b) {
    send a + b;
}

func int main() {

    string name = input();

    int age = input();

    if (age >= 18) {
        output("Hello", name);
        output("You are an adult.");
    } else {
        output("Hello", name);
        output("You are a minor.");
    }

    int result = add(10, 20);

    output("Result:", result);

    each (int i = 0; i < 5; i = i + 1) {
        output(i);
    }

    send 0;
}
```

---

# 36. Compiler Requirements

CipherCompiler will implement the following major compilation stages:

### Lexical Analysis

Converts Arc source code into tokens.

```text
Source Code → Tokens
```

### Syntax Analysis

Checks whether the sequence of tokens follows Arc's grammar.

```text
Tokens → AST
```

### Semantic Analysis

Checks:

* variable declarations
* variable scope
* duplicate declarations
* type compatibility
* function declarations
* function calls
* argument counts
* return types
* valid use of `break`
* valid use of `skip`

```text
AST → Validated AST
```

### Code Generation

Converts the validated AST into x86-64 assembly.

```text
Validated AST → x86-64 Assembly
```

### Execution

The generated assembly can be assembled and linked using an appropriate system toolchain.

```text
Assembly → Executable
```

---

# 37. Version 1 Scope

The first complete implementation of Arc will prioritize:

* `int`
* variables
* arithmetic expressions
* comparison expressions
* logical expressions
* `truth`
* `if / else`
* `loop`
* `each`
* `break`
* `skip`
* functions
* function parameters
* `send`
* lexical scope
* symbol table
* x86-64 code generation

The following features may be implemented after the core compiler is functional:

* `decimal`
* `char`
* `string`
* arrays
* advanced input/output
* compiler optimizations
* improved diagnostics

This staged approach ensures that the fundamental compiler pipeline is completed before optional language features are added.

---

# 38. Future Extensions

Possible future versions of Arc may include:

* structures
* additional data structures
* standard library
* string manipulation
* array iteration using `each`
* explicit type conversion
* constant declarations
* additional optimization passes
* improved error recovery
* modules

These features are outside the scope of Arc v1.

---

# 39. Design Philosophy

Arc aims to remain small enough to understand while still demonstrating the major concepts involved in compiler construction.

The language deliberately combines familiar programming constructs with a small set of custom keywords:

```text
func    → function declaration
send    → return
output  → output
input   → input
loop    → while-style loop
each    → for-style loop
skip    → continue
truth   → boolean type
yes/no  → boolean values
```

This allows Arc to remain easy to learn while giving CipherCompiler a distinct language specification.
