# CipherCompiler

CipherCompiler compiles Arc source files to x86-64 Windows GNU assembly using
the Microsoft x64 calling convention.

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

Run these commands in PowerShell from the `Compiler` directory:

```powershell
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
Set `$env:ARC_GCC` to another compatible `gcc.exe` path if needed.
See [tests/README.md](tests/README.md) for coverage and known unsupported
features.