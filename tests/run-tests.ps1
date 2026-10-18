$ErrorActionPreference = "Continue"

$projectRoot = Split-Path -Parent $PSScriptRoot
$classDirectory = Join-Path $projectRoot "out"
$testOutputDirectory = Join-Path $classDirectory "test-run"
$null = New-Item -ItemType Directory -Force -Path $testOutputDirectory

Push-Location $projectRoot
try {
    $javaSources = @(
        Get-ChildItem -Path (Join-Path $projectRoot "src") -Recurse -Filter "*.java" |
            ForEach-Object { $_.FullName }
    )
    & javac -d $classDirectory @javaSources
    if ($LASTEXITCODE -ne 0) {
        throw "javac failed with exit code $LASTEXITCODE."
    }

    if ($env:ARC_GCC) {
        $gcc = $env:ARC_GCC
    } else {
        $defaultGcc = "C:\msys64\ucrt64\bin\gcc.exe"
        $gccCommand = Get-Command gcc -ErrorAction SilentlyContinue
        $gcc = if (Test-Path $defaultGcc) { $defaultGcc } elseif ($gccCommand) { $gccCommand.Source } else { $null }
    }
    if (-not $gcc -or -not (Test-Path $gcc)) {
        throw "A 64-bit MinGW GCC is required. Set ARC_GCC to its gcc.exe path."
    }

    $gccDirectory = Split-Path -Parent $gcc
    $env:PATH = "$gccDirectory;$env:PATH"
    $target = (& $gcc -dumpmachine 2>&1 | Out-String).Trim()
    if ($LASTEXITCODE -ne 0 -or $target -ne "x86_64-w64-mingw32") {
        throw "Expected x86_64-w64-mingw32 GCC; '$gcc' reports '$target'."
    }

    $cases = @(
        @{ Name = "existing integer smoke test"; File = "integer_test.arc"; Expected = @("15", "5", "50", "2") }
        @{ Name = "integer arithmetic and precedence"; File = "integer_arithmetic.arc"; Expected = @("14", "20", "6", "2", "4") }
        @{ Name = "decimal arithmetic"; File = "decimal_arithmetic.arc"; Expected = @("4.750000", "2.250000", "4.375000", "2.800000", "1.500000", "2.500000", "0.500000") }
        @{ Name = "strings and string variables"; File = "strings.arc"; Expected = @("hello", "Arc", "1", "done") }
        @{ Name = "variable declarations and assignments"; File = "variables.arc"; Expected = @("7", "1.500000", "assigned", "20", "10") }
        @{ Name = "if and else branches"; File = "if_else.arc"; Expected = @("then branch", "else branch") }
        @{ Name = "while loop"; File = "while_loop.arc"; Expected = @("10", "5") }
        @{ Name = "each loop"; File = "each_loop.arc"; Expected = @("10") }
        @{ Name = "break and skip"; File = "break_skip.arc"; Expected = @("8") }
        @{ Name = "functions and nested calls"; File = "functions.arc"; Expected = @("7", "1170", "5.500000") }
        @{ Name = "input and output"; File = "input_output.arc"; Input = "7`n8`n"; Expected = @("15", "7") }
        @{ Name = "char and truth"; File = "char_truth.arc"; Expected = @("1", "0", "1", "65") }
        @{ Name = "invalid syntax rejected"; File = "invalid_syntax.arc"; ShouldFail = $true }
        @{ Name = "semantic error rejected"; File = "invalid_semantic.arc"; ShouldFail = $true }
        @{ Name = "loop control outside loops rejected"; File = "invalid_loop_control.arc"; ShouldFail = $true }
        @{ Name = "unsupported function arity rejected"; File = "invalid_function_arity.arc"; ShouldFail = $true }
    )

    $passed = 0
    $failed = 0
    foreach ($case in $cases) {
        $baseName = [System.IO.Path]::GetFileNameWithoutExtension($case.File)
        $source = Join-Path $PSScriptRoot $case.File
        $assembly = Join-Path $testOutputDirectory "$baseName.s"
        $executable = Join-Path $testOutputDirectory "$baseName.exe"

        foreach ($artifact in @($assembly, $executable)) {
            if (Test-Path $artifact) {
                Remove-Item -LiteralPath $artifact -Force
            }
        }

        try {
            $compileOutput = & java -cp $classDirectory Main $source $assembly 2>&1
            $compileExit = $LASTEXITCODE
            if ($case.ShouldFail) {
                if ($compileExit -ne 0 -and -not (Test-Path $assembly)) {
                    Write-Output "PASS $($case.Name)"
                    $passed++
                } else {
                    throw "Expected compilation failure; exit=$compileExit; output=$($compileOutput -join ' ')"
                }
                continue
            }
            if ($compileExit -ne 0) {
                throw "Arc compilation failed (exit $compileExit): $($compileOutput -join ' ')"
            }

            $linkOutput = & $gcc -o $executable $assembly 2>&1
            if ($LASTEXITCODE -ne 0) {
                throw "Assembly/link failed: $($linkOutput -join ' ')"
            }

            $startInfo = New-Object System.Diagnostics.ProcessStartInfo
            $startInfo.FileName = $executable
            $startInfo.WorkingDirectory = $projectRoot
            $startInfo.UseShellExecute = $false
            $startInfo.RedirectStandardInput = $true
            $startInfo.RedirectStandardOutput = $true
            $startInfo.RedirectStandardError = $true
            $process = New-Object System.Diagnostics.Process
            $process.StartInfo = $startInfo
            $null = $process.Start()
            $stdoutTask = $process.StandardOutput.ReadToEndAsync()
            $stderrTask = $process.StandardError.ReadToEndAsync()
            $process.StandardInput.Write([string]$case.Input)
            $process.StandardInput.Close()

            if (-not $process.WaitForExit(10000)) {
                $process.Kill()
                $process.WaitForExit()
                throw "Program exceeded the 10-second runtime limit."
            }
            $actual = $stdoutTask.Result
            $runtimeError = $stderrTask.Result
            $runtimeExit = $process.ExitCode
            $process.Dispose()
            if ($runtimeExit -ne 0) {
                throw "Program exited with $runtimeExit`: $runtimeError"
            }
            if ($runtimeError) {
                throw "Program wrote to stderr: $runtimeError"
            }

            $actual = [regex]::Replace($actual, "\r\n?", "`n")
            if ($actual.EndsWith("`n")) {
                $actual = $actual.Substring(0, $actual.Length - 1)
            }
            $expected = [string]::Join("`n", [string[]]$case.Expected)
            if ($actual -cne $expected) {
                throw "Output mismatch. Expected '$expected'; got '$actual'."
            }

            Write-Output "PASS $($case.Name)"
            $passed++
        } catch {
            Write-Output "FAIL $($case.Name): $($_.Exception.Message)"
            $failed++
        }
    }

    Write-Output ""
    Write-Output "Result: $passed passed; $failed failed."
    if ($failed -gt 0) {
        exit 1
    }
} finally {
    Pop-Location
}
