# CipherCompiler

CipherCompiler compiles Arc source files to x86-64 Windows GNU assembly using
the Microsoft x64 calling convention.

## Arc at a glance

Arc is a small, statically typed language with `int`, `decimal`, `truth`,
`char`, `string`, and `void` types. It supports variables and assignment,
arithmetic and comparison expressions, logical operators, `if`/`else`,
`while`/`loop`, `each`, `break`, `skip`, functions with up to four parameters,
`send` returns, and integer `input()` plus `output()`.

```arc
func int add(int left, int right) {
    send left + right;
}

func void main() {
    output(add(2, 3));
}
```

Arrays, typed input other than integers, and string/character escape sequences
are not implemented. See [docs/language-spec.md](docs/language-spec.md) and
[tests/README.md](tests/README.md) for supported behavior and limitations.

## Project structure

```text
Compiler/
├── src/          Java compiler: lexer, parser, AST, semantic analysis, codegen
├── examples/     Arc example programs
├── tests/        Arc integration tests and PowerShell test runner
├── docs/         Language grammar, specification, and compiler architecture
├── out/          Generated Java classes, assembly, and executables (ignored)
├── sources.txt   Java source-file list
├── README.md
└── .gitignore
```

## Windows build requirements

- JDK 17 or later (`javac` and `java`)
- MSYS2 UCRT64 x86-64 MinGW GCC, normally
  `C:\msys64\ucrt64\bin\gcc.exe`

The compiler found on `PATH` may be a 32-bit MinGW compiler. It cannot assemble
the generated x86-64 code. Put the UCRT64 `bin` directory first on `PATH` in
PowerShell before assembling:

```powershell
$env:PATH = "C:\msys64\ucrt64\bin;$env:PATH"
gcc -dumpmachine
```

The expected target is `x86_64-w64-mingw32`.

## Build and run

Run these commands in Windows PowerShell from the `Compiler` directory. They
compile every Java source, compile an Arc source file to assembly, assemble
and link a Windows executable, and run it:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)
java -cp out Main tests\integer_arithmetic.arc out\program.s
$env:PATH = "C:\msys64\ucrt64\bin;$env:PATH"
gcc -o out\program.exe out\program.s
.\out\program.exe
```

Pass an Arc source path and, optionally, an assembly output path to `Main`.
Without arguments it compiles the built-in demonstration program.

## Tests

Run the executable integration suite from PowerShell:

```powershell
powershell -ExecutionPolicy Bypass -File .\tests\run-tests.ps1
```

The runner compiles every Java source, compiles each Arc test, assembles and
links it with x86-64 MinGW GCC, runs the executable, and checks its output.
Set `$env:ARC_GCC` to another compatible `gcc.exe` path if needed. For example:

```powershell
$env:ARC_GCC = "D:\tools\mingw64\bin\gcc.exe"
powershell -ExecutionPolicy Bypass -File .\tests\run-tests.ps1
```

See [tests/README.md](tests/README.md) for coverage and known unsupported
features.